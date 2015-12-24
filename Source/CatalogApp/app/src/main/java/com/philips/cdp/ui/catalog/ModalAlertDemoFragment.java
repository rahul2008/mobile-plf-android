package com.philips.cdp.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertDemoFragment extends BlurDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(com.philips.cdp.uikit.R.layout.uikit_modal_alert, container, false);
        Button justOnce = (Button) v.findViewById(R.id.dialogButtonCancel);
        Button always = (Button) v.findViewById(R.id.dialogButtonOK);
        justOnce.setOnClickListener(dismissDialog());
        always.setOnClickListener(dismissDialog());
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
