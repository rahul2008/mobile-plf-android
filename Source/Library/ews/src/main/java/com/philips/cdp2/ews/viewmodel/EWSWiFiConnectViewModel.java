/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.os.Handler;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.tagging.Tag.KEY.PRODUCT_NAME;

@SuppressWarnings("WeakerAccess")
public class EWSWiFiConnectViewModel extends BaseObservable {

    static final int APPLIANCE_PAIR_TIME_OUT = 60000;

    @NonNull private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    @NonNull private final WiFiUtil wiFiUtil;
    @NonNull private final EventBus eventBus;
    @NonNull private final Navigator navigator;
    @NonNull private final ConnectionEstablishDialogFragment connectingDialog;

    public ObservableField<String> password;
    private Fragment fragment;
    private Handler handler;
    Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            showConnectionUnsuccessful();
        }
    };

    @Inject
    public EWSWiFiConnectViewModel(@NonNull final WiFiUtil wiFiUtil,
                                   @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                   @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                   @NonNull final Navigator navigator,
                                   @NonNull final ConnectionEstablishDialogFragment connectingDialog,
                                   @NonNull final Handler handler) {
        this.wiFiUtil = wiFiUtil;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.eventBus = eventBus;
        this.navigator = navigator;
        this.connectingDialog = connectingDialog;
        this.handler = handler;
        this.password = new ObservableField<>("");
        this.eventBus.register(this);
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
        navigator.navigateToConnectingDeviceWithWifiScreen(getHomeWiFiSSID(), password.get());
        handler.postDelayed(timeoutRunnable, APPLIANCE_PAIR_TIME_OUT);
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPairingSuccessEvent(@NonNull final PairingSuccessEvent event) {
        dismissDialog();
        removeTimeoutRunnable();
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
        removeTimeoutRunnable();
        navigator.navigateToConnectionUnsuccessfulTroubleShootingScreen();
    }

    protected void dismissDialog() {
        if (connectingDialog != null) {
            connectingDialog.dismissAllowingStateLoss();
        }
    }

    private void tagConnectionStart() {
        EWSTagger.startTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_START, PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
    }

    private void removeTimeoutRunnable() {
        handler.removeCallbacks(timeoutRunnable);
    }

    public void unregister() {
        eventBus.unregister(this);
    }

    public void onStart() {
        if (wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI && (connectingDialog != null && !connectingDialog.isAdded())) {
            tagConnectionStart();
            connectingDialog.show(fragment.getFragmentManager(), fragment.getClass().getName());
            handler.postDelayed(timeoutRunnable, APPLIANCE_PAIR_TIME_OUT);
        }
    }
}