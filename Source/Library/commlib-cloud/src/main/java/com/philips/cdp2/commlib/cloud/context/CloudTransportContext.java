/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.cloud.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class CloudTransportContext implements TransportContext {
    private static CloudController cloudController;

    private final BroadcastReceiver networkChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo == null) {
                isAvailable = false;
            } else {
                boolean hasNetwork = networkInfo.getType() == TYPE_WIFI || networkInfo.getType() == TYPE_MOBILE;
                isAvailable = hasNetwork && networkInfo.isConnected();
            }
        }
    };

    private boolean isAvailable;

    public CloudTransportContext(final @NonNull Context context, @NonNull final CloudController cloudController) {
        setupNetworkChangedReceiver(context);

        CloudTransportContext.cloudController = cloudController;
        updateAppId();
    }

    private void setupNetworkChangedReceiver(@NonNull Context context) {
        IntentFilter filter = createIntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkChangedReceiver, filter);
    }

    @VisibleForTesting
    @NonNull
    IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return null;
    }

    @Override
    public CloudCommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, cloudController);
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    public static CloudController getCloudController() {
        updateAppId();
        return cloudController;
    }

    private static void updateAppId() {
        final String appCppId = cloudController.getAppCppId();

        if (TextUtils.isEmpty(appCppId)) {
            DICommLog.w(DICommLog.CPPCONTROLLER, "Could not obtain an appId from the provided CloudController.");
        } else {
            CommCentral.setAppId(appCppId);
        }
    }
}
