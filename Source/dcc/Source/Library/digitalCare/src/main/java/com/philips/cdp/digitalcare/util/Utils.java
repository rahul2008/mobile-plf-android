/**
 * Utils class contains common utility methods required across framework under
 * different scenario's.
 *
 * @author naveen@philips.com
 *
 * @since Feb 10, 2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 *
 */
package com.philips.cdp.digitalcare.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;

import java.util.Locale;


public class Utils {

	public boolean isSimAvailable(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

            switch (SIM_STATE) {
                case TelephonyManager.SIM_STATE_ABSENT: //SimState = "No Sim Found!";
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = "Network Locked!";
                case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = "PIN Required to access SIM!";
                case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = "Unknown SIM State!";
                    return false;
                default:
                    return true;
            }
    }

    public boolean isTelephonyEnabled(final Context context){

        TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm.getPhoneType()==TelephonyManager.PHONE_TYPE_NONE){
            return false;
        }
        return true;
    }

    public static boolean isCountryChina() {
        if(DigitalCareConfigManager.getInstance().getCountry() != null)
        {
            if(DigitalCareConfigManager.getInstance().getCountry().equals("CN")){
                return true;
            }

        }

        return false;
    }

    public static boolean isTablet(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        return diagonalInches >= 6.5;
    }

    public static Locale getLocaleFromAppInfra(){
        Locale locale = Locale.getDefault();
        String[] locale_strings = DigitalCareConfigManager.getInstance().getAPPInfraInstance().getInternationalization().getBCP47UILocale().split("-");
        if (locale_strings.length == 2) {
            locale = new Locale(locale_strings[0], locale_strings[1]);
        }
        return locale;
    }

    public static boolean isEulaAccepted(Context context) {
        SharedPreferences mSharedpreferences = context.getSharedPreferences(DigitalCareConstants.
                DIGITALCARE_FRAGMENT_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedpreferences.edit();

        boolean mBoolean = mSharedpreferences.getBoolean("acceptEula", false);
        editor.commit();
        return mBoolean;
    }

    public static void setEulaPreference(Context context) {
        SharedPreferences mSharedpreferences = context.getSharedPreferences(DigitalCareConstants.
                DIGITALCARE_FRAGMENT_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putBoolean("acceptEula", true);
        editor.commit();
    }
}
