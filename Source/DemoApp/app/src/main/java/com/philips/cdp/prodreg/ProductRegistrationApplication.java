package com.philips.cdp.prodreg;

import android.app.Application;

import com.philips.cdp.prodreg.register.ProdRegHelper;

public class ProductRegistrationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new ProdRegHelper().init(getApplicationContext());
    }
}
