package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CatalogActivity extends UiKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE)).getTheme());
        super.onCreate(savedInstanceState);
    }

}
