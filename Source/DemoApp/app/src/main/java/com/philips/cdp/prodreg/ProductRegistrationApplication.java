package com.philips.cdp.prodreg;

import android.app.Application;

import com.philips.cdp.prodreg.register.ProdRegHelper;

public class ProductRegistrationApplication extends Application {

    private String TAG = getClass().toString();

    @Override
    public void onCreate() {
        super.onCreate();
        ProdRegHelper.getInstance().init(getApplicationContext());
    }
}
