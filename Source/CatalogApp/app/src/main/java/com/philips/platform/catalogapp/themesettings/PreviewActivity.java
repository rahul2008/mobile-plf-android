/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.catalogapp.BuildConfig;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.ActivityPreviewBinding;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class PreviewActivity extends AppCompatActivity {

    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private ThemeHelper themeHelper;
    private NavigationColor navigationColor = NavigationColor.ULTRA_LIGHT;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(this));

        UIDHelper.init(getThemeConfig());

        if (BuildConfig.DEBUG) {
            Log.d(PreviewActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s",
                    contentColor, this.colorRange));
        }
        super.onCreate(savedInstanceState);

        final ActivityPreviewBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview);
        dataBinding.setPreviewActivity(this);

        final com.philips.platform.catalogapp.databinding.PreviewToolbarBinding toolbar = dataBinding.toolbar;
        toolbar.setPreActivity(this);
        setSupportActionBar(toolbar.previewbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public ThemeConfiguration getThemeConfig() {
        colorRange = themeHelper.initColorRange();
        contentColor = themeHelper.initContentTonalRange();
        navigationColor = themeHelper.initNavigationRange();
        return new ThemeConfiguration(colorRange, contentColor, navigationColor, this);
    }

    public void cancelOrPreviewClicked() {
        finish();
    }
}
