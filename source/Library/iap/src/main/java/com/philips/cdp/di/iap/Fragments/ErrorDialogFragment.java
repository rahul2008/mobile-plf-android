/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class ErrorDialogFragment extends DialogFragment {
    public interface ErrorDialogListener {
        void onDialogOkClick();
    }

    Bundle bundle;
    private ErrorDialogListener mErrorDialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_error_dialog, container, false);
        bundle = getArguments();
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_TEXT));

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION));

        Button mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(IAPConstant.MODEL_ALERT_BUTTON_TEXT));
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorDialogListener != null) {
                    mErrorDialogListener.onDialogOkClick();
                }
                dismissDialog();
            }
        });
        return v;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    public void setErrorDialogListener(ErrorDialogListener dialogClickListener) {
        mErrorDialogListener = dialogClickListener;
    }

    private void dismissDialog() {
        dismiss();
        setShowsDialog(false);
    }
}
