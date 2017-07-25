/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;

import java.util.Random;

@Deprecated
public class CloudControllerProvider {

    private static String sTemporaryAppId = generateTemporaryAppId();
    private static CloudController sCloudControllerInstance;

    public static void initialize(final @NonNull CloudController cloudController) {
        sCloudControllerInstance = cloudController;
    }

    public static CloudController getCloudController() {
        return sCloudControllerInstance;
    }

    private static String generateTemporaryAppId() {
        return String.format("deadbeef%08x", new Random().nextInt());
    }

    private static boolean isCppAppIdAvailable() {
        return sCloudControllerInstance != null && sCloudControllerInstance.getAppCppId() != null;
    }

    public static String getAppId() {
        return isCppAppIdAvailable() ? sCloudControllerInstance.getAppCppId() : sTemporaryAppId;
    }
}
