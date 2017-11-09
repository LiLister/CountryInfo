package com.dream.countryinfo.network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lixingming on 09/11/2017.
 */

public interface CountryApi {

    @GET("rest/v2/name/{name}")
    Call<List<Map<String, String>>> getCountriesByName(@Path("name") String name, @Query("fields") String fields);

    @GET("rest/v2/all")
    Call<List<Map<String, String>>> getAllCountries(@Query("fields") String fields);

    @GET("rest/v2/name/{name}")
    Observable<String> getOneCountry(@Path("name") String name, @Query("fields") String fields);

}
