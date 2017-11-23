package com.dream.countryinfo.feature.country.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.caverock.androidsvg.SVG
import com.dream.countryinfo.R
import com.dream.countryinfo.activity.BaseActivity
import com.dream.countryinfo.feature.country.CountryDetail
import com.dream.countryinfo.network.CountryApiHelper
import com.dream.countryinfo.network.OkHttpHelper
import com.dream.countryinfo.util.LogUtil
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import java.lang.ref.SoftReference

class CountryDetailActivity : BaseActivity() {

    private lateinit var countryName: String

    private var mapView: MapView? = null

    private var imgvFlag: ImageView? = null
    private var progressBar: ProgressBar? = null

    private var countryDetail: CountryDetail? = null

    private var mapReady = false
    private var countryInfoReady = false

    private var refMapboxMap: SoftReference<MapboxMap>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        countryName = intent.getStringExtra(KEY_COUNTRY_NAME)

        initView()

        initData()

        mapView = findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap ->
            refMapboxMap = SoftReference(mapboxMap)
            mapReady = true

            updateMap()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        mapView!!.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView!!.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mapView!!.onSaveInstanceState(outState!!)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        mapView!!.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        mapView!!.onStart()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun initView() {
        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setHomeActionContentDescription("Back")
            actionBar.title = countryName
        } else {
            title = countryName
        }
    }

    private fun initData() {
        showLoading()

        CountryApiHelper.getSingleton().getCountriesByName(countryName, "name;capital;region;" + "nativeName;languages;flag;translations;area;latlng",
                object : CountryApiHelper.MyCallback<List<CountryDetail>> {
                    override fun onResponse(responseData: List<CountryDetail>?) {
                        if (responseData != null && responseData.isNotEmpty()) {
                            countryDetail = responseData[0]
                            countryInfoReady = true

                            updateView()

                            updateMap()
                        }

                        hideLoading()
                    }

                    override fun onFailure(t: Throwable) {
                        hideLoading()
                        LogUtil.e(this.javaClass.name, "" + t.message)
                        safeToast("Failed to get country detail.")
                        finish()
                    }
                })
    }

    private fun updateView() {
        imgvFlag = findViewById(R.id.imgv_flag)
        imgvFlag!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        renderSVGToImageView()

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvNativeName = findViewById<TextView>(R.id.tv_native_name)
        val tvRegion = findViewById<TextView>(R.id.tv_region)
        val tvCapital = findViewById<TextView>(R.id.tv_capital)
        val tvLanguages = findViewById<TextView>(R.id.tv_languages)
        val tvTranslations = findViewById<TextView>(R.id.tv_translations)

        tvName.text = String.format(resources.getString(R.string.country_name), countryDetail!!.name)
        tvNativeName.text = String.format(resources.getString(R.string.country_native_name),
                countryDetail!!.nativeName)
        tvRegion.text = String.format(resources.getString(R.string.country_region), countryDetail!!.region)
        tvCapital.text = String.format(resources.getString(R.string.country_capital),
                countryDetail!!.capital)
        tvLanguages.text = String.format(resources.getString(R.string.country_languages),
                countryDetail!!.languageNames)
        tvTranslations.text = String.format(resources.getString(R.string.country_translations),
                countryDetail!!.translationOfGerman)
    }

    private fun renderSVGToImageView() {
        progressBar = findViewById(R.id.progressBar)
        progressBar!!.visibility = View.VISIBLE
        val refActivity = SoftReference(this)
        Thread(Runnable {
            val activity = refActivity.get()
            if (activity != null) {
                val flagSVG = OkHttpHelper.getString(activity, "" + activity.countryDetail!!.flag)
                runOnUiThread {
                    if (!TextUtils.isEmpty(flagSVG)) {
                        try {
                            val svg = SVG.getFromString(flagSVG!!)
                            val drawable = PictureDrawable(svg.renderToPicture())
                            imgvFlag!!.setImageDrawable(drawable)

                            activity.progressBar!!.visibility = View.GONE
                        } catch (e: Exception) {
                            activity.progressBar!!.visibility = View.GONE
                            safeToast("Failed to parse flag svg")
                        }

                    } else {
                        activity.progressBar!!.visibility = View.GONE
                        safeToast("Failed to download flag svg")
                    }
                }
            }
        }).start()
    }

    private fun updateMap() {
        if (mapReady && countryInfoReady) {
            // set a marker with location get from countryDetail

            val mapboxMap = refMapboxMap!!.get()
            if (mapboxMap != null) {

                mapboxMap.addMarker(MarkerOptions()
                        .position(LatLng(countryDetail!!.latitude, countryDetail!!.longitude))
                        .title("Eiffel Tower")
                )

                mapboxMap.setLatLng(LatLng(countryDetail!!.latitude, countryDetail!!.longitude))
            }
        }
    }

    companion object {
        val KEY_COUNTRY_NAME = "COUNTRY_NAME"

        fun startMe(context: Context, countryName: String) {
            val intent = Intent()
            intent.putExtra(KEY_COUNTRY_NAME, countryName)

            intent.setClass(context, CountryDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
