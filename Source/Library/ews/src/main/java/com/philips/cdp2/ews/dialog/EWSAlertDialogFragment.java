/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class EWSAlertDialogFragment extends AlertDialogFragment {

    public interface FragmentLifeCycleListener{
        void onStart();
    }

    public EWSAlertDialogFragment() {
    }

    @Nullable
    private FragmentLifeCycleListener fragmentLifeCycleListener;

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onStart();
        }
    }
    public void setFragmentLifeCycleListener(@Nullable FragmentLifeCycleListener fragmentLifeCycleListener) {
        this.fragmentLifeCycleListener = fragmentLifeCycleListener;
    }
}
