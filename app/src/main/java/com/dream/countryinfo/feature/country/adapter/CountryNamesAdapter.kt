package com.dream.countryinfo.feature.country.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.dream.countryinfo.CountryApp
import com.dream.countryinfo.R

import java.util.ArrayList

/**
 * Created by lixingming on 10/11/2017.
 */

class CountryNamesAdapter : BaseAdapter() {

    private val countryNames = ArrayList<String>()

    fun setCountryNames(names: List<String>) {
        countryNames.clear()
        countryNames.addAll(names)
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(i: Int): Boolean {
        return true
    }

    override fun getCount(): Int {
        return countryNames.size
    }

    override fun getItem(i: Int): Any {
        return countryNames[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var myView = view
        if (myView == null) {
            myView = LayoutInflater.from(CountryApp.application!!.applicationContext).inflate(R.layout
                    .item_country_name, null)
        }

        val tvCountryName = myView!!.findViewById<TextView>(R.id.tv_country_name)

        tvCountryName.text = getItem(i).toString()

        return myView
    }

    override fun getItemViewType(i: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return false
    }
}
