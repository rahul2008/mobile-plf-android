/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.StringProvider;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;

@SuppressWarnings("WeakerAccess")
public class SecondSetupStepsViewModel extends ConnectPhoneToDeviceAPModeViewModel {

    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final ObservableField<String> question;
    @NonNull
    public final Drawable image;
    @NonNull
    public final ObservableField<String> yesButton;
    @NonNull
    public final ObservableField<String> noButton;
    @NonNull
    private final StringProvider stringProvider;

    @Inject
    public SecondSetupStepsViewModel(@NonNull final Navigator navigator,
                                     @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                     @NonNull final PermissionHandler permissionHandler,
                                     @NonNull final DialogFragment connectingDialog,
                                     @NonNull final DialogFragment unsuccesfulDialog,
                                     @NonNull final DialogFragment gpsSettingsDialog,
                                     @NonNull final Handler handler,
                                     @NonNull final StringProvider stringProvider,
                                     @NonNull final HappyFlowContentConfiguration happyFlowContentConfiguration) {
        super(navigator, eventBus, permissionHandler, connectingDialog, unsuccesfulDialog, gpsSettingsDialog, handler);
        this.stringProvider = stringProvider;
        this.question = new ObservableField<>(getQuestion(happyFlowContentConfiguration));
        this.title = new ObservableField<>(getTitle(happyFlowContentConfiguration));
        this.image = getImage(happyFlowContentConfiguration);
        this.yesButton = new ObservableField<>(getYesButton(happyFlowContentConfiguration));
        this.noButton = new ObservableField<>(getNoButton(happyFlowContentConfiguration));
    }

    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenTitle());
    }

    @VisibleForTesting
    @NonNull
    String getQuestion(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenQuestion());
    }

    @NonNull
    @VisibleForTesting
    Drawable getImage(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getImageResource(happyFlowContentConfiguration.getSetUpVerifyScreenImage());
    }

    @VisibleForTesting
    @NonNull
    String getYesButton(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenYesButton());
    }

    @VisibleForTesting
    @NonNull
    String getNoButton(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpVerifyScreenNoButton());
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

    public void trackPageName() {
        EWSTagger.trackPage(Page.SETUP_STEP2);
    }
}

