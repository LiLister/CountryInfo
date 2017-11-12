package com.dream.countryinfo;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

/**
 * Created by lixingming on 09/11/2017.
 */

public class CountryApp extends Application {

    private static CountryApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_sdk_token));
    }

    public static CountryApp getApplication() {
        return instance;
    }

}
