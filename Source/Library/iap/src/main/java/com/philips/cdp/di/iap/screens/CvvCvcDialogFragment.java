/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class CvvCvcDialogFragment extends DialogFragment {
    protected static final int REQUEST_CODE = 0;
    private String mCvv;
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_edit_text_dialog, container, false);
        Bundle bundle = getArguments();
        PaymentMethod mPaymentMethod = null;
        if (bundle.containsKey(IAPConstant.SELECTED_PAYMENT)) {
            mPaymentMethod = (PaymentMethod) bundle.getSerializable(IAPConstant.SELECTED_PAYMENT);
        }
        TextView iap_mastercard = (TextView) view.findViewById(R.id.iap_mastercard);
        TextView iap_mastercard_number = (TextView) view.findViewById(R.id.iap_mastercard_number);
        if (mPaymentMethod != null) {
            iap_mastercard.setText(mPaymentMethod.getCardType().getCode());
            iap_mastercard_number.setText(mPaymentMethod.getCardNumber());
        }
        final Button btnProceed = (Button) view.findViewById(R.id.dialogButtonOk);
        mEditText = (EditText) view.findViewById(R.id.iap_edit_box);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NOP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    btnProceed.setEnabled(false);
                } else {
                    btnProceed.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NOP
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCvv = mEditText.getText().toString();
                dismiss();
                setShowsDialog(false);
                sendResult(REQUEST_CODE);
            }
        });

        mEditText.requestFocus();
        return view;
    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra(IAPConstant.CVV_KEY_BUNDLE, mCvv);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

}
