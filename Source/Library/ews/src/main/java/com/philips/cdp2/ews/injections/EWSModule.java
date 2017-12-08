/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.appliance.EWSGenericAppliance;
import com.philips.cdp2.ews.communication.DiscoveryHelper;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.FragmentNavigator;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordViewModel;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import java.util.UUID;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@SuppressWarnings("WeakerAccess")
@Module
public class EWSModule {

    @NonNull
    private final Context context;
    @NonNull
    private final FragmentManager fragmentManager;
    @IdRes
    int parentContainerResourceID;
    @NonNull
    private CommCentral commCentral;

    public EWSModule(@NonNull Context context, @NonNull FragmentManager fragmentManager, @IdRes int parentContainerResourceID, @NonNull CommCentral commCentral) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.parentContainerResourceID = parentContainerResourceID;
        this.commCentral = commCentral;
    }

    @Provides
    WifiManager providesWiFiManager() {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Provides
    @Singleton
    CommCentral provideCommCentral(){
        return commCentral;
    }

    @Provides
    @Named("ews.temporary.appliance")
    EWSGenericAppliance provideTemporaryAppliance() {
        NetworkNode fakeNetworkNode = createFakeNetworkNodeForHotSpot();
        ConnectivityMonitor monitor =
                ConnectivityMonitor.forNetworkTypes(context, ConnectivityManager.TYPE_WIFI);
        LanDeviceCache lanDeviceCache = createLanCache();
        injectFakeNodeIntoDeviceCache(lanDeviceCache, fakeNetworkNode);
        // We are intentionally not creating the strategy from the transport context!
        CommunicationStrategy communicationStrategy = new LanCommunicationStrategy(fakeNetworkNode,
                lanDeviceCache, monitor);
        return new EWSGenericAppliance(fakeNetworkNode, communicationStrategy);
    }

    private void injectFakeNodeIntoDeviceCache(@NonNull LanDeviceCache lanDeviceCache,
                                               @NonNull NetworkNode fakeNetworkNode) {
        lanDeviceCache.addNetworkNode(fakeNetworkNode, new DeviceCache.ExpirationCallback() {
            @Override
            public void onCacheExpired(NetworkNode networkNode) {
                // Do nothing
            }
        }, 300);
        lanDeviceCache.stopTimers();
    }

    private LanDeviceCache createLanCache() {
        return new LanDeviceCache(Executors.newSingleThreadScheduledExecutor());
    }

    private NetworkNode createFakeNetworkNodeForHotSpot() {
        String tempEui64 = UUID.randomUUID().toString();
        NetworkNode networkNode = new NetworkNode();
        networkNode.setCppId(tempEui64);
        networkNode.setIpAddress("192.168.1.1");
        networkNode.setBootId(-1);
        networkNode.setName(null);
        return networkNode;
    }

    @Provides
    DiscoveryHelper providesDiscoverHelper() {
        return new DiscoveryHelper(commCentral);
    }

    @Provides
    ConnectWithPasswordViewModel providesSetDeviceConnectViewModel(@NonNull final WiFiUtil wifiUtil,
                                                                   @NonNull final Navigator navigator,
                                                                   @NonNull BaseContentConfiguration baseContentConfiguration,
                                                                   @NonNull StringProvider stringProvider,
                                                                   @NonNull final EWSTagger ewsTagger) {
        return new ConnectWithPasswordViewModel(wifiUtil, navigator,
                baseContentConfiguration, stringProvider, ewsTagger);
    }

    @Provides
    SecondSetupStepsViewModel provideSecondSetupStepsViewModel(
            @NonNull final Navigator navigator,
            @NonNull final PermissionHandler permissionHandler,
            @NonNull HappyFlowContentConfiguration happyFlowContentConfiguration,
            @NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration baseContentConfiguration,
            @NonNull final EWSTagger ewsTagger,
            @NonNull final EWSLogger ewsLogger) {

        return new SecondSetupStepsViewModel(navigator,
                permissionHandler, stringProvider, happyFlowContentConfiguration, baseContentConfiguration, ewsTagger, ewsLogger);
    }


    @Provides
    Navigator provideNavigator() {
        return new Navigator(new FragmentNavigator(fragmentManager, parentContainerResourceID));
    }

    @Provides
    @Named("mainLooperHandler")
    Handler provideHandlerWithMainLooper() {
        return new Handler(Looper.getMainLooper());
    }
}


