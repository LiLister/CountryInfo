package com.dream.countryinfo.activity

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by lixingming on 10/11/2017.
 */

open class BaseActivity : AppCompatActivity() {

    private var pDialog: ProgressDialog? = null

    val isActivityValid: Boolean
        get() {
            if (this.isFinishing) {
                return false
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && activityIsDestroyed()) {
                return false
            }

            return true
        }

    fun checkPermission(permission: String): Boolean {
        val checkResult = ContextCompat.checkSelfPermission(this, permission)

        return checkResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    @TargetApi(17)
    private fun activityIsDestroyed(): Boolean {
        return this.isDestroyed
    }

    protected fun hideLoading() {
        if (!isActivityValid) return

        if (pDialog != null && pDialog!!.isShowing) {
            pDialog!!.dismiss()
        }
    }

    fun safeToast(msg: String) {
        if (isActivityValid) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun showLoading() {
        showLoading("Loading...")
    }

    protected fun showLoading(msg: String) {
        if (!isActivityValid) return

        if (pDialog != null && pDialog!!.isShowing) {
            pDialog!!.setMessage(msg)

            return
        }
        if (pDialog == null) {
            pDialog = ProgressDialog(this)
            pDialog!!.isIndeterminate = false
            pDialog!!.setCancelable(false)
            pDialog!!.setCanceledOnTouchOutside(false)
        }

        pDialog!!.setMessage(msg)

        pDialog!!.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
