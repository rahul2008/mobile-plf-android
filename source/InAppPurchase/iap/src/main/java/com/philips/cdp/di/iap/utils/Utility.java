package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utility {

    private static ProgressDialog mProgressDialog = null;

    /**
     * Displays the loading progress dialog
     *
     * @param context Current context
     */
    public static void showProgressDialog(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message + "...");

        if ((!mProgressDialog.isShowing()) && !((Activity) context).isFinishing()) {
            mProgressDialog.show();
        }
    }

    /***
     * Dismiss the progress dialog
     */
    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static boolean isProgressDialogShowing() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks for internet connection
     *
     * @return True if internet exists otherwise false
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] netInfo = cm.getAllNetworks();

            for (Network network : netInfo) {
                NetworkInfo networkInfo = cm.getNetworkInfo(network);

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    if (networkInfo.isConnected())
                        return true;

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIMAX) // WIMAX//
                    if (networkInfo.isConnected())
                        return true;

                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    if (networkInfo.isConnected())
                        return true;
            }
        } else {
            @SuppressWarnings("deprecation")
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo networkInfo : netInfo) {

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    if (networkInfo.isConnected())
                        return true;

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIMAX) // WIMAX//
                    if (networkInfo.isConnected())
                        return true;

                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    if (networkInfo.isConnected())
                        return true;
            }
        }
        return false;
    }

    public static void hideKeypad(Context pContext) {
        InputMethodManager inputMethodManager = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != ((Activity) pContext).getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) pContext).getCurrentFocus().getWindowToken(),
                    0);
        }
    }

    public static void showKeypad(Context pContext, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
