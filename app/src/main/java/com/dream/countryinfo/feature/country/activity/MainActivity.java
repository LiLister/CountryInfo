package com.dream.countryinfo.feature.country.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends BaseActivity {

    private ProgressBar progressBar;
    private CountryNamesAdapter countryNamesAdapter = new CountryNamesAdapter();

    private List<String> countryNamesSearched = new ArrayList<>();

    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    private String currentSearchText;

    final private static long searchDelayMills = 500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.main_activity_title);

        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // do instant search when text changed
                currentSearchText = s;

                startSearch(s);

                return true;
            }
        });

        ListView listView = findViewById(R.id.lv_country_names);
        listView.setAdapter(countryNamesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String countryName = countryNamesSearched.get(i);

                CountryDetailActivity.startMe(MainActivity.this, countryName);
            }
        });
    }

    private void startSearch(String s) {
        schedule(new SearchThread(s), searchDelayMills);
    }

    private ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
        return scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS);
    }

    private class SearchThread extends Thread {
        String newText;

        public SearchThread(String newText){
            this.newText = newText;
        }

        @Override
        public void run() {
            // keep only one thread to load current search text
            if (newText != null && newText.equals(currentSearchText)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(newText);
                    }
                });
            }
        }
    }

    private void doSearch(String s) {
        CountryApiHelper.getSingleton().cancelSearchCountryCalls();

        if (TextUtils.isEmpty(s)) {
            countryNamesSearched.clear();
            countryNamesAdapter.setCountryNames(countryNamesSearched);
            countryNamesAdapter.notifyDataSetChanged();
            return;
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
    }
}
