package com.philips.cdp.di.iap.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;

public class PaymentMethodsHolder extends RecyclerView.ViewHolder {

    UIKitRadioButton paymentRadioBtn;
    TextView cardName;
    TextView cardHoldername;
    TextView cardValidity;
    Button usePayment;
    Button addNewPayment;
    ViewGroup paymentOptions;

    public PaymentMethodsHolder(final View view) {
        super(view);
        paymentRadioBtn = (UIKitRadioButton) view.findViewById(R.id.radio_btn_payment);
        cardName = (TextView) view.findViewById(R.id.tv_card_name);
        cardHoldername = (TextView) view.findViewById(R.id.tv_card_holder_name);
        cardValidity = (TextView)view.findViewById(R.id.tv_card_validity);
        usePayment = (Button) view.findViewById(R.id.btn_use_payment_method);
        addNewPayment = (Button) view.findViewById(R.id.btn_add_new_payment);
        paymentOptions = (ViewGroup) view.findViewById(R.id.ll_payment_options);
    }
}

