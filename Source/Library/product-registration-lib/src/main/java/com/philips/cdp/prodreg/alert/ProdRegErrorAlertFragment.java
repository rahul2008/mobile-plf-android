package com.philips.cdp.prodreg.alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegErrorAlertFragment extends BlurDialogFragment {

    private TextView titleTextView, descriptionTextView;
    private DialogOkButtonListener dialogOkButtonListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prodreg_alert_dialog, container, false);
        Button always = (Button) v.findViewById(R.id.dialogButtonOK);
        titleTextView = (TextView) v.findViewById(R.id.dialogTitle);
        descriptionTextView = (TextView) v.findViewById(R.id.dialogDescription);
        always.setOnClickListener(dismissDialog());

        return v;
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogOkButtonListener != null)
                    dialogOkButtonListener.onOkButtonPressed();
                dismiss();
            }
        };
    }

    public void setTitle(String title) {
        titleTextView.setText(title != null ? title : "");
    }

    public void setDescription(String description) {
        descriptionTextView.setText(description != null ? description : "");
    }

    public void setDialogOkButtonListener(final DialogOkButtonListener dialogOkButtonListener) {
        this.dialogOkButtonListener = dialogOkButtonListener;
    }
}
