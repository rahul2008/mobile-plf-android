package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getTheme(this));
        super.onCreate(savedInstanceState);
    }

}
