package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.carts.CountryEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;
import com.philips.cdp.di.iap.response.payment.BillingAddress;

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
        } else if (addressObj instanceof BillingAddress) {
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getTown());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getPostalCode());
            String country = getCountryName(((BillingAddress) addressObj).getCountry().getIsocode());
            if (country != null) {
                sb.append(country);
            }
        }
        return sb.toString();
    }

    public static int getThemeColor(Context context){
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        int mThemeBaseColor = a.getColor(0, ContextCompat.getColor(context, R.color.uikit_philips_blue));
        a.recycle();
        return mThemeBaseColor;
    }

}
