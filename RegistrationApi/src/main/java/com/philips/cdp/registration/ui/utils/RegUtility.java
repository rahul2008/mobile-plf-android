package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by 310190722 on 8/6/2015.
 */
public class RegUtility {


    private static final String FILE_NAME = "FILE_NAME";
    private static final String TRADITIONAL_PASSWORD_ID = "TRADITIONAL_PASSWORD_ID";

    public static int getCheckBoxPadding(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int padding;
        if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1){
            padding = (int) (35*scale + 0.5f);
        }else {
            padding = (int) (10*scale + 0.5f);
        }
        return padding;
    }

    public static void setTraditionalPassword(Context context, String accessToken) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TRADITIONAL_PASSWORD_ID, accessToken);
        editor.commit();
    }
    public static String getTraditionalPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(TRADITIONAL_PASSWORD_ID,"0");
    }
}
