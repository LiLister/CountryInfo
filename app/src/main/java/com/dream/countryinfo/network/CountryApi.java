package com.dream.countryinfo.network;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lixingming on 09/11/2017.
 */

public interface CountryApi {

    @GET("rest/v2/all?fields={fields}")
    Observable<List<Map<String, String>>> getAllCountries(@Path("fields") String fields);

    @GET("rest/v2/all?fields={fields}")
    Observable<String> getOneCountry(@Path("fields") String fields);

}
