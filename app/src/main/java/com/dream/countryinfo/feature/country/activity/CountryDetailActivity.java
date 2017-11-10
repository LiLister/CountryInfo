package com.dream.countryinfo.feature.country.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.dream.countryinfo.CountryApp;
import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.CountryDetail;
import com.dream.countryinfo.network.CountryApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryDetailActivity extends BaseActivity {
    final private static String KEY_COUNTRY_NAME = "COUNTRY_NAME";

    private String countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        countryName = getIntent().getStringExtra(KEY_COUNTRY_NAME);

        initView();

        initData();
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

        CountryApi countryApi = CountryApp.getApplication().getCountryApi();

        Call<List<CountryDetail>> call = countryApi.getCountriesByName(countryName, "name;capital;region;" +
                "nativeName;languages;flag;translations;area;latlng");

        call.enqueue(new Callback<List<CountryDetail>>() {
            @Override
            public void onResponse(Call<List<CountryDetail>> call, Response<List<CountryDetail>> response) {
                List<CountryDetail> countryDetails = response.body();
                if (countryDetails.size() > 0) {
                    CountryDetail countryDetail = countryDetails.get(0);

                    updateView(countryDetail);
                }

                hideLoading();
                Log.e("Ok", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<CountryDetail>> call, Throwable t) {
                hideLoading();
                Log.e("failed", t.getMessage());
            }
        });
    }

    private void updateView(CountryDetail countryDetail) {
        WebView webView = findViewById(R.id.webView);

        // TODO user a HTML template to change the SVG size in webview
        webView.loadUrl(countryDetail.getFlag());

        // TODO handle MapBox -- set a marker with location get from countryDetail

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
        tvTranslations.setText(String.format(getResources().getString(R.string.country_capital),
                countryDetail.getTranslationOfGerman()));
    }

    public static void startMe(Context context, String countryName) {
        Intent intent = new Intent();
        intent.putExtra(KEY_COUNTRY_NAME, countryName);

        intent.setClass(context, CountryDetailActivity.class);
        context.startActivity(intent);
    }
}
