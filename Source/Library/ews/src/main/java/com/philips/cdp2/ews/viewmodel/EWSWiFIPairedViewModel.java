/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.view.FragmentCallback;

import javax.inject.Inject;

public class EWSWiFIPairedViewModel {

    @Nullable private FragmentCallback fragmentCallback;

    @Inject
    public EWSWiFIPairedViewModel() {
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
}