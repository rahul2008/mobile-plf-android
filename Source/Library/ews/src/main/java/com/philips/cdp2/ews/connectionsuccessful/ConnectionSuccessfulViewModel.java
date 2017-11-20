/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.connectionsuccessful;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

public class ConnectionSuccessfulViewModel {

    @Nullable private FragmentCallback fragmentCallback;

    @NonNull StringProvider stringProvider;
    @NonNull public final ObservableField<String> title;
    @Inject
    public ConnectionSuccessfulViewModel(@NonNull BaseContentConfiguration baseConfig,
                                         @NonNull StringProvider stringProvider) {
        this.stringProvider = stringProvider;
        title = new ObservableField<>(getTitle(baseConfig));
    }

    public void setFragmentCallback(@NonNull FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void onStartClicked() {
        EWSCallbackNotifier.getInstance().onSuccess();
        if (fragmentCallback != null) {
            fragmentCallback.finishMicroApp();
        }
    }

    @VisibleForTesting
    @NonNull
    public String getTitle(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_succesful_body,
                baseConfig.getDeviceName());
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.CONNECTION_SUCCESSFUL);
    }
}