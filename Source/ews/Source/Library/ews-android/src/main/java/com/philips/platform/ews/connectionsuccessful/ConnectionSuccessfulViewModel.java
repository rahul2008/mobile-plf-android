/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.connectionsuccessful;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.ews.R;
import com.philips.platform.ews.common.callbacks.FragmentCallback;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.StringProvider;
import com.philips.platform.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class ConnectionSuccessfulViewModel {

    @Nullable
    private FragmentCallback fragmentCallback;

    @NonNull
    private StringProvider stringProvider;
    @NonNull
    public final ObservableField<String> title;
    @NonNull
    private WiFiUtil wiFiUtil;

    @NonNull private final EWSTagger ewsTagger;

    @Inject
    public ConnectionSuccessfulViewModel(@NonNull BaseContentConfiguration baseConfig,
                                         @NonNull StringProvider stringProvider,
                                         @NonNull WiFiUtil wiFiUtil,
                                         @NonNull final EWSTagger ewsTagger) {
        this.stringProvider = stringProvider;
        this.wiFiUtil = wiFiUtil;
        title = new ObservableField<>(getTitle(baseConfig));
        this.ewsTagger = ewsTagger;
    }

    protected void setFragmentCallback(@NonNull FragmentCallback fragmentCallback) {
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
        ewsTagger.trackPage(Page.CONNECTION_SUCCESSFUL);
    }
}