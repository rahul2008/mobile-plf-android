package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

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

    public static boolean isProgressDialogShowing(){
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            return true;
        }else{
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

    /**
     * Shows Network error alert
     *
     * @param context Current context
     */
    public static void showNetworkError(final Context context, final boolean finish) {
        String alertTitle = "Network Error";
        String alertBody = "No network available. Please check your network settings and try again.";
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(alertTitle);
        alert.setMessage(alertBody);
        alert.setPositiveButton(android.R.string.ok,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

//                        if (finish) {
//                            context.finish();
//                        }
                    }
                });
        alert.show();
    }
}
