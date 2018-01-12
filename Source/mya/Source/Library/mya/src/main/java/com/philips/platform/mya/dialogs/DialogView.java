/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.mya.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

public class DialogView {

    private final String title;
    private final String message;

    public DialogView(String dialogTitle, String dialogMessage) {
        this.title = dialogTitle;
        this.message = dialogMessage;
    }

    public void showDialog(FragmentActivity activity) {
        if (!(activity.isFinishing())) {
            // Philips UI theme
            Context popupThemedContext = UIDHelper.getPopupThemedContext(activity);

            // XML layout for this dialog
            View view = LayoutInflater
                    .from(activity)
                    .cloneInContext(popupThemedContext)
                    .inflate(R.layout.mya_dialog_connection, null, false);

            // Setting up the actual dialog
            AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity)
                    .setDialogView(view)
                    .setDialogType(DialogConstants.TYPE_DIALOG)
                    .setDimLayer(DialogConstants.DIM_STRONG)
                    .setCancelable(false);
            final AlertDialogFragment alertDialogFragment = builder.create(new AlertDialogFragment());

            // Additional behaviour for XML elements
            Button okButton = view.findViewById(R.id.mya_dialog_offline_button_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogFragment.dismiss();
                }
            });
            TextView titleView = view.findViewById(R.id.mya_offline_label_title);
            titleView.setText(this.title);
            TextView messageView = view.findViewById(R.id.mya_offline_label_message);
            messageView.setText(this.message);

            // Show the dialog to the user
            alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        }
    }
}

