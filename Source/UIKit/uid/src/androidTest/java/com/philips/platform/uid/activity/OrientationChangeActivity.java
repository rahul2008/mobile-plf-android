/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.activity;


import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;


public class OrientationChangeActivity extends BaseTestActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.philips.platform.uid.test.R.layout.layout_orientation_change);
    }

}
