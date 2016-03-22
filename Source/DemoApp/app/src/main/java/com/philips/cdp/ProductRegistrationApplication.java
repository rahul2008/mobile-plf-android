package com.philips.cdp;

import android.app.Application;

import com.philips.cdp.demo.R;
import com.philips.cdp.dicom.AirPurifierFactory;
import com.philips.cdp.dicom.SampleKpsConfigurationInfo;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
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
