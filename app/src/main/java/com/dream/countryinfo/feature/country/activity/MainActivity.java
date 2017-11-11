package com.dream.countryinfo.feature.country.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dream.countryinfo.CountryApp;
import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.adapter.CountryNamesAdapter;
import com.dream.countryinfo.network.CountryApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
                        Toast.makeText(MainActivity.this, "Failed to retrieve country names. " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO do instant search when text changed


                return false;
            }
        });

        listView = findViewById(R.id.lv_country_names);
        listView.setAdapter(countryNamesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String countryName = countryNamesSearched.get(i);

                CountryDetailActivity.startMe(MainActivity.this, countryName);



            }
        });

    }



}
