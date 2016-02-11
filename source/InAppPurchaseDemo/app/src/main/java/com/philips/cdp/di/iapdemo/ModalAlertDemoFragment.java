package com.philips.cdp.di.iapdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

import org.w3c.dom.Text;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertDemoFragment extends BlurDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_modal_alert, container, false);
        Button CANCEL = (Button) v.findViewById(R.id.dialogButtonCancel);

        Button OK = (Button) v.findViewById(R.id.dialogButtonOK);

        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        TextView dialogDescription = (TextView) v.findViewById(R.id.dialogDescription);

        OK.setOnClickListener(dismissDialog());

        dialogTitle.setText(getResources().getString(R.string.iap_add_to_cart));
        dialogDescription.setText(getResources().getString(R.string.no_stock_description));
        return v;
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }
}
