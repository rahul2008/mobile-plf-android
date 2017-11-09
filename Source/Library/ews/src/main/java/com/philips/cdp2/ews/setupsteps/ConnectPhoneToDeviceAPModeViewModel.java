/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.Manifest;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.events.DeviceConnectionErrorEvent;
import com.philips.cdp2.ews.communication.events.NetworkConnectEvent;
import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Named;

import static com.philips.cdp2.ews.EWSActivity.DEVICE_CONNECTION_TIMEOUT;
import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public abstract class ConnectPhoneToDeviceAPModeViewModel {

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

    public ConnectPhoneToDeviceAPModeViewModel(@NonNull final Navigator navigator,
                                               @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                               @NonNull final PermissionHandler permissionHandler,
                                               @NonNull final DialogFragment connectingDialog,
                                               @NonNull final DialogFragment unsuccessfulDialog,
                                               @NonNull final DialogFragment gpsSettingsDialog,
                                               @NonNull final Handler handler) {
        this.navigator = navigator;
        this.permissionHandler = permissionHandler;
        this.eventBus = eventBus;
        this.connectingDialog = connectingDialog;
        this.unsuccessfulDialog = unsuccessfulDialog;
        this.gpsSettingsDialog = gpsSettingsDialog;
        this.handler = handler;
        eventBus.register(this);
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
            gpsSettingsDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
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

    protected void startConnection() {
        handler.postDelayed(timeoutRunnable, DEVICE_CONNECTION_TIMEOUT);
        connectingDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
        eventBus.post(new NetworkConnectEvent(NetworkType.DEVICE_HOTSPOT, WiFiUtil.DEVICE_SSID));
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
}

