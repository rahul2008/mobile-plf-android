package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.app.Activity;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.shamanland.fonticon.FontIconTypefaceHolder;

public class SocialIconsActivity extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFontIconLib();
        setContentView(R.layout.activity_social_icons);
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