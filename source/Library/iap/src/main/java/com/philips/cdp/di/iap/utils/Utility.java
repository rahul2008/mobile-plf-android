/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;
import com.philips.cdp.di.iap.response.orders.Address;
import com.philips.cdp.di.iap.response.payment.BillingAddress;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static final String TAG = Utility.class.getName();

    private static ProgressDialog mProgressDialog = null;

    public static AIAppTaggingInterface mAIAppTaggingInterface;
    public static AppInfra mAppInfra;

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
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getPostalCode());
            appendAddressWithNewLineIfNotNull(sb, ((DeliveryAddressEntity) addressObj).getTown());
            Country countryEntity = ((DeliveryAddressEntity) addressObj).getCountry();
            String country = getCountryName(countryEntity.getIsocode());
            if (country != null) {
                sb.append(country);
            }

        } else if (addressObj instanceof AddressFields) {
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getPostalCode());
            appendAddressWithNewLineIfNotNull(sb, ((AddressFields) addressObj).getTown());
            String country = getCountryName(((AddressFields) addressObj).getCountryIsocode());
            if (country != null) {
                sb.append(country);
            }
        } else if (addressObj instanceof Addresses) {
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getPostalCode());
            appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getTown());
            if ((((Addresses) addressObj).getRegion()) != null && (((Addresses) addressObj).getRegion().getName()) != null) {
                appendAddressWithNewLineIfNotNull(sb, ((Addresses) addressObj).getRegion().getName());
            }
            sb.append(((Addresses) addressObj).getCountry().getName());
        } else if (addressObj instanceof BillingAddress) {
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getPostalCode());
            appendAddressWithNewLineIfNotNull(sb, ((BillingAddress) addressObj).getTown());
            String country = getCountryName(((BillingAddress) addressObj).getCountry().getIsocode());
            if (country != null) {
                sb.append(country);
            }
        } else if (addressObj instanceof Address) {
            appendAddressWithNewLineIfNotNull(sb, ((Address) addressObj).getLine1());
            appendAddressWithNewLineIfNotNull(sb, ((Address) addressObj).getLine2());
            appendAddressWithNewLineIfNotNull(sb, ((Address) addressObj).getPostalCode());
            appendAddressWithNewLineIfNotNull(sb, ((Address) addressObj).getTown());
            if ((((Address) addressObj).getRegion()) != null && (((Address) addressObj).getRegion().getName()) != null) {
                appendAddressWithNewLineIfNotNull(sb, ((Address) addressObj).getRegion().getName());
            }
            appendAddressWithNewLineIfNotNull(sb, (((Address) addressObj).getCountry().getName()));
        }
        return sb.toString();
    }

    public static String formatAddress(final String address) {
        if (address != null)
            return address.replaceAll(", ", "\n");
        else
            return null;
    }

    public static int getThemeColor(Context context) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        int mThemeBaseColor = a.getColor(0, ContextCompat.getColor(context, R.color.uikit_philips_blue));
        a.recycle();
        return mThemeBaseColor;
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
}
