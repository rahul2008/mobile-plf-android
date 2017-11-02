/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class CancelSetupViewModel {

    @Nullable private FragmentCallback fragmentCallback;
    @Nullable private DialogFragment dialogDismissListener;

    @Inject
    public CancelSetupViewModel() {}

    public void setDialogDismissListener(@NonNull DialogFragment dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public void removeDialogDismissListener() {
        this.dialogDismissListener = null;
    }

    public void setFragmentCallback(@NonNull FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void cancelSetup() {
        EWSCallbackNotifier.getInstance().onCancel();
        if (fragmentCallback != null) {
            fragmentCallback.finishMicroApp();
        }
    }

    public void dismissDialog() {
        if (dialogDismissListener != null) {
            dialogDismissListener.dismissAllowingStateLoss();
        }
    }
}
