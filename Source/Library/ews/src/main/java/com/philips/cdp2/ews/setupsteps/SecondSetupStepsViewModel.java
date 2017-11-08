/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;

public class SecondSetupStepsViewModel extends ConnectPhoneToDeviceAPModeViewModel {


    @Inject
    public SecondSetupStepsViewModel(@NonNull final Navigator navigator,
                                     @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                     @NonNull final PermissionHandler permissionHandler,
                                     @NonNull final DialogFragment connectingDialog,
                                     @NonNull final DialogFragment unsuccesfulDialog,
                                     @NonNull final DialogFragment gpsSettingsDialog, @NonNull final Handler handler) {
        super(navigator, eventBus, permissionHandler, connectingDialog, unsuccesfulDialog, gpsSettingsDialog, handler);
    }

    public void onNextButtonClicked() {
        tapWifiBlinking();
        connectPhoneToDeviceHotspotWifi();
    }

    public void onNoButtonClicked() {
        tapWifiNotBlinking();
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

    private void tapWifiNotBlinking() {
        EWSTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.WIFI_NOT_BLINKING);
    }

    private void tapWifiBlinking() {
        EWSTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.WIFI_BLINKING);
    }

    @Override
    protected void startConnection() {
        eventBus.unregister(this);
        navigator.navigateToConnectingPhoneToHotspotWifiScreen();
    }

    @Override
    public void showPasswordEntryScreenEvent(ShowPasswordEntryScreenEvent entryScreenEvent) {
        // TODO .. for now do nothing!
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.SETUP_STEP2);
    }
}

