/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.DefaultCppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final CppController cppController = new DefaultCppController(this, new SampleKpsConfigurationInfo());

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, new SampleApplianceFactory(), null, cppController);
        }
    }
}
