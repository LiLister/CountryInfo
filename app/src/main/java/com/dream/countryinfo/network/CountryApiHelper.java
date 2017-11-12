package com.dream.countryinfo.network;

import com.dream.countryinfo.CountryApp;
import com.dream.countryinfo.feature.country.CountryDetail;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lixingming on 12/11/2017.
 */

public class CountryApiHelper {

    private List<SoftReference<Call<List<Map<String, String>>>>> listRefCall;

    private CountryApi countryApi;

    private volatile static CountryApiHelper singleton;

    private CountryApiHelper (){}

    public static CountryApiHelper getSingleton() {
        if (singleton == null) {
            synchronized (CountryApiHelper.class) {
                if (singleton == null) {
                    singleton = new CountryApiHelper();
                    singleton.countryApi = CountryApp.getApplication().getCountryApi();
                    singleton.listRefCall = new ArrayList<>();
                }
            }
        }
        return singleton;
    }

    public void searchCountriesByName(String searchText, String fields, MyCallback<List<Map<String, String>>>
            callback) {
        Call<List<Map<String, String>>> call = countryApi.searchCountriesByName(searchText, fields);
        call.enqueue(new DefaultCallback<List<Map<String, String>>>(callback));

        listRefCall.add(new SoftReference<Call<List<Map<String, String>>>>(call));
    }

    public void getCountriesByName(String countryName, String fields, MyCallback<List<CountryDetail>> callback) {
        Call<List<CountryDetail>> call = countryApi.getCountriesByName(countryName, fields);
        call.enqueue(new DefaultCallback<List<CountryDetail>>(callback));
    }

    public void cancelSearchCountryCalls() {
        for (SoftReference<Call<List<Map<String, String>>>> ref : listRefCall) {
            if (ref.get() != null) {
                ref.get().cancel();
            }
        }
        listRefCall.clear();
    }

    class DefaultCallback<T> implements Callback<T> {
        private MyCallback<T> callback;

        public DefaultCallback(MyCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (!call.isCanceled())
                callback.onResponse(response.body());
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (!call.isCanceled())
                callback.onFailure(t);
        }
    }

    public interface MyCallback<T> {
        // this callback just ignore the call
        void onResponse(T responseData);

        // this callback just ignore the call
        void onFailure(Throwable t);
    }
}
