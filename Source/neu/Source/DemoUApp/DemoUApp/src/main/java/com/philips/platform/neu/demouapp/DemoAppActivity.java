/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.neu.demouapp;

import android.os.Bundle;

import com.philips.platform.mya.demouapp.R;
import com.philips.platform.uid.utils.UIDActivity;



public class DemoAppActivity extends UIDActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
