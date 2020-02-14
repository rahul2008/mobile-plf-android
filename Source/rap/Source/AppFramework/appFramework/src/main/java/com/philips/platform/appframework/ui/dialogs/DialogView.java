/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appframework.ui.dialogs;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

public class DialogView {

    private final String title;
    private final String message;
    private View view;
    private AlertDialogFragment alertDialogFragment;

    public DialogView(String dialogTitle, String dialogMessage) {
        this.title = dialogTitle;
        this.message = dialogMessage;
    }

    public void showDialog(FragmentActivity activity) {
        if (!(activity.isFinishing())) {
            setupView(activity);
            initializeAlertDialog(activity);
            setupOkButton(alertDialogFragment);
            showButton(activity);
        }
    }

    private void showButton(FragmentActivity activity) {
        alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
    }

    private void setupOkButton(final AlertDialogFragment alertDialogFragment) {
        // Additional behaviour for XML elements
        Button okButton = view.findViewById(R.id.mya_dialog_offline_button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
            }
        });
    }

    private void initializeAlertDialog(FragmentActivity activity) {
        // Setting up the actual dialog
        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);
        alertDialogFragment = builder.create(new AlertDialogFragment());
    }

    private void setupView(FragmentActivity activity) {
        // Philips UI theme
        Context popupThemedContext = UIDHelper.getPopupThemedContext(activity);

        // XML layout for this dialog
        view = LayoutInflater
                .from(activity)
                .cloneInContext(popupThemedContext)
                .inflate(R.layout.af_dialog_connection, null, false);


        TextView titleView = view.findViewById(R.id.mya_offline_label_title);
        titleView.setText(this.title);
        TextView messageView = view.findViewById(R.id.mya_offline_label_message);
        messageView.setText(this.message);
    }
}

