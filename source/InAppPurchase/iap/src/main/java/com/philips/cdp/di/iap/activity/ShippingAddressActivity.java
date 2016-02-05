package com.philips.cdp.di.iap.activity;

import android.app.Activity;
import android.os.Bundle;

import com.philips.cdp.di.iap.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShippingAddressActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_address_layout);
    }
}
