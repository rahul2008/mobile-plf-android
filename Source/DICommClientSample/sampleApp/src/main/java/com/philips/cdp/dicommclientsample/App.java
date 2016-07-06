package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CppController cppController = CppController.getInstance();
        if (cppController == null) {
            cppController = CppController.createSharedInstance(this, new SampleKpsConfigurationInfo());
        }

        cppController.setDefaultDcsState();

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, new SampleApplianceFactory(), null, cppController);
        }
    }
}
