package com.philips.cdp2.commlib.core.util;


import android.support.annotation.NonNull;

import java.util.Random;

public class AppIdProvider {

    private static String appId = generateTemporaryAppId();

    private static String generateTemporaryAppId() {
        return String.format("deadbeef%08x", new Random().nextInt());
    }

    public static void setAppId(@NonNull final String appId) {
        AppIdProvider.appId = appId;
    }

    public static String getAppId() {
        return appId;
    }
}
