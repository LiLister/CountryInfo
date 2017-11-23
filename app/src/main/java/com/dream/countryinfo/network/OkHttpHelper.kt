package com.dream.countryinfo.network

import android.content.Context

import java.util.concurrent.TimeUnit

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by lixingming on 13/11/2017.
 */

object OkHttpHelper {

    fun getString(context: Context, url: String): String? {
        val newBuilder = OkHttpClient.Builder()

        val okHttpClient = newBuilder.readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(Cache(context.cacheDir, (50 * 1024 * 1024).toLong())) // 设置缓存大小
                .build()

        val request = Request.Builder().url(url).build()
        try {
            val response = okHttpClient.newCall(request).execute()
            return response.body()!!.string()
        } catch (e: Exception) {
            return null
        }

    }
}
