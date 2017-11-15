/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.philips.cdp.di.iap.utils.NetworkUtility.ALERT_DIALOG_TAG;

public class Utility {
    public static final String TAG = Utility.class.getName();
    private static AlertDialogFragment alertDialogFragment;

    public static void hideKeypad(Activity pContext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                pContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != (pContext).getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(pContext.getCurrentFocus().getWindowToken(),
                    0);
        }
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

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMM dd, yyyy"); // Set your date format
        return sdf.format(convertedDate);
    }

//    public static int getThemeColor(Context context) {
//        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
//        int mThemeBaseColor = a.getColor(0, ContextCompat.getColor(context, R.color.uikit_philips_blue));
//        a.recycle();
//        return mThemeBaseColor;
//    }

    public static void addCountryInPreference(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
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
        appendAddressWithNewLineIfNotNull(sb, address.getTown());
        appendAddressWithNewLineIfNotNull(sb, address.getRegionName());
        appendAddressWithNewLineIfNotNull(sb, address.getPostalCode());
        return sb.toString();
    }

    static boolean isNotNullNorEmpty(String field) {
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
            fields.setPhone1(addresses.getPhone1());
        }

        if (addresses.getRegion() != null) {
            fields.setRegionName(addresses.getRegion().getIsocodeShort());
            CartModelContainer.getInstance().setRegionIsoCode(addresses.getRegion().getIsocodeShort());
        }
        return fields;
    }

    public static void showActionDialog(final Context context, String positiveBtnText, String negativeBtnText,
                                        String pErrorString, String descriptionText, final FragmentManager pFragmentManager, final AlertListener alertListener) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context);
        if (!TextUtils.isEmpty(descriptionText)) {
            builder.setMessage(descriptionText);
        }

        if (!TextUtils.isEmpty(pErrorString)) {
            builder.setTitle(pErrorString);
        }
        builder.setPositiveButton(positiveBtnText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertListener.onPositiveBtnClick();
                        dismissAlertFragmentDialog(alertDialogFragment, pFragmentManager);
                    }
                }
        );
        builder.setNegativeButton(negativeBtnText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertListener.onNegativeBtnClick();
                dismissAlertFragmentDialog(alertDialogFragment, pFragmentManager);
            }
        });
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(pFragmentManager, ALERT_DIALOG_TAG);
    }

    static void dismissAlertFragmentDialog(AlertDialogFragment alertDialogFragment, FragmentManager fragmentManager) {
        if (alertDialogFragment != null) {
            alertDialogFragment.dismiss();
        } else {
            alertDialogFragment = (AlertDialogFragment) fragmentManager.findFragmentByTag(ALERT_DIALOG_TAG);
            alertDialogFragment.dismiss();
        }
    }

    /**
     * Utility function for find the substring in a string.
     *
     * @param ignoreCase pass for case sensitive comparison
     * @param str        Primary charsequence in which substring need to searched.
     * @param subString  substring to be searched.
     * @return 0 if main string contains substring else -1
     */
    public static int indexOfSubString(boolean ignoreCase, final CharSequence str, final CharSequence subString) {
        if (str == null || subString == null) {
            return -1;
        }
        final int subStringLen = subString.length();
        final int max = str.length() - subStringLen;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(ignoreCase, str, i, subString, 0, subStringLen)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper function to find matching region for two Charsequence.
     *
     * @param ignoreCase   pass true for case insensitive comparison
     * @param str          primary string for searching
     * @param strOffset    offset in primary string
     * @param subStr       sub string
     * @param subStrOffset offset in sub string
     * @param length       lenght of the lookup
     * @return the comparsion result if the string contains substring of given legth
     */
    public static boolean regionMatches(final boolean ignoreCase, final CharSequence str, final int strOffset,
                                        final CharSequence subStr, final int subStrOffset, final int length) {
        if (str == null || subStr == null) {
            return false;
        }

        if (str instanceof String && subStr instanceof String) {
            return ((String) str).regionMatches(ignoreCase, strOffset, (String) subStr, subStrOffset, length);
        }

        //SubString length is more than string
        if (subStr.length() > str.length()) {
            return false;
        }

        //Invalid start point
        if (strOffset < 0 || subStrOffset < 0 || length < 0) {
            return false;
        }

        //Length can't be greater than diff of string length and offset
        if ((str.length() - strOffset) < length
                || (subStr.length() - subStrOffset) < length) {
            return false;
        }

        //Start comparing
        int strIndex = strOffset;
        int subStrIndex = subStrOffset;
        int tmpLenth = length;

        while (tmpLenth-- > 0) {
            char c1 = str.charAt(strIndex++);
            char c2 = subStr.charAt(subStrIndex++);

            if (c1 == c2) {
                continue;
            }

            //Same comparison as java framework
            if (ignoreCase &&
                    (Character.toUpperCase(c1) == Character.toUpperCase(c2)
                            || Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
                continue;
            }
            return false;
        }
        return true;
    }

//    public static InputValidator new InputValidator(Pattern valid_regex_pattern) {
//        return new InputValidator(valid_regex_pattern);
//    }

    public static Drawable getImageArrow(Context mContext) {
        int width = (int) mContext.getResources().getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) mContext.getResources().getDimension(R.dimen.iap_count_drop_down_icon_height);
        Drawable imageArrow = VectorDrawableCompat.create(mContext.getResources(), R.drawable.iap_product_count_drop_down, mContext.getTheme());
        imageArrow.setBounds(0, 0, width, height);
        return imageArrow;
    }

}
