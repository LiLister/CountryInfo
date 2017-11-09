package com.dream.countryinfo;

import android.app.Application;

import com.dream.countryinfo.network.CountryApi;
import com.dream.countryinfo.network.CountryApiCreator;

/**
 * Created by lixingming on 09/11/2017.
 */

public class CountryApp extends Application {

    private static CountryApp instance;
    private CountryApi countryApi;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static CountryApp getApplication() {
        return instance;
    }

    public CountryApi getCountryApi() {
        if (countryApi == null) {
            CountryApiCreator apiCreator = new CountryApiCreator(getApplicationContext());
            countryApi = apiCreator.createApi("https://restcountries.eu/");
        }
        return countryApi;
    }

}
