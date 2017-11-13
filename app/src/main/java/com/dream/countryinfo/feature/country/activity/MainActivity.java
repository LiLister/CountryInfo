package com.dream.countryinfo.feature.country.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.dream.countryinfo.R;
import com.dream.countryinfo.activity.BaseActivity;
import com.dream.countryinfo.feature.country.adapter.CountryNamesAdapter;
import com.dream.countryinfo.network.CountryApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    private SearchView searchView;
    private ListView listView;
    private ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progressBar);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // do instant search when text changed
                CountryApiHelper.getSingleton().cancelSearchCountryCalls();

                if (TextUtils.isEmpty(s)) {
                    countryNamesSearched.clear();
                    countryNamesAdapter.setCountryNames(countryNamesSearched);
                    countryNamesAdapter.notifyDataSetChanged();
                    return false;
                }

                progressBar.setVisibility(View.VISIBLE);

                CountryApiHelper.getSingleton().searchCountriesByName(s, "name",
                        new CountryApiHelper.MyCallback<List<Map<String, String>>>() {
                            @Override
                            public void onResponse(List<Map<String, String>> responseData) {
                                countryNamesSearched.clear();

                                if (responseData != null) {
                                    for (Map<String, String> item : responseData) {
                                        countryNamesSearched.add(item.get("name"));
                                    }
                                }

                                countryNamesAdapter.setCountryNames(countryNamesSearched);
                                countryNamesAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Failed to retrieve country names. " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
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
