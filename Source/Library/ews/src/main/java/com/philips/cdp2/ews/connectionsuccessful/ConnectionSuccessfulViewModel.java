/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.connectionsuccessful;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class ConnectionSuccessfulViewModel {

    @Nullable
    private FragmentCallback fragmentCallback;

    @NonNull
    StringProvider stringProvider;
    @NonNull
    public final ObservableField<String> title;
    @NonNull
    private WiFiUtil wiFiUtil;

    @Inject
    public ConnectionSuccessfulViewModel(@NonNull BaseContentConfiguration baseConfig,
                                         @NonNull StringProvider stringProvider,
                                         @NonNull WiFiUtil wiFiUtil) {
        this.stringProvider = stringProvider;
        this.wiFiUtil = wiFiUtil;
        title = new ObservableField<>(getTitle(baseConfig));
    }

    public void setFragmentCallback(@NonNull FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void onStartClicked() {
        if (fragmentCallback != null) {
            fragmentCallback.finishMicroApp();
        }
    }

    @NonNull
    public String getTitle(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_succesful_body,
                baseConfig.getDeviceName(), getHomeWiFiSSID());
    }

    @Nullable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getHomeWiFiSSD();
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.CONNECTION_SUCCESSFUL);
    }
}