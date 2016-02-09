package com.philips.cdp.di.iap.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EmptyCartActivity extends UiKitActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iap_empty_shopping_cart);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.philips.cdp.di.iapdemo", "com.philips.cdp.di.iapdemo.DemoAppActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
