/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UITHelper.injectCalligraphyFonts();
        UITHelper.init(getThemeConfig());
        super.onCreate(savedInstanceState);
    }

    private ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, ContentTonalRange.ULTRA_LIGHT, this);
    }

    public void switchTo(final int layout) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        setContentView(layout);
                    }
                }
        );
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
