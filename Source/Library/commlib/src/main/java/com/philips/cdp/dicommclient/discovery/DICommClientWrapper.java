/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp2.commlib.BuildConfig;
import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.util.Random;

public final class DICommClientWrapper {

    private static String sTemporaryAppId;
    private static CloudController sCloudControllerInstance;

    private DICommClientWrapper() {
        // Utility class
    }

    public static synchronized <U extends Appliance> void initializeDICommLibrary(@NonNull Context context, @NonNull DICommApplianceFactory<U> applianceFactory, @Nullable DICommApplianceDatabase<U> applianceDatabase, @Nullable CloudController cloudController) {
        if (context == null) throw new IllegalArgumentException("Context is null");
        if (applianceFactory == null)
            throw new IllegalArgumentException("ApplicanceFactory is null");

        sTemporaryAppId = generateTemporaryAppId();
        sCloudControllerInstance = cloudController;

        if (applianceDatabase == null) {
            DiscoveryManager.createSharedInstance(context, cloudController, applianceFactory);
        } else {
            DiscoveryManager.createSharedInstance(context, cloudController, applianceFactory, applianceDatabase);
        }
    }

    public static Context getContext() {
        return DiscoveryManager.getInstance() != null ? DiscoveryManager.getInstance().getContext() : null;
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

    @Nullable
    public static CloudController getCloudController() {
        return sCloudControllerInstance;
    }
}
