/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.digitalcare.CcSettings;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.appliance.EWSGenericAppliance;
import com.philips.cdp2.ews.communication.ApplianceAccessEventMonitor;
import com.philips.cdp2.ews.communication.EventingChannel;
import com.philips.cdp2.ews.communication.WiFiBroadcastReceiver;
import com.philips.cdp2.ews.communication.WiFiEventMonitor;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.view.dialog.ConnectionUnsuccessfulDialog;
import com.philips.cdp2.ews.view.dialog.GPSEnableDialogFragment;
import com.philips.cdp2.ews.viewmodel.BlinkingAccessPointViewModel;
import com.philips.cdp2.ews.viewmodel.EWSPressPlayAndFollowSetupViewModel;
import com.philips.cdp2.ews.viewmodel.EWSWiFiConnectViewModel;
import com.philips.cdp2.ews.viewmodel.ProductSupportViewModel;
import com.philips.cdp2.ews.wifi.WiFiUtil;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.UUID;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class EWSModule {

    private Context context;

    public EWSModule(Context context) {
        this.context = context;
    }

    @Provides
    WifiManager providesWiFiManager() {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Provides
    WiFiBroadcastReceiver providesWiFiBroadcastReceiver(@NonNull final @Named("ews.event.bus") EventBus eventBus,
                                                        @NonNull final WiFiUtil wiFiUtil,
                                                        @NonNull ApplianceSessionDetailsInfo applianceSessionDetailsInfo) {
        return new WiFiBroadcastReceiver(context, eventBus, wiFiUtil, applianceSessionDetailsInfo);
    }

    @Singleton
    @Provides
    @Named("ews.event.bus")
    EventBus providesEWSEventBus() {
        return EventBus.builder().build();
    }

    @SuppressWarnings("unchecked")
    @Singleton
    @Provides
    EventingChannel<EventingChannel.ChannelCallback> providesEWSEventingChannel(@NonNull final WiFiBroadcastReceiver receiver,
                                                                                @NonNull final ApplianceAccessEventMonitor applianceAccessEventMonitor,
                                                                                @NonNull final WiFiEventMonitor wiFiEventMonitor) {
        return new EventingChannel<>(Arrays.asList(receiver, applianceAccessEventMonitor, wiFiEventMonitor));
    }

    @Provides
    @Named("ews.temporary.appliance")
    EWSGenericAppliance provideTemporaryAppliance() {
        String tempEui64 = UUID.randomUUID().toString();

        NetworkNode networkNode = new NetworkNode();
        networkNode.setCppId(tempEui64);
        networkNode.setIpAddress("192.168.1.1");
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        networkNode.setBootId(-1);
        networkNode.setName(null);

        CommunicationStrategy communicationStrategy = new LanCommunicationStrategy(networkNode);
        return new EWSGenericAppliance(networkNode, communicationStrategy);
    }

    @Provides
    DiscoveryManager<? extends Appliance> providesDiscoverManager() {
        return EWSDependencyProvider.getInstance().getDiscoveryManager();
    }

    @Provides
    EWSWiFiConnectViewModel providesWiFiConnectViewModel(@NonNull final WiFiUtil wifiUtil,
                                                         @NonNull final ApplianceSessionDetailsInfo sessionInfo,
                                                         @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                                         @NonNull final ScreenFlowController screenFlowController) {
        final ConnectionEstablishDialogFragment dialogFragment =
                ConnectionEstablishDialogFragment.getInstance(R.string.label_ews_establishing_connection_body);
        return new EWSWiFiConnectViewModel(wifiUtil, sessionInfo, eventBus, screenFlowController,
                dialogFragment, new Handler(context.getMainLooper()));
    }

    @Provides
    EWSPressPlayAndFollowSetupViewModel providesEWSPressPlayAndFollowSetupViewModel(@NonNull final ScreenFlowController screenFlowController,
                                                                                    @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                                                                    @NonNull final PermissionHandler permissionHandler) {
        final ConnectionEstablishDialogFragment dialogFragment =
                ConnectionEstablishDialogFragment.getInstance(R.string.label_ews_establishing_connection_body);
        return new EWSPressPlayAndFollowSetupViewModel(screenFlowController, eventBus, permissionHandler, dialogFragment,
                new ConnectionUnsuccessfulDialog(), new GPSEnableDialogFragment(), new Handler(context.getMainLooper()));
    }

    @Provides
    BlinkingAccessPointViewModel providesBlinkingAccessPointViewModel(@NonNull final ScreenFlowController screenFlowController,
                                                                      @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                                                      @NonNull final PermissionHandler permissionHandler) {
        final ConnectionEstablishDialogFragment dialogFragment =
                ConnectionEstablishDialogFragment.getInstance(R.string.label_ews_establishing_connection_body);
        return new BlinkingAccessPointViewModel(screenFlowController, eventBus, permissionHandler, dialogFragment,
                new ConnectionUnsuccessfulDialog(), new GPSEnableDialogFragment(), new Handler(context.getMainLooper()));
    }

    @Provides
    ProductSupportViewModel productSupportViewModel(@NonNull final ScreenFlowController screenFlowController) {
        final CcLaunchInput ccLaunchInput = new CcLaunchInput();
        final CcInterface ccInterface = new CcInterface();
        final UappDependencies dependencies = new CcDependencies(EWSDependencyProvider.getInstance().getAppInfra());
        final UappSettings settings = new CcSettings(context);

        return new ProductSupportViewModel(ccLaunchInput, ccInterface, dependencies, settings, screenFlowController);
    }
}
