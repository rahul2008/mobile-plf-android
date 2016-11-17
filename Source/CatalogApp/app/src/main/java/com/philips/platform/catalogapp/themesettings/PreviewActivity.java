/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.philips.platform.catalogapp.BuildConfig;
import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity {

    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private NavigationColor navigationColor = NavigationColor.VERY_LIGHT;
    private ThemeHelper themeHelper;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(this));

        UITHelper.init(getThemeConfig());

        if (BuildConfig.DEBUG) {
            Log.d(PreviewActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                    contentColor, colorRange, navigationColor));
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.page_tittle_preview);

        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    private Drawable getIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.ic_theme_setting_entrance_icon, getTheme());
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public ThemeConfiguration getThemeConfig() {
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        return new ThemeConfiguration(colorRange, contentColor, navigationColor, this);
    }
}
