package com.dream.countryinfo.feature.country.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;
import android.widget.TextView;

import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.CountryDetail;
import com.dream.countryinfo.network.CountryApiHelper;
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
        WebView webView = findViewById(R.id.webView);

        //  user a HTML template to change the SVG size in webview
//        webView.loadUrl(countryDetail.getFlag());
        webView.loadDataWithBaseURL(null, getHtml(countryDetail.getFlag()), "text/html",  "utf-8", null);

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

    private String getHtml(String flagUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<!DOCTYPE html>");
        stringBuilder.append("<html lang=\"en\"><head>" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "  <meta name=\"description\" content=\"\">\n" +
                "  <meta name=\"HandheldFriendly\" content=\"True\">\n" +
                "  <meta name=\"MobileOptimized\" content=\"320\">\n" +
                "  <meta name=\"full-screen\" content=\"no\">\n" +
                "  <meta name=\"viewport\" content=\"width=320,maximum-scale=1.3,width=device-width, initial-scale=1,user-scalable=no\">\n" +
                "  </head>");
        stringBuilder.append("<body>");
        stringBuilder.append("<embed src=\"");
        stringBuilder.append(flagUrl);
        stringBuilder.append("\" width=\"l60\" height=\"90\" type=\"image/svg+xml\" ");
        stringBuilder.append("pluginspage=\"http://www.adobe.com/svg/viewer/install/\" /> ");
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");

        return stringBuilder.toString();
    }

    public static void startMe(Context context, String countryName) {
        Intent intent = new Intent();
        intent.putExtra(KEY_COUNTRY_NAME, countryName);

        intent.setClass(context, CountryDetailActivity.class);
        context.startActivity(intent);
    }
}
