package com.dream.countryinfo.network

import android.content.Context
import android.support.test.espresso.IdlingRegistry
import com.dream.countryinfo.BuildConfig
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by lixingming on 09/11/2017.
 */

class CountryApiCreator(private val mContext: Context) {

    fun createApi(endpoint: String): CountryApi {
        val newBuilder = OkHttpClient.Builder()

        val okHttpClient = newBuilder.readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(Cache(mContext.cacheDir, (50 * 1024 * 1024).toLong())) // set the cache size
                .addInterceptor(NetworkCheckInterceptor())
                .build()

        if (BuildConfig.DEBUG) {
            IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttpClient))
        }


        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit.create(CountryApi::class.java)
    }

    internal inner class NetworkCheckInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            if (!NetworkManager.isConnected) {
                throw IOException("No network access available.")
            }

            return chain.proceed(request)
        }
    }
}
