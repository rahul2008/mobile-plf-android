package com.philips.cdp.di.iapdemo;

import android.app.Application;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

/**
 * Created by 310164421 on 3/11/2016.
 */
public class DemoApplication extends Application {
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        IAPLog.d(TAG, "DemoApplication: onCreate");
        IAPLog.d(TAG, "DemoApplication: Janrain initialization with locale : " + Locale.getDefault());
        initializeUserRegistration();
    }

    private void initializeUserRegistration() {
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext(),
                Locale.getDefault());
        Tagging.init(RegistrationHelper.getInstance().getLocale(), getApplicationContext(), "Philips Registartion Sample demo");
    }
}
