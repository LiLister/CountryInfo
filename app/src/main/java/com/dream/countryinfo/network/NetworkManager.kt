package com.dream.countryinfo.network

import android.content.Context
import android.net.ConnectivityManager
import com.dream.countryinfo.CountryApp

/**
 * Created by lixingming on 09/11/2017.
 */

object NetworkManager {

    val isConnected: Boolean
        get() {
            val mConnectivityManager = CountryApp.application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as
                    ConnectivityManager?
            if (mConnectivityManager != null) {
                val mNetworkInfo = mConnectivityManager.activeNetworkInfo
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable
                }
            }

            return false
        }
}
