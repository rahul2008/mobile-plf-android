/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class ConnectWithPasswordViewModel extends BaseObservable {

    @NonNull public final ObservableField<String> deviceFriendlyName;
    @NonNull public final ObservableField<String> password;
    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> note;

    @NonNull private final WiFiUtil wiFiUtil;
    @NonNull private final Navigator navigator;
    @NonNull private final StringProvider stringProvider;
    @NonNull private final BaseContentConfiguration baseContentConfiguration;

    @Inject
    public ConnectWithPasswordViewModel(@NonNull final WiFiUtil wiFiUtil,
                                        @NonNull final Navigator navigator,
                                        @NonNull BaseContentConfiguration baseConfig,
                                        @NonNull StringProvider stringProvider) {
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
        this.stringProvider = stringProvider;

        this.password = new ObservableField<>("");
        this.deviceFriendlyName = new ObservableField<>("");
        title = new ObservableField<>(getTitle(baseConfig));
        note = new ObservableField<>(getNote(baseConfig));
        this.baseContentConfiguration = baseConfig;

    }

    @Nullable
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
                stringProvider.getString(baseContentConfiguration.getDeviceName()), deviceFriendlyName.get());
    }

    public void setDeviceFriendlyName(@NonNull String name) {
        deviceFriendlyName.set(name);
    }


    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_password_title,
                baseConfig.getDeviceName(), getHomeWiFiSSID());
    }

    @VisibleForTesting
    @NonNull
    String getNote(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_password_from_name_title,
                baseConfig.getDeviceName());
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.CONNECT_WITH_PASSWORD);
    }
}