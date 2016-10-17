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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.ThemeHelper;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity {

    private ContentTonalRange contentTonalRange = ContentTonalRange.ULTRA_LIGHT;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private NavigationColor navigationColor = NavigationColor.VERY_LIGHT;
    private ThemeHelper themeHelper;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.back_button)
    ImageView backButton;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(this));
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentTonalRange = themeHelper.initTonalRange();
        Log.d("DLS", String.format("[%s]Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                this.getClass().getName(), contentTonalRange, colorRange, navigationColor));

        UITHelper.init(getThemeConfig());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
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
        contentTonalRange = themeHelper.initTonalRange();
        return new ThemeConfiguration(colorRange, contentTonalRange, navigationColor, this);
    }
}
