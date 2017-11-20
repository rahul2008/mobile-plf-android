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
        void onStop();
        void onDismiss(DialogInterface dialog);
        void onCancel(DialogInterface dialog);
        void onActivityCreated(Bundle savedInstanceState);
    }

    public EWSAlertDialogFragment() {
    }

    @Nullable
    private FragmentLifeCycleListener fragmentLifeCycleListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onStart();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onCancel(dialog);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fragmentLifeCycleListener != null) {
            fragmentLifeCycleListener.onStop();
        }
    }

    public void setFragmentLifeCycleListener(@Nullable FragmentLifeCycleListener fragmentLifeCycleListener) {
        this.fragmentLifeCycleListener = fragmentLifeCycleListener;
    }
}
