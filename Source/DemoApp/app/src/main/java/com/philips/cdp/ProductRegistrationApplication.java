package com.philips.cdp;

import android.app.Application;

import com.philips.cdp.demo.R;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RLog.d("ProductReg", "ProductRegistrationApplication : onCreate");
        RLog.d("ProductReg", "ProductRegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext(),
                Locale.getDefault());
        Tagging.init(RegistrationHelper.getInstance().getLocale(), getApplicationContext(), getResources().getString(R.string.app_name));
    }
}
