/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.DefaultCppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.util.DICommLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final CppController cppController = new DefaultCppController(this, new SampleKpsConfigurationInfo());

        String ICPClientVersion = cppController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, new SampleApplianceFactory(), null, cppController);
        }
    }
}
