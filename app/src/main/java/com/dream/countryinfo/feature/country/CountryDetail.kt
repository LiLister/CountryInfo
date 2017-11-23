package com.dream.countryinfo.feature.country

import android.text.TextUtils

/**
 * Created by lixingming on 10/11/2017.
 */

class CountryDetail {
    var name: String? = null
    var capital: String? = null
    var region: String? = null
    var nativeName: String? = null
    var languages: List<Map<String, String>>? = null
    var flag: String? = null
    var translations: Map<String, String>? = null
    var area: String? = null
    var latlng: List<Double>? = null

    val latitude: Double
        get() = if (latlng!!.size > 0) latlng!![0] else 0.0

    val longitude: Double
        get() = if (latlng!!.size > 1) latlng!![1] else 0.0

    val languageNames: String
        get() {
            val sb = StringBuilder()
            for (map in languages!!) {
                sb.append(map["name"])
                sb.append(", ")
            }

            return sb.substring(0, sb.length - 2)
        }

    val translationOfGerman: String
        get() {
            var result = translations!!["de"]
            return if (TextUtils.isEmpty(result))
                "N/A"
            else
                "(de)" + result
        }
}
