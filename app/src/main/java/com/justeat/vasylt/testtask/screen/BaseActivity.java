package com.justeat.vasylt.testtask.screen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.justeat.vasylt.testtask.R;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    protected void showLoading(String message) {
        hideLoading();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.create();
        progressDialog.show();
    }

    protected void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Log.e("swatch", message);
        } else {
            Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected boolean hasPermission(String permission, int requestCode, int rationaleRes) {
        boolean hasPermission = hasPermission(permission);
        if (!hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    new AlertDialog.Builder(this)
                            .setMessage(rationaleRes)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, (dialog, which) ->
                                    requestPermission(permission, requestCode))
                            .setNegativeButton(android.R.string.cancel, (dialog, which) ->
                                    finish())
                            .show();
                } else {
                    requestPermission(permission, requestCode);
                }
            }
        }
        return hasPermission;
    }

    private boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }
}
