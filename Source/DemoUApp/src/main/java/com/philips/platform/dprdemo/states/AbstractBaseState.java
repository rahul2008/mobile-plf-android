/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;
import android.app.ProgressDialog;

public abstract class AbstractBaseState {
    Activity mActivity;
    private static ProgressDialog mProgressDialog;

    public AbstractBaseState(Activity context) {
        mActivity = context;
    }

    abstract void start(StateContext stateContext);

    public void showProgressDialog(final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(mActivity);
                }
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage(message);
                if (mProgressDialog != null && !mProgressDialog.isShowing() && !(mActivity.isFinishing())) {
                    mProgressDialog.show();
                }

            }
        });
    }

    public void dismissProgressDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing() && !(mActivity.isFinishing())) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    public void clearProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()
                && !(mActivity.isFinishing())) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
}
