/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.Manifest;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.events.DeviceConnectionErrorEvent;
import com.philips.cdp2.ews.communication.events.NetworkConnectEvent;
import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceViewModel;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.EWSActivity.DEVICE_CONNECTION_TIMEOUT;
import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class SecondSetupStepsViewModel {

    public interface LocationPermissionFlowInterface {
        void showGPSEnableDialog(@NonNull BaseContentConfiguration baseContentConfiguration);
    }

    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    @NonNull
    protected final Navigator navigator;
    @NonNull
    protected final EventBus eventBus;
    @NonNull
    private final PermissionHandler permissionHandler;
    @NonNull
    private final DialogFragment connectingDialog;
    @NonNull
    private final DialogFragment unsuccessfulDialog;
    @NonNull
    private final BaseContentConfiguration baseContentConfiguration;

    private Fragment fragment;

    Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            showUnsuccessfulDialog();
        }
    };
    @NonNull
    private DialogFragment gpsSettingsDialog;
    @NonNull
    private Handler handler;

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
    private LocationPermissionFlowInterface fragmentCallback;

    @Inject
    public SecondSetupStepsViewModel(@NonNull final Navigator navigator,
                                     @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                     @NonNull final PermissionHandler permissionHandler,
                                     @NonNull final DialogFragment connectingDialog,
                                     @NonNull final DialogFragment unsuccessfulDialog,
                                     @NonNull final DialogFragment gpsSettingsDialog,
                                     @NonNull final Handler handler,
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
        this.connectingDialog = connectingDialog;
        this.unsuccessfulDialog = unsuccessfulDialog;
        this.gpsSettingsDialog = gpsSettingsDialog;
        this.handler = handler;
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
            permissionHandler.requestPermission(fragment, R.string.label_location_permission_required,
                    ACCESS_COARSE_LOCATION,
                    SecondSetupStepsFragment.LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void connect() {
        EWSLogger.d(EWS_STEPS, "Step 1 : Trying to connect to appliance hot spot");
        if (GpsUtil.isGPSRequiredForWifiScan() && !GpsUtil.isGPSEnabled(fragment.getContext())) {
            fragmentCallback.showGPSEnableDialog(baseContentConfiguration);
            //gpsSettingsDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
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

    void showUnsuccessfulDialog() {
        connectingDialog.dismissAllowingStateLoss();
        if (unsuccessfulDialog.getDialog() != null && unsuccessfulDialog.getDialog().isShowing()) {
            return;
        }
        unsuccessfulDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deviceConnectionError(DeviceConnectionErrorEvent deviceConnectionErrorEvent) {
        handler.removeCallbacks(timeoutRunnable);
        showUnsuccessfulDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPasswordEntryScreenEvent(@SuppressWarnings("UnusedParameters") ShowPasswordEntryScreenEvent entryScreenEvent) {
        handler.removeCallbacks(timeoutRunnable);
        if (connectingDialog.isVisible()) {
            connectingDialog.dismissAllowingStateLoss();
        }
        eventBus.unregister(this);
        navigator.navigateToConnectToDeviceWithPasswordScreen("");
    }

    @Nullable
    public LocationPermissionFlowInterface getFragmentCallback() {
        return fragmentCallback;
    }

    public void setFragmentCallback(@Nullable LocationPermissionFlowInterface fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }
}

