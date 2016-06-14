package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;


public class TwoButtonDailogFragment extends DialogFragment {
    public interface TwoButtonDialogListener {
        public void onDialogOkClick();

        public void onDialogCancelClick();
    }

    private TwoButtonDialogListener mDialogClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_two_button_dialog, container, false);
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.iap_confirmation);
        Bundle bundle = getArguments();
        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        //errorDescription.setText(R.string.cancelPaymentMsg);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_CONFIRM_DESCRIPTION));
        Button cancel = (Button) v.findViewById(R.id.dialogButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogClickListener != null) {
                    mDialogClickListener.onDialogCancelClick();
                }
                dismissDialog();
            }
        });
        cancel.setText(R.string.iap_cancel);
        Button ok = (Button) v.findViewById(R.id.dialogButtonOk);
        ok.setText(R.string.iap_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogClickListener != null) {
                    mDialogClickListener.onDialogOkClick();
                }
                dismissDialog();
            }
        });

        return v;
    }

    public void setOnDialogClickListener(TwoButtonDialogListener dialogClickListener) {
        mDialogClickListener = dialogClickListener;
    }


    private void dismissDialog() {
        dismiss();
        setShowsDialog(false);
    }


}
