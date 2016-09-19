/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.Addresses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static final String TAG = Utility.class.getName();
    private static ProgressDialog mProgressDialog = null;

    public static void showProgressDialog(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message + "...");

        if ((!mProgressDialog.isShowing()) && !((Activity) context).isFinishing()) {
            mProgressDialog.show();
        }
    }

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

    public static void changeProgressMessage(String message) {
        mProgressDialog.setMessage(message);
    }

    public static void hideKeypad(Context pContext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                pContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != ((Activity) pContext).getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) pContext).getCurrentFocus().getWindowToken(),
                    0);
        }
    }

    public static void showKeypad(Context pContext, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    public static String formatAddress(final String address) {
        if (address != null)
            return address.replaceAll(", ", "\n");
        else
            return null;
    }

    public static String getFormattedDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            IAPLog.d(Utility.TAG, e.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy"); // Set your date format
        return sdf.format(convertedDate);
    }

    public static int getThemeColor(Context context) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        int mThemeBaseColor = a.getColor(0, ContextCompat.getColor(context, R.color.uikit_philips_blue));
        a.recycle();
        return mThemeBaseColor;
    }

    public static void addCountryInPreference(Context pContext, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getCountryFromPreferenceForKey(Context pContext, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
        String value = sharedPreferences.getString(key, null);
        return value;
    }

    protected static void appendAddressWithNewLineIfNotNull(StringBuilder sb, String code) {
        if (!TextUtils.isEmpty(code)) {
            sb.append(code).append(IAPConstant.NEW_LINE_ESCAPE_CHARACTER);
        }
    }

    public static String getAddressToDisplay(final AddressFields address) {
        StringBuilder sb = new StringBuilder();
        appendAddressWithNewLineIfNotNull(sb, address.getLine1());
        appendAddressWithNewLineIfNotNull(sb, address.getLine2());
        appendAddressWithNewLineIfNotNull(sb, address.getRegionName());
        appendAddressWithNewLineIfNotNull(sb, address.getTown());
        appendAddressWithNewLineIfNotNull(sb, address.getPostalCode());
        return sb.toString();
    }

    private static boolean isNotNullNorEmpty(String field) {
        return !TextUtils.isEmpty(field);
    }

    public static AddressFields prepareAddressFields(Addresses addresses, String janRainEmail) {
        AddressFields fields = new AddressFields();

        if (isNotNullNorEmpty(addresses.getFirstName())) {
            fields.setFirstName(addresses.getFirstName());
        }

        if (isNotNullNorEmpty(addresses.getLastName())) {
            fields.setLastName(addresses.getLastName());
        }

        if (isNotNullNorEmpty(addresses.getTitleCode())) {
            String titleCode = addresses.getTitleCode();
            if (titleCode.trim().length() > 0)
                fields.setTitleCode(titleCode.substring(0, 1).toUpperCase(Locale.getDefault())
                        + titleCode.substring(1));
        }

        if (isNotNullNorEmpty(addresses.getLine1())) {
            fields.setLine1(addresses.getLine1());
        }

        if (isNotNullNorEmpty(addresses.getLine2())) {
            fields.setLine2(addresses.getLine2());
        }

        if (isNotNullNorEmpty(addresses.getTown())) {
            fields.setTown(addresses.getTown());
        }

        if (isNotNullNorEmpty(addresses.getPostalCode())) {
            fields.setPostalCode(addresses.getPostalCode());
        }

        if (isNotNullNorEmpty(addresses.getCountry().getIsocode())) {
            fields.setCountryIsocode(addresses.getCountry().getIsocode());
        }

        if (isNotNullNorEmpty(addresses.getEmail())) {
            fields.setEmail(addresses.getEmail());
        } else {
            fields.setEmail(janRainEmail); // Since there is no email response from hybris
        }

        if (isNotNullNorEmpty(addresses.getPhone1())) {
            fields.setPhoneNumber(addresses.getPhone1());
        }

        if (addresses.getRegion() != null) {
            fields.setRegionName(addresses.getRegion().getName());
            CartModelContainer.getInstance().setRegionIsoCode(addresses.getRegion().getIsocode());
        }
        return fields;
    }

}
