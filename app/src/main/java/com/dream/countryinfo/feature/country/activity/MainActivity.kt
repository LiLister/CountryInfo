package com.dream.countryinfo.feature.country.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast

import com.dream.countryinfo.R
import com.dream.countryinfo.activity.BaseActivity
import com.dream.countryinfo.feature.country.adapter.CountryNamesAdapter
import com.dream.countryinfo.network.CountryApiHelper

import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {

    private var progressBar: ProgressBar? = null
    private val countryNamesAdapter = CountryNamesAdapter()

    private val countryNamesSearched = ArrayList<String>()

    private val scheduledExecutor = Executors.newScheduledThreadPool(10)

    private var currentSearchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle(R.string.main_activity_title)

        initView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun initView() {
        progressBar = findViewById(R.id.progressBar)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                // do instant search when text changed
                currentSearchText = s

                startSearch(s)

                return true
            }
        })

        val listView = findViewById<ListView>(R.id.lv_country_names)
        listView.adapter = countryNamesAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val countryName = countryNamesSearched[i]

            CountryDetailActivity.startMe(this@MainActivity, countryName)
        }
    }

    private fun startSearch(s: String) {
        schedule(SearchThread(s), searchDelayMills)
    }

    private fun schedule(command: Runnable, delayTimeMills: Long): ScheduledFuture<*> {
        return scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS)
    }

    private inner class SearchThread(internal var newText: String?) : Thread() {

        override fun run() {
            // keep only one thread to load current search text
            if (newText != null && newText == currentSearchText) {
                runOnUiThread { doSearch(newText!!) }
            }
        }
    }

    private fun doSearch(s: String) {
        CountryApiHelper.getSingleton()!!.cancelSearchCountryCalls()

        if (TextUtils.isEmpty(s)) {
            countryNamesSearched.clear()
            countryNamesAdapter.setCountryNames(countryNamesSearched)
            countryNamesAdapter.notifyDataSetChanged()
            return
        }

        progressBar!!.visibility = View.VISIBLE

        CountryApiHelper.getSingleton()!!.searchCountriesByName(s, "name",
                object : CountryApiHelper.MyCallback<List<Map<String, String>>> {
                    override fun onResponse(responseData: List<Map<String, String>>?) {
                        countryNamesSearched.clear()

                        if (responseData != null) {
                            for (item in responseData) {
                                countryNamesSearched.add(item["name"]!!)
                            }
                        }

                        countryNamesAdapter.setCountryNames(countryNamesSearched)
                        countryNamesAdapter.notifyDataSetChanged()

                        progressBar!!.visibility = View.GONE
                    }

                    override fun onFailure(t: Throwable) {
                        progressBar!!.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Failed to retrieve country names. " + t.message,
                                Toast.LENGTH_LONG).show()
                    }
                })
    }

    companion object {

        private val searchDelayMills = 500L
    }
}
