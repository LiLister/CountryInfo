package com.dream.countryinfo.network;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;

import com.dream.countryinfo.BuildConfig;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lixingming on 09/11/2017.
 */

public class CountryApiCreator {
    private Context mContext;

    private OkHttpClient okHttpClient;

    public CountryApiCreator(Context context) {
        mContext = context;
    }

    public CountryApi createApi(String endpoint) {
        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();

        okHttpClient = newBuilder.readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(mContext.getCacheDir(), 50 * 1024*1024)) // 设置缓存大小
                .addInterceptor(new NetworkCheckInterceptor())
                .build();

        if (BuildConfig.DEBUG) {
            IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttpClient));
        }


        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(CountryApi.class);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    class NetworkCheckInterceptor implements Interceptor {

        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if (!NetworkManager.isConnected()) {
                if ("GET".equals(request.method().toUpperCase())) {
                    long maxStale = 60 * 60 * 24 * 7; // tolerate 1-week stale
                    request = request.newBuilder().addHeader("Cache-Control",
                            "public, only-if-cached, max-stale=" + maxStale).build();
                } else {
                    throw new IOException("No network access available.");
                }
            }

            return chain.proceed(request);
        }
    }
}
