package com.philips.cdp;

import android.app.Application;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RLog.d("ProductReg", "ProductRegistrationApplication : onCreate");
        RLog.d("ProductReg", "ProductRegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
//        Tagging.enableAppTagging(true);
//        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
//        Tagging.setLaunchingPageName("demoapp:home");
        RegistrationHelper.getInstance().setCoppaFlow(false);
        RegistrationHelper.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);


        initRegistration();


    }

    private void initRegistration() {
        RegistrationHelper.getInstance().setLocale(Locale.getDefault());
        RegistrationHelper.getInstance().intializeRegistrationSettings(this, Locale.getDefault());
//        Tagging.init(Locale.getDefault(), this);
    }

}
