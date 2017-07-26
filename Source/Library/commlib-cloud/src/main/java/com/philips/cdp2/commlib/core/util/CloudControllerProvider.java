/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;

@Deprecated
public class CloudControllerProvider {
    private static CloudController cloudController;

    public static void setCloudController(final @NonNull CloudController cloudController) {
        CloudControllerProvider.cloudController = cloudController;
        generateAppId();
    }

    public static CloudController getCloudController() {
        generateAppId();
        return cloudController;
    }

    private static void generateAppId() {
        if (cloudController != null) {
            String appCppId = cloudController.getAppCppId();

            if (appCppId != null) {
                AppIdProvider.setAppId(appCppId);
            }
        }
    }
}
