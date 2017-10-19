/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ApplianceConnectErrorEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import static com.philips.cdp2.ews.tagging.Tag.KEY.PRODUCT_NAME;

@SuppressWarnings("WeakerAccess")
public class SetDeviceInfoViewModel extends BaseObservable {

    static final int APPLIANCE_PAIR_TIME_OUT = 60000;

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

    public ObservableField<String> password;
    private Fragment fragment;


    @Inject
    public SetDeviceInfoViewModel(@NonNull final WiFiUtil wiFiUtil,
                                  @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                  @NonNull final Navigator navigator,
                                  @NonNull final ConnectionEstablishDialogFragment connectingDialog,
                                  @NonNull final DeviceFriendlyNameFetcher deviceFriendlyNameFetcher) {
        this.wiFiUtil = wiFiUtil;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.navigator = navigator;
        this.connectingDialog = connectingDialog;
        this.password = new ObservableField<>("");
        this.deviceFriendlyNameFetcher = deviceFriendlyNameFetcher;
    }

    public String getDeviceName() {
        return sessionDetailsInfo.getDeviceName();
    }

    public String getHomeWiFiSSID() {
        return wiFiUtil.getHomeWiFiSSD();
    }

    @SuppressWarnings("UnusedParameters")
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            password.set(s.toString());
        }
    }

    public void setFragment(@NonNull final Fragment fragment) {
        this.fragment = fragment;
    }

    public void onFocusChange(View view, InputMethodManager inputMethodManager, boolean hasFocus) {
        if (!hasFocus) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void connectApplianceToHomeWiFi() {
        navigator.navigateToConnectingDeviceWithWifiScreen(getHomeWiFiSSID(), password.get(),
                getDeviceName());
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPairingSuccessEvent(@NonNull final PairingSuccessEvent event) {
        dismissDialog();
        EWSCallbackNotifier.getInstance().onApplianceDiscovered(sessionDetailsInfo.getCppId());
        navigator.navigateToPairingSuccessScreen();
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectionErrorOccured(@NonNull final ApplianceConnectErrorEvent errorEvent) {
        showConnectionUnsuccessful();
    }

    private void showConnectionUnsuccessful() {
        dismissDialog();
        navigator.navigateToConnectionUnsuccessfulTroubleShootingScreen("");
    }

    protected void dismissDialog() {
        if (connectingDialog != null) {
            connectingDialog.dismissAllowingStateLoss();
        }
    }

    private void tagConnectionStart() {
        EWSTagger.startTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_START, PRODUCT_NAME,
                EWSDependencyProvider.getInstance().getProductName());
    }

    public void onStart() {
        if (wiFiUtil
                .getCurrentWifiState() == WiFiUtil.HOME_WIFI && (connectingDialog != null &&
                !connectingDialog
                .isAdded())) {
            tagConnectionStart();
            connectingDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
        }
    }

    public void fetchDeviceFriendlyName() {
        deviceFriendlyNameFetcher.fetchFriendlyName(new DeviceFriendlyNameFetcher.Callback() {
            @Override
            public void onFriendlyNameFetchingSuccess(@NonNull String friendlyName) {
                sessionDetailsInfo.setDeviceName(friendlyName);
            }

            @Override
            public void onFriendlyNameFetchingFailed() {
                sessionDetailsInfo.setDeviceName("");
            }
        });
    }

    public void putDeviceFriendlyName() {

    }
}