/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.ScreenFlowController;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class CancelSetupViewModel {

    private ScreenFlowController screenFlowController;
    private DialogFragment dialogDismissListener;

    @Inject
    public CancelSetupViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void cancelSetup() {
        EWSCallbackNotifier.getInstance().onCancel();
        this.screenFlowController.finish();
    }

    public void dismissDialog() {
        dialogDismissListener.dismissAllowingStateLoss();
    }

    public void setDialogDismissListener(final DialogFragment dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public void removeDialogDismissListener() {
        this.dialogDismissListener = null;
    }
}
