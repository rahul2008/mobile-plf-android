/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.content.Context;

import com.philips.cdp.dicommclient.BuildConfig;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.DefaultCppController;

import java.util.Random;

public class DICommClientWrapper {

    private static Context mContext;
    private static String mTemporaryAppId;

    public static synchronized <U extends DICommAppliance> void initializeDICommLibrary(Context context, DICommApplianceFactory<U> applianceFactory, DICommApplianceDatabase<U> applianceDatabase, CppController cppController) {
        mContext = context;
        mTemporaryAppId = generateTemporaryAppId();
        if (mContext == null) {
            throw new RuntimeException("Please call initializeDICommLibrary() before you get discoverymanager");
        }
        if (applianceDatabase != null) {
            DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory, applianceDatabase);
        } else {
            DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory);
        }
    }

    public static synchronized Context getContext() {
        return mContext;
    }

    private static String generateTemporaryAppId() {
        return String.format("deadbeef%08x", new Random().nextInt());
    }

    public static String getDICommClientLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getAppId() {
        return isCppAppIdAvailable() ? DefaultCppController.getInstance().getAppCppId() : mTemporaryAppId;
    }

    public static boolean isCppAppIdAvailable() {
        return DefaultCppController.getInstance() != null && DefaultCppController.getInstance().getAppCppId() != null;
    }
}
