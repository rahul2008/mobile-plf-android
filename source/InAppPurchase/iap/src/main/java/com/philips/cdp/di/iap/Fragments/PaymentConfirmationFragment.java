/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.session.NetworkConstants;

public class PaymentConfirmationFragment extends BaseAnimationSupportFragment {
    private TextView mThank;
    private TextView mOrderNumber;
    private TextView mConfimWithEmail;
    private Button mOKButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.iap_payment_confirmation, null);
        bindViews(view);
        assignValues();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_confirmation);
        setBackButtonVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void assignValues() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ModelConstants.ORDER_NUMBER)) {
                mOrderNumber.setText(arguments.getString(ModelConstants.ORDER_NUMBER));
            }
            String email = "";
            if(arguments.containsKey(ModelConstants.EMAIL_ADDRESS)) {
                email = arguments.getString(ModelConstants.EMAIL_ADDRESS);
            }
            String emailConfirmation = String.format(getContext().getString(R.string
                    .iap_confirmation_email_msg), email);
            mConfimWithEmail.setText(emailConfirmation);
        }
    }

    private void bindViews(ViewGroup viewGroup) {
        mThank = (TextView) viewGroup.findViewById(R.id.tv_thank);
        mOrderNumber = (TextView) viewGroup.findViewById(R.id.tv_order_num);
        mConfimWithEmail = (TextView) viewGroup.findViewById(R.id.tv_confirm_email);
        mOKButton = (Button) viewGroup.findViewById(R.id.tv_confirm_ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finishActivity();
            }
        });
    }

    public static PaymentConfirmationFragment createInstance(final Bundle bundle, final AnimationType animType) {
        PaymentConfirmationFragment fragment = new PaymentConfirmationFragment();
        bundle.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }
}