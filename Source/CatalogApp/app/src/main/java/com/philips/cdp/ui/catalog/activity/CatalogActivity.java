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
        checkForGradient();
    }

    private void checkForGradient(){
        TypedArray a = getTheme().obtainStyledAttributes(R.style.Theme_Philips, new int[] {R.attr.gradientBackground});
        int attributeResourceId = a.getResourceId(0, 0);
        if (attributeResourceId != 0)
            setActionBarTheme(attributeResourceId);
    }

    private void setActionBarTheme(final int attributeResourceId) {
        ActionBar actionBar = getSupportActionBar();
        setGradientDrawable(actionBar,attributeResourceId);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void setGradientDrawable(final ActionBar actionBar, int path) {
        GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(path);
        gradientDrawable.mutate();
        actionBar.setBackgroundDrawable(gradientDrawable);
    }
}
