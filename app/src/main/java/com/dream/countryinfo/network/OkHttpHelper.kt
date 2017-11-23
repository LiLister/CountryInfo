package com.dream.countryinfo.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

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
        var result: String? = null
        try {
            val response = okHttpClient.newCall(request).execute()
            result = response.body()!!.string()
        } catch (e: Exception) {

        }

        return result
    }
}
