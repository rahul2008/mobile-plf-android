package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by 310190722 on 8/6/2015.
 */
public class RegUtility {

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
}
