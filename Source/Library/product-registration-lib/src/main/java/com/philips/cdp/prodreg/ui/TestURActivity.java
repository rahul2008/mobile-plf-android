package com.philips.cdp.prodreg.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.uikit.UiKitActivity;

public class TestURActivity extends UiKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ur);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        InitialFragment initialFragment = new InitialFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, initialFragment,
                RegConstants.REGISTRATION_FRAGMENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
