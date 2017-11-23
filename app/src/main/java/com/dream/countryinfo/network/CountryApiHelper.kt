package com.dream.countryinfo.network

import com.dream.countryinfo.CountryApp
import com.dream.countryinfo.feature.country.CountryDetail

import java.lang.ref.SoftReference
import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by lixingming on 12/11/2017.
 */

class CountryApiHelper private constructor() {

    private var listRefCall: MutableList<SoftReference<Call<List<Map<String, String>>>>> =
            ArrayList<SoftReference<Call<List<Map<String, String>>>>>().toMutableList()

    private var countryApi: CountryApi? = null

    fun searchCountriesByName(searchText: String, fields: String, callback: MyCallback<List<Map<String, String>>>) {
        val call = countryApi!!.searchCountriesByName(searchText, fields)
        call.enqueue(DefaultCallback(callback))

        listRefCall.add(SoftReference(call))
    }

    fun getCountriesByName(countryName: String, fields: String, callback: MyCallback<List<CountryDetail>>) {
        val call = countryApi!!.getCountriesByName(countryName, fields)
        call.enqueue(DefaultCallback(callback))
    }

    fun cancelSearchCountryCalls() {
        for (ref in listRefCall) {
            if (ref.get() != null) {
                (ref.get() as Call<List<Map<String, String>>>).cancel()
            }
        }
        listRefCall.clear()
    }

    internal inner class DefaultCallback<T>(private val callback: MyCallback<T>) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!call.isCanceled)
                callback.onResponse(response.body())
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!call.isCanceled)
                callback.onFailure(t)
        }
    }

    interface MyCallback<T> {
        // this callback just ignore the call
        fun onResponse(responseData: T?)

        // this callback just ignore the call
        fun onFailure(t: Throwable)
    }

    companion object {

        @Volatile private var instance: CountryApiHelper? = null

        fun getSingleton(): CountryApiHelper {
            if (instance == null) {
                synchronized(CountryApiHelper::class.java) {
                    if (instance == null) {
                        instance = CountryApiHelper()
                        val apiCreator = CountryApiCreator(CountryApp.application!!
                                .getApplicationContext())
                        instance!!.countryApi = apiCreator.createApi("https://restcountries.eu/")

//                        instance!!.listRefCall = ArrayList()
                    }
                }
            }
            return instance!!
        }
    }
}
