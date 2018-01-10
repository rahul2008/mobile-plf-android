/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.csw.dialogs;

import android.app.AlertDialog;
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
    private static final String TAG = "DialogView";

    private AlertDialog alertDialog;

    public void showDialog(FragmentActivity activity) {
        if (!(activity.isFinishing())) {
            Context popupThemedContext = UIDHelper.getPopupThemedContext(activity);
            View view = LayoutInflater
                    .from(activity)
                    .cloneInContext(popupThemedContext)
                    .inflate(R.layout.csw_dialog_connection, null, false);

            AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity)
                    .setDialogView(view)
                    .setDialogType(DialogConstants.TYPE_DIALOG)
                    .setDimLayer(DialogConstants.DIM_STRONG)
                    .setCancelable(false);

            final AlertDialogFragment alertDialogFragment = builder.create(new AlertDialogFragment());

            Button okButton = view.findViewById(R.id.cws_dialog_offline_button_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogFragment.dismiss();
                }
            });

            alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        }
    }

    public void hideDialog() {
        if(alertDialog != null && alertDialog.isShowing()) {
            alertDialog.hide();
        }
    }
}

