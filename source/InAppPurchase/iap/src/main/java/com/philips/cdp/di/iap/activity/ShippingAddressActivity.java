package com.philips.cdp.di.iap.activity;

import android.os.Bundle;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShippingAddressActivity extends UiKitActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_address_layout);
    }
}
