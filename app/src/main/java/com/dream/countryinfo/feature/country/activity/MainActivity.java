package com.dream.countryinfo.feature.country.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dream.countryinfo.CountryApp;
import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.CountryDetail;
import com.dream.countryinfo.feature.country.adapter.CountryNamesAdapter;
import com.dream.countryinfo.network.CountryApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    private SearchView searchView;
    private ListView listView;
    private CountryNamesAdapter countryNamesAdapter = new CountryNamesAdapter();

    private List<String> countryNamesSearched = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.main_activity_title);

        initView();
    }

    private void initView() {
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CountryApi countryApi = CountryApp.getApplication().getCountryApi();

                // countryApi.getAllCountries("name");

                Call<List<Map<String, String>>> call = countryApi.searchCountriesByName(s, "name");

                call.enqueue(new Callback<List<Map<String, String>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                        countryNamesSearched.clear();
                        for (Map<String, String> item : response.body()) {

                            countryNamesSearched.add(item.get("name"));
                        }

                        countryNamesAdapter.setCountryNames(countryNamesSearched);
                        countryNamesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "failed to retrieve country names",
                                Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {



//                Observable<String> observable = countryApi.getAllCountries("name");
//                observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                new Subscriber<String>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        hideLoading();
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        hideLoading();
//                                    }
//
//                                    @Override
//                                    public void onNext(String responseStr) {
//                                        List<Map<String, String>> countryNames = new Gson().fromJson(responseStr,
//                                                new ArrayList<Map<String, String>>().getClass());
//                                        List<String> names = new ArrayList<>();
//                                        for (Map<String, String> item: countryNames) {
//                                            names.add(item.get("name"));
//                                        }
//
//                                        countryNamesAdapter.setCountryNames(names);
//                                        countryNamesAdapter.notifyDataSetChanged();
//                                    }
//
//                                    @Override
//                                    public void onStart() {
//                                        super.onStart();
//                                        showLoading("");
//                                    }
//                                }
//
//                        );

                return false;
            }
        });

        listView = findViewById(R.id.lv_country_names);
        listView.setAdapter(countryNamesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String countryName = countryNamesSearched.get(i);

                showLoading();

                CountryApi countryApi = CountryApp.getApplication().getCountryApi();

                Call<List<CountryDetail>> call = countryApi.getCountriesByName(countryName, "");

                call.enqueue(new Callback<List<CountryDetail>>() {
                    @Override
                    public void onResponse(Call<List<CountryDetail>> call, Response<List<CountryDetail>> response) {
                        hideLoading();
                        Log.e("Ok", response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<List<CountryDetail>> call, Throwable t) {
                        hideLoading();
                        Log.e("failed", t.getMessage());
                    }
                });

            }
        });

    }



}
