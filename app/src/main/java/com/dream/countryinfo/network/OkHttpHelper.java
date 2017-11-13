package com.dream.countryinfo.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lixingming on 13/11/2017.
 */

public class OkHttpHelper {

    public static String getString(Context context, String url) {
        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();

        OkHttpClient okHttpClient = newBuilder.readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(context.getCacheDir(), 50 * 1024*1024)) // 设置缓存大小
                .build();

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}
