/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class SetDeviceInfoViewModel extends BaseObservable {

    @NonNull
    private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final ConnectionEstablishDialogFragment connectingDialog;
    @NonNull
    private final DeviceFriendlyNameFetcher deviceFriendlyNameFetcher;

    @NonNull public final ObservableField<String> deviceFriendlyName;
    @NonNull public final ObservableField<String> password;

    @Inject
    public SetDeviceInfoViewModel(@NonNull final WiFiUtil wiFiUtil,
                                  @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                  @NonNull final Navigator navigator,
                                  @NonNull final ConnectionEstablishDialogFragment connectingDialog,
                                  @NonNull final DeviceFriendlyNameFetcher
                                              deviceFriendlyNameFetcher) {
        this.wiFiUtil = wiFiUtil;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.navigator = navigator;
        this.connectingDialog = connectingDialog;
        this.deviceFriendlyNameFetcher = deviceFriendlyNameFetcher;
        this.password = new ObservableField<>("");
        this.deviceFriendlyName = new ObservableField<>("");
    }

    public String getHomeWiFiSSID() {
        return wiFiUtil.getHomeWiFiSSD();
    }

    @SuppressWarnings("UnusedParameters")
    public void onPasswordTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            password.set(s.toString());
        }
    }

    public void onPasswordFocusChange(View view, InputMethodManager inputMethodManager,
                                      boolean hasFocus) {
        if (!hasFocus) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void onDeviceNameTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            deviceFriendlyName.set(s.toString());
        }
    }

    public void onDeviceNameFocusChange(View view, InputMethodManager inputMethodManager,
                                        boolean hasFocus) {
        if (!hasFocus) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onConnectButtonClicked() {
        navigator.navigateToConnectingDeviceWithWifiScreen(getHomeWiFiSSID(), password.get(),
                sessionDetailsInfo.getDeviceName(), deviceFriendlyName.get());
    }

    public void fetchDeviceFriendlyName() {
        deviceFriendlyNameFetcher.fetchFriendlyName(new DeviceFriendlyNameFetcher.Callback() {
            @Override
            public void onFriendlyNameFetchingSuccess(@NonNull String friendlyName) {
                deviceFriendlyName.set(friendlyName);
            }

            @Override
            public void onFriendlyNameFetchingFailed() {
                deviceFriendlyName.set("Failed");
            }
        });
    }

    public void clear() {
        deviceFriendlyNameFetcher.clear();
    }

}