package com.dream.countryinfo.network

import com.dream.countryinfo.feature.country.CountryDetail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by lixingming on 09/11/2017.
 */

interface CountryApi {

    @GET("rest/v2/name/{name}?fullText=true")
    fun getCountriesByName(@Path("name") name: String, @Query("fields") fields: String): Call<List<CountryDetail>>

    @GET("rest/v2/name/{name}")
    fun searchCountriesByName(@Path("name") name: String, @Query("fields") fields: String): Call<List<Map<String, String>>>

    @GET("rest/v2/all")
    fun getAllCountries(@Query("fields") fields: String): Call<List<Map<String, String>>>

}

