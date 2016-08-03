/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

public class ProdRegErrorAlertFragment extends BlurDialogFragment {

    private static final String TAG = ProdRegErrorAlertFragment.class.getSimpleName();
    private TextView titleTextView, descriptionTextView;
    private DialogOkButtonListener dialogOkButtonListener;

    public static ProdRegErrorAlertFragment newInstance(String title, String description) {
        ProdRegErrorAlertFragment frag = new ProdRegErrorAlertFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        frag.setCancelable(false);
        frag.setArguments(args);
        return frag;
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogOkButtonListener != null)
                    dialogOkButtonListener.onOkButtonPressed();
            }
        };
    }

    public void setDialogOkButtonListener(final DialogOkButtonListener dialogOkButtonListener) {
        this.dialogOkButtonListener = dialogOkButtonListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String description = getArguments().getString("description");
        ProdRegLogger.v(TAG, "Starting dialog with error:" + title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.prodreg_alert_dialog, null);
        Button always = (Button) v.findViewById(R.id.dialogButtonOK);
        titleTextView = (TextView) v.findViewById(R.id.dialogTitle);
        titleTextView.setText(title);
        descriptionTextView = (TextView) v.findViewById(R.id.dialogDescription);
        descriptionTextView.setText(description);
        always.setOnClickListener(dismissDialog());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        setRetainInstance(true);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}
