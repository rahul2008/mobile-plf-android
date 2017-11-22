/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.Manifest;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.util.StringProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class SecondSetupStepsViewModel {

    public interface LocationPermissionFlowCallback {
        void showGPSEnableDialog(@NonNull BaseContentConfiguration baseContentConfiguration);

        void showLocationPermissionDialog(@NonNull BaseContentConfiguration baseContentConfiguration);
    }

    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    @NonNull
    protected final Navigator navigator;
    @NonNull
    protected final EventBus eventBus;
    @NonNull
    private final PermissionHandler permissionHandler;
    @NonNull
    private final BaseContentConfiguration baseContentConfiguration;

    private Fragment fragment;

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

    @Nullable
    private LocationPermissionFlowCallback locationPermissionFlowCallback;

    @Inject
    public SecondSetupStepsViewModel(@NonNull final Navigator navigator,
                                     @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                     @NonNull final PermissionHandler permissionHandler,
                                     @NonNull final StringProvider stringProvider,
                                     @NonNull final HappyFlowContentConfiguration happyFlowContentConfiguration,
                                     @NonNull final BaseContentConfiguration baseContentConfiguration) {
        this.stringProvider = stringProvider;
        this.question = new ObservableField<>(getQuestion(happyFlowContentConfiguration));
        this.title = new ObservableField<>(getTitle(happyFlowContentConfiguration));
        this.image = getImage(happyFlowContentConfiguration);
        this.yesButton = new ObservableField<>(getYesButton(happyFlowContentConfiguration));
        this.noButton = new ObservableField<>(getNoButton(happyFlowContentConfiguration));
        this.navigator = navigator;
        this.permissionHandler = permissionHandler;
        this.eventBus = eventBus;
        this.baseContentConfiguration = baseContentConfiguration;
        eventBus.register(this);
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

    protected void startConnection() {
        eventBus.unregister(this);
        navigator.navigateToConnectingPhoneToHotspotWifiScreen();
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.SETUP_STEP2);
    }

    public void connectPhoneToDeviceHotspotWifi() {
        if (permissionHandler.hasPermission(fragment.getContext(), ACCESS_COARSE_LOCATION)) {
            connect();
        } else {
            if (locationPermissionFlowCallback != null) {
                locationPermissionFlowCallback.showLocationPermissionDialog(baseContentConfiguration);
            }
        }
    }

    private void connect() {
        EWSLogger.d(EWS_STEPS, "Step 1 : Trying to connect to appliance hot spot");
        if (GpsUtil.isGPSRequiredForWifiScan() && !GpsUtil.isGPSEnabled(fragment.getContext())) {
            if (locationPermissionFlowCallback != null) {
                locationPermissionFlowCallback.showGPSEnableDialog(baseContentConfiguration);
            }
        } else {
            startConnection();
        }
    }

    public void setFragment(@NonNull final Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean areAllPermissionsGranted(final int[] grantResults) {
        return permissionHandler.areAllPermissionsGranted(grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPasswordEntryScreenEvent(@SuppressWarnings("UnusedParameters") ShowPasswordEntryScreenEvent entryScreenEvent) {
        eventBus.unregister(this);
        navigator.navigateToConnectToDeviceWithPasswordScreen("");
    }

    public void setLocationPermissionFlowCallback(@Nullable LocationPermissionFlowCallback locationPermissionFlowCallback) {
        this.locationPermissionFlowCallback = locationPermissionFlowCallback;
    }
}

