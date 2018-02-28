/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class ProgressDialogView implements View.OnClickListener {
    protected View view;
    protected AlertDialogFragment alertDialogFragment;
    protected boolean isDialogShown;
    private View.OnClickListener okListener;

    public ProgressDialogView() {
        this.okListener = null;
        this.isDialogShown = false;
    }

    public ProgressDialogView(final View.OnClickListener listener) {
        this.okListener = listener;
        this.isDialogShown = false;
    }

    public void showDialog(FragmentActivity activity) {
        if (!(activity.isFinishing()) && !isDialogShown) {
            setupView(activity);
            setupAlertDialogFragment(activity);
            displayDialog(activity);
            isDialogShown = true;
        }
    }

    public void hideDialog() {
        if (alertDialogFragment != null && isDialogShown) {
            alertDialogFragment.dismiss();
            isDialogShown = false;
        }
    }

    public boolean isDialogShown() {
        return isDialogShown;
    }

    protected void displayDialog(FragmentActivity activity) {
        alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
    }

    protected void setupAlertDialogFragment(FragmentActivity activity) {
        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity, R.style.MyaAlertDialog)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);

        alertDialogFragment = builder.create(new AlertDialogFragment());
    }

    protected void setupView(FragmentActivity activity) {
        Context popupThemedContext = UIDHelper.getPopupThemedContext(activity);
        view = LayoutInflater
                .from(activity)
                .cloneInContext(popupThemedContext)
                .inflate(R.layout.csw_progress_dialog_connection, null, false);
    }

    @Override
    public void onClick(View view) {
        alertDialogFragment.dismiss();
        if (okListener != null) {
            okListener.onClick(view);
        }
    }
}
