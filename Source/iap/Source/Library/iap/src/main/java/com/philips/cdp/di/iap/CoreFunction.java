package com.philips.cdp.di.iap;

import android.content.Context;

public class CoreFunction {

    public String getContent(Context context) {
        return context.getString(R.string.iap_app_name);
    }
}
