package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.UiKitActivity;
import com.shamanland.fonticon.FontIconTypefaceHolder;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CatalogActivity extends UiKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE)).getTheme());
        initFontIconLib();

        super.onCreate(savedInstanceState);

    }
    private void initFontIconLib() {
        try {
            FontIconTypefaceHolder.getTypeface();

        }
        catch(IllegalStateException e)
        {
            FontIconTypefaceHolder.init(getAssets(), "fonts/puicon.ttf");
        }
    }
}