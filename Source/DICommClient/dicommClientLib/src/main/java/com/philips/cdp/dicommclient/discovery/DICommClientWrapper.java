/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.BuildConfig;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;

import java.util.Random;

public final class DICommClientWrapper {

    private static Context sContext;
    private static String sTemporaryAppId;
    private static CloudController sCloudControllerInstance;

    private DICommClientWrapper() {
        // Utility class
    }

    public static synchronized <U extends DICommAppliance> void initializeDICommLibrary(@NonNull Context context, @NonNull DICommApplianceFactory<U> applianceFactory, @Nullable DICommApplianceDatabase<U> applianceDatabase, @NonNull CloudController cloudController) {
        if (context == null) throw new IllegalArgumentException("Context is null");
        if (applianceFactory == null)
            throw new IllegalArgumentException("ApplicanceFactory is null");
        if (cloudController == null) throw new IllegalArgumentException("CloudController is null.");

        sContext = context;
        sTemporaryAppId = generateTemporaryAppId();
        sCloudControllerInstance = cloudController;

        if (applianceDatabase == null) {
            DiscoveryManager.createSharedInstance(sContext, cloudController, applianceFactory);
        } else {
            DiscoveryManager.createSharedInstance(sContext, cloudController, applianceFactory, applianceDatabase);
        }
    }

    public static synchronized Context getContext() {
        return sContext;
    }

    private static String generateTemporaryAppId() {
        return String.format("deadbeef%08x", new Random().nextInt());
    }

    public static String getDICommClientLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getAppId() {
        return isCppAppIdAvailable() ? getCloudController().getAppCppId() : sTemporaryAppId;
    }

    public static boolean isCppAppIdAvailable() {
        return getCloudController() != null && getCloudController().getAppCppId() != null;
    }

    public static CloudController getCloudController() {
        return sCloudControllerInstance;
    }
}
