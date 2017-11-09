package com.dream.countryinfo.feature.country.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dream.countryinfo.CountryApp;
import com.dream.countryinfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixingming on 10/11/2017.
 */

public class CountryNamesAdapter extends BaseAdapter {

    private List<String> countryNames = new ArrayList<>();

    public void setCountryNames(List<String> names) {
        countryNames.clear();
        countryNames.addAll(names);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return countryNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view =  LayoutInflater.from(CountryApp.getApplication().getApplicationContext()).inflate(R.layout
                    .item_country_name, null);
        }

        TextView tvCountryName = view.findViewById(R.id.tv_country_name);

        tvCountryName.setText(getItem(i).toString());

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
