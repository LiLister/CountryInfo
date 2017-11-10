package com.dream.countryinfo.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

/**
 * Created by lixingming on 10/11/2017.
 */

public class BaseActivity extends AppCompatActivity {


    private ProgressDialog pDialog = null;

    public boolean isActivityValid() {
        if (this.isFinishing()) {
            return false;
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) && activityIsDestroyed()) {
            return false;
        }

        return true;
    }

    @TargetApi(17)
    private boolean activityIsDestroyed() {
        return this.isDestroyed();
    }

    protected void hideLoading() {
        if (!isActivityValid()) return;

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void showLoading() {
        showLoading("Loading...");
    }

    protected void showLoading(String msg) {
        if (!isActivityValid()) return;

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.setMessage(msg);

            return;
        }
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
        }

        pDialog.setMessage(msg);

        pDialog.show();
    }


}
