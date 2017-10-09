/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.plataform.mya;


import android.os.Bundle;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.mya.R;

public class MyAccountActivity extends UiKitActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consents);
    }

}
