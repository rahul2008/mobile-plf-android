package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorDialogFragment extends BlurDialogFragment {

    private Button mOkBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_error_dialog, container, false);

        Bundle bundle = getArguments();
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_TEXT));

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION));

        mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(IAPConstant.MODEL_ALERT_BUTTON_TEXT));

        mOkBtn.setOnClickListener(dismissDialog());
        return v;
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                setShowsDialog(false);
            }
        };
    }
}
