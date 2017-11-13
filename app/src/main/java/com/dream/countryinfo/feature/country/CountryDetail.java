package com.dream.countryinfo.feature.country;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingming on 10/11/2017.
 */

public class CountryDetail {
    private String name;
    private String capital;
    private String region;
    private String nativeName;
    private List<Map<String, String>> languages;
    private String flag;
    private Map<String, String> translations;
    private String area;
    private List<Double> latlng;

    public double getLatitude() {
        return latlng.size() > 0 ? latlng.get(0) : 0;
    }

    public double getLongitude() {
        return latlng.size() > 1 ? latlng.get(1) : 0;
    }

    public String getLanguageNames() {
        StringBuilder sb = new StringBuilder();
        for(Map<String, String> map : languages) {
            sb.append(map.get("name"));
            sb.append(", ");
        }

        return sb.substring(0, sb.length() - 3);
    }

    public String getTranslationOfGerman() {
        String result = translations.get("de");
        if (TextUtils.isEmpty(result))
            result = "N/A";
        else
            result += "(de)";

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public void setLanguages(List<Map<String, String>> languages) {
        this.languages = languages;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLatlng(List<Double> latlng) {
        this.latlng = latlng;
    }

    public String getCapital() {
        return capital;
    }

    public String getRegion() {
        return region;
    }

    public String getNativeName() {
        return nativeName;
    }

    public List<Map<String, String>> getLanguages() {
        return languages;
    }

    public String getFlag() {
        return flag;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public String getArea() {
        return area;
    }

    public List<Double> getLatlng() {
        return latlng;
    }
}
