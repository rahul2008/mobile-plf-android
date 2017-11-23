/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.dialog;

import android.support.annotation.Nullable;

import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class EWSAlertDialogFragment extends AlertDialogFragment {

    public interface DialogLifeCycleListener {
        void onStart();
    }

    public EWSAlertDialogFragment() {
    }

    @Nullable
    private DialogLifeCycleListener dialogLifeCycleListener;

    @Override
    public void onStart() {
        super.onStart();
        if (dialogLifeCycleListener != null) {
            dialogLifeCycleListener.onStart();
        }
    }
    public void setDialogLifeCycleListener(@Nullable DialogLifeCycleListener dialogLifeCycleListener) {
        this.dialogLifeCycleListener = dialogLifeCycleListener;
    }
}
