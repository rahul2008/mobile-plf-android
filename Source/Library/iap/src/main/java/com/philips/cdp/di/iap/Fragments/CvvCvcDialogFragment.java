/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;

public class CvvCvcDialogFragment extends DialogFragment  {

    public static final String CVV_KEY_BUNDLE = "CVV_KEY_BUNDLE"; // can be moved to IAPConstant where all the bundle key has maintained
    public static final int REQUEST_CODE = 0 ;
    String cvvValue; //Can be changed to mCVV
    EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_edit_text_dialog, container, false);

        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.iap_txt_cvv_cvc);

        mEditText = (EditText) v.findViewById(R.id.iap_edit_box);

        Button btnOk = (Button) v.findViewById(R.id.dialogButtonOk);
        Button btnCancel = (Button) v.findViewById(R.id.dialogButtonCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvvValue = mEditText.getText().toString(); //Empty string handling is missing?
                dismiss();
                setShowsDialog(false);
                sendResult(REQUEST_CODE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                setShowsDialog(false);
            }
        });
        mEditText.requestFocus();

        return v;
    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra(CVV_KEY_BUNDLE, cvvValue);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

}
