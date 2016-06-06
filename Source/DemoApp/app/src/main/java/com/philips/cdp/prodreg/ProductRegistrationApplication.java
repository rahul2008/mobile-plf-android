package com.philips.cdp.prodreg;

import android.app.Application;

import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.util.ProdRegConfigManager;

public class ProductRegistrationApplication extends Application {

    private String TAG = getClass().toString();

    @Override
    public void onCreate() {
        super.onCreate();
        new ProdRegHelper().init(getApplicationContext());
        ProdRegConfigManager.getInstance().initializeProductRegistration(getApplicationContext());
    }
}
