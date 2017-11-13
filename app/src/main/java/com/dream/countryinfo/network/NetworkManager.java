package com.dream.countryinfo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dream.countryinfo.CountryApp;

/**
 * Created by lixingming on 09/11/2017.
 */

public class NetworkManager {

    public static boolean isConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager)CountryApp.getApplication().getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager != null) {
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }
}
