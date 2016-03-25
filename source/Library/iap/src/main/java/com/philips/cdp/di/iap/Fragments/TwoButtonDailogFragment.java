package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * Created by 310164421 on 3/16/2016.
 */
public class TwoButtonDailogFragment extends BlurDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_two_button_dialog, container, false);
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.iap_confirmation);

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(R.string.cancelPaymentMsg);

        Button cancel = (Button) v.findViewById(R.id.dialogButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancel.setText(R.string.iap_cancel);
        Button ok = (Button) v.findViewById(R.id.dialogButtonOk);
        ok.setText(R.string.iap_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }
}
