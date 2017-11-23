package com.dream.countryinfo

import android.app.Application

import com.mapbox.mapboxsdk.Mapbox

/**
 * Created by lixingming on 09/11/2017.
 */

class CountryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        application = this

        Mapbox.getInstance(applicationContext, resources.getString(R.string.mapbox_sdk_token))
    }

    companion object {

        var application: CountryApp? = null
            private set
    }

}
