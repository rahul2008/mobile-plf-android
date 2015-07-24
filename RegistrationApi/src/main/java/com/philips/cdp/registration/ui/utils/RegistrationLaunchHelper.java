package com.philips.cdp.registration.ui.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

public class RegistrationLaunchHelper {
    public static void launchRegistrationActivity(Context context) {
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        context.startActivity(registrationIntent);
    }

    public static void launchRegistrationActivityWithFixedOrientation(Context context, int orientation) {
        if(RegistrationHelper.getInstance().getAnalyticsAppId()==null){
            throw new RuntimeException("Please set appid for tagging before you invoke registration");
        }
        Intent registrationIntent = new Intent(context, RegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(RegConstants.ORIENTAION, orientation);
        registrationIntent.putExtras(bundle);
        context.startActivity(registrationIntent);
    }
}
