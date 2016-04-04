package com.philips.cdp.prodreg;

import android.app.Application;
import android.util.Log;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.prodreg.dicom.AirPurifierFactory;
import com.philips.cdp.prodreg.dicom.SampleKpsConfigurationInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

public class ProductRegistrationApplication extends Application {

    private String TAG = getClass().toString();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ProductRegistrationApplication : onCreate");
        Log.d(TAG, "ProductRegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier(getResources().getString(R.string.integratingApplicationAppsId));
        Tagging.setLaunchingPageName(getResources().getString(R.string.demo_app_home));
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext(), Locale.getDefault());
        Tagging.init(RegistrationHelper.getInstance().getLocale(), getApplicationContext(), getResources().getString(R.string.app_name));

        configureDiCom();
    }

    private void configureDiCom() {
        CppController cppController = CppController.getInstance();
        if (cppController == null) {
            cppController = CppController.createSharedInstance(this, new SampleKpsConfigurationInfo());
        }

        cppController.setDefaultDcsState();

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, new AirPurifierFactory(), null, cppController);
        }
    }
}
