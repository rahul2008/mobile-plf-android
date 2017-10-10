/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.plataform.mya;


import android.os.Bundle;
import com.philips.platform.mya.R;
import com.philips.platform.uid.utils.UIDActivity;

public class MyAccountActivity extends UIDActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consents);
    }

}
