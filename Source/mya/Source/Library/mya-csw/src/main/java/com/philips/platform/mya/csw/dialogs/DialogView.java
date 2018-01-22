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
import com.philips.platform.uid.view.widget.Button;

public class DialogView {

    private View view;
    private AlertDialogFragment alertDialogFragment;
    private View.OnClickListener okListener;

    public DialogView(){
        okListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
            }
        };
    }

    public DialogView(View.OnClickListener listener) {
        okListener = listener;
    }

    public void showDialog(FragmentActivity activity) {
        if (!(activity.isFinishing())) {
            setupView(activity);
            setupAlertDialogFragment(activity);
            setupOkButton();
            showButton(activity);
        }
    }

    protected void showButton(FragmentActivity activity) {
        alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
    }

    protected void setupOkButton() {
        Button okButton = getOkButton();
        okButton.setOnClickListener(okListener);
    }

    protected Button getOkButton() {
        return view.findViewById(R.id.cws_dialog_offline_button_ok);
    }

    protected void setupAlertDialogFragment(FragmentActivity activity) {
        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity)
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
                .inflate(R.layout.csw_dialog_connection, null, false);
    }

}

