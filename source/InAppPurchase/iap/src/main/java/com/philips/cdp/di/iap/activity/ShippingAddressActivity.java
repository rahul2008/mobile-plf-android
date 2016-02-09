package com.philips.cdp.di.iap.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShippingAddressActivity extends UiKitActivity implements View.OnClickListener{

    Button mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_address_layout);

        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mCancelBtn){
            finish();
        }
    }
}
