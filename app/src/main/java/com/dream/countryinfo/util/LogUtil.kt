package com.dream.countryinfo.util

import android.util.Log

import com.dream.countryinfo.BuildConfig

/**
 * Created by lixingming on 12/11/2017.
 *
 * This class provide static method ALMOST the same as android.util.Log, except that it will only output log when
 * BUILD_TYPE is debug
 *
 */

object LogUtil {

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }
}
