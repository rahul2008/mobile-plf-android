/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.customviews.UIKitRadioButton;

public class AddressSelectionHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView address;
    UIKitRadioButton toggle;
    ImageView options;
    Button deliver;
    Button newAddress;
    ViewGroup paymentOptions;
    ViewGroup optionLayout;

    public AddressSelectionHolder(final View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.tv_name);
        address = (TextView) view.findViewById(R.id.tv_address);
        toggle = (UIKitRadioButton) view.findViewById(R.id.rbtn_toggle);
        options = (ImageView) view.findViewById(R.id.img_options);
        paymentOptions = (ViewGroup) view.findViewById(R.id.payment_options);
        deliver = (Button) view.findViewById(R.id.btn_deliver);
        newAddress = (Button) view.findViewById(R.id.btn_new_address);
        optionLayout = (ViewGroup) view.findViewById(R.id.options_layout);
    }
}