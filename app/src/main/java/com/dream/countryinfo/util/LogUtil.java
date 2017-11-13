package com.dream.countryinfo.util;

import android.util.Log;

import com.dream.countryinfo.BuildConfig;

/**
 * Created by lixingming on 12/11/2017.
 *
 * This class provide static method ALMOST the same as android.util.Log, except that it will only output log when
 * BUILD_TYPE is debug
 *
 */

public class LogUtil {
    public static void d(String tag, String msg) {
        if (isDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug()) {
            Log.e(tag, msg);
        }
    }

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
