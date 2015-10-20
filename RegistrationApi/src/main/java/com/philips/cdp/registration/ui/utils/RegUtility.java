package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import com.philips.cdp.registration.R;

/**
 * Created by 310190722 on 8/6/2015.
 */
public class RegUtility {


    private static final String FILE_NAME = "FILE_NAME";
    private static final String TRADITIONAL_PASSWORD_ID = "TRADITIONAL_PASSWORD_ID";

    public static int getCheckBoxPadding(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int padding;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            padding = (int) (35 * scale + 0.5f);
        } else {
            padding = (int) (10 * scale + 0.5f);
        }
        return padding;
    }

    public static void invalidalertvisibilitygone(View view){
        android.widget.RelativeLayout.LayoutParams userNameParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
        userNameParam.addRule(RelativeLayout.LEFT_OF, R.id.rl_reg_parent_verified_field);
        view.setLayoutParams(userNameParam);
    }

    public static void invalidalertvisibilityview(View view){
        android.widget.RelativeLayout.LayoutParams userNameParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
        userNameParam.addRule(RelativeLayout.LEFT_OF, R.id.fl_reg_invalid_alert);
        view.setLayoutParams(userNameParam);
    }
}
