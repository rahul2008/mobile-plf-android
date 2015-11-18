package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CatalogActivity extends UiKitActivity {
    protected ThemeUtils themeUtils;
    private int noActionBarTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (noActionBarTheme > 0) {
            setTheme(noActionBarTheme);
        } else {
            themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name),
                    Context.MODE_PRIVATE));
            setTheme(themeUtils.getTheme());
        }
        super.onCreate(savedInstanceState);
    }

    protected void setNoActionBarTheme() {
        themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name),
                Context.MODE_PRIVATE));
        noActionBarTheme = themeUtils.getNoActionBarTheme();
    }
}
