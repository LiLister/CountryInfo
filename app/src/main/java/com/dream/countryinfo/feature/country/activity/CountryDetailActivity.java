package com.dream.countryinfo.feature.country.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.CountryDetail;
import com.dream.countryinfo.network.CountryApiHelper;
import com.dream.countryinfo.network.NetworkManager;
import com.dream.countryinfo.network.OkHttpHelper;
import com.dream.countryinfo.util.LogUtil;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.lang.ref.SoftReference;
import java.util.List;

public class CountryDetailActivity extends BaseActivity {
    final public static String KEY_COUNTRY_NAME = "COUNTRY_NAME";

    private String countryName;

    private MapView mapView;

    private  ImageView imgvFlag;
    private ProgressBar progressBar;

    private CountryDetail countryDetail;

    private boolean mapReady = false;
    private boolean countryInfoReady = false;

    private SoftReference<MapboxMap> refMapboxMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        countryName = getIntent().getStringExtra(KEY_COUNTRY_NAME);

        initView();

        initData();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                refMapboxMap = new SoftReference<>(mapboxMap);
                mapReady = true;

                updateMap();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        mapView.onStart();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeActionContentDescription("Back");
            actionBar.setTitle(countryName);
        } else {
            setTitle(countryName);
        }
    }

    private void initData() {
        showLoading();

        CountryApiHelper.getSingleton().getCountriesByName(countryName, "name;capital;region;" +
                        "nativeName;languages;flag;translations;area;latlng",
                new CountryApiHelper.MyCallback<List<CountryDetail>>() {
                    @Override
                    public void onResponse(List<CountryDetail> countryDetails) {
                        if (countryDetails != null && countryDetails.size() > 0) {
                            countryDetail = countryDetails.get(0);
                            countryInfoReady = true;

                            updateView();

                            updateMap();
                        }

                        hideLoading();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        hideLoading();
                        LogUtil.e(this.getClass().getName(), t.getMessage());
                        safeToast("Failed to get country detail.");
                        finish();
                    }
                });
    }

    private void updateView() {
        imgvFlag = findViewById(R.id.imgv_flag);
        imgvFlag.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        renderSVGToImageView();

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvNativeName = findViewById(R.id.tv_native_name);
        TextView tvRegion = findViewById(R.id.tv_region);
        TextView tvCapital= findViewById(R.id.tv_capital);
        TextView tvLanguages = findViewById(R.id.tv_languages);
        TextView tvTranslations = findViewById(R.id.tv_translations);

        tvName.setText(String.format(getResources().getString(R.string.country_name), countryDetail.getName()));
        tvNativeName.setText(String.format(getResources().getString(R.string.country_native_name),
                countryDetail.getNativeName()));
        tvRegion.setText(String.format(getResources().getString(R.string.country_region), countryDetail.getRegion()));
        tvCapital.setText(String.format(getResources().getString(R.string.country_capital),
                countryDetail.getCapital()));
        tvLanguages.setText(String.format(getResources().getString(R.string.country_languages),
                countryDetail.getLanguageNames()));
        tvTranslations.setText(String.format(getResources().getString(R.string.country_translations),
                countryDetail.getTranslationOfGerman()));
    }

    private void renderSVGToImageView() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        final SoftReference<CountryDetailActivity> refActivity = new SoftReference<>(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CountryDetailActivity activity = refActivity.get();
                if (activity != null) {
                    final String flagSVG = OkHttpHelper.getString(activity, activity.countryDetail.getFlag());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(flagSVG)) {
                                try {
                                    SVG svg = SVG.getFromString(flagSVG);
                                    Drawable drawable = new PictureDrawable(svg.renderToPicture());
                                    imgvFlag.setImageDrawable(drawable);

                                    activity.progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    activity.progressBar.setVisibility(View.GONE);
                                    safeToast("Failed to parse flag svg");
                                }
                            } else {
                                activity.progressBar.setVisibility(View.GONE);
                                safeToast("Failed to download flag svg");
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void updateMap() {
        if (mapReady && countryInfoReady) {
            // set a marker with location get from countryDetail

            MapboxMap mapboxMap = refMapboxMap.get();
            if (mapboxMap != null) {

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(countryDetail.getLatitude(), countryDetail.getLongitude()))
                        .title("Eiffel Tower")
                );

                mapboxMap.setLatLng(new LatLng(countryDetail.getLatitude(), countryDetail.getLongitude()));
            }
        }
    }

    public static void startMe(Context context, String countryName) {
        Intent intent = new Intent();
        intent.putExtra(KEY_COUNTRY_NAME, countryName);

        intent.setClass(context, CountryDetailActivity.class);
        context.startActivity(intent);
    }
}
