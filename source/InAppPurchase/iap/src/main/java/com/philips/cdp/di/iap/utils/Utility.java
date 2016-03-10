package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.carts.CountryEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;

import java.util.Locale;

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


    protected static void appendAddressWithNewLineIfNotNull(StringBuilder sb, String code) {
        if (!TextUtils.isEmpty(code)) {
            sb.append(code).append(IAPConstant.NEW_LINE_ESCAPE_CHARACTER);
        }
    }

    protected static String getCountryName(String isoCode) {
        return new Locale(Locale.getDefault().toString(), isoCode).getDisplayCountry();
    }

    public static String createAddress(final Object addressObj) {
        StringBuilder sb = new StringBuilder();
        if (addressObj instanceof DeliveryAddressEntity) {
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getTown());
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getPostalCode());
            CountryEntity countryEntity = ((DeliveryAddressEntity) addressObj).getCountry();
            String country = getCountryName(countryEntity.getIsocode());
            if (country != null) {
                sb.append(country);
            }

        } else if (addressObj instanceof AddressFields) {
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getTown());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getPostalCode());
            String country = getCountryName(((AddressFields) addressObj).getCountryIsocode());
            if (country != null) {
                sb.append(country);
            }
        } else if (addressObj instanceof Addresses) {
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getTown());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getPostalCode());
            String country = getCountryName(((Addresses) addressObj).getCountry().getIsocode());
            if (country != null) {
                sb.append(country);
            }
        }
        return sb.toString();
    }

}
