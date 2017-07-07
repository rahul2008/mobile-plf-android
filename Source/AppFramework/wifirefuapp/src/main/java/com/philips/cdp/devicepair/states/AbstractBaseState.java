/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.states;

import android.app.ProgressDialog;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

public abstract class AbstractBaseState {
    FragmentLauncher context;
    private static ProgressDialog mProgressDialog;

    public AbstractBaseState(FragmentLauncher context) {
        this.context = context;
    }

    abstract void start(StateContext stateContext);

    public void showProgressDialog(final String message) {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(context.getFragmentActivity());
                }
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage(message);
                if (mProgressDialog != null && !mProgressDialog.isShowing() && !(context.getFragmentActivity().isFinishing())) {
                    mProgressDialog.show();
                }

            }
        });
    }

    public void dismissProgressDialog() {
        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing() && !(context.getFragmentActivity().isFinishing())) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    public void clearProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()
                && !(context.getFragmentActivity().isFinishing())) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
}
