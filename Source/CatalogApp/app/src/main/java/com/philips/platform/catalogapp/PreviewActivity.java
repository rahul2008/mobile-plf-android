package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity {

    private ContentTonalRange contentTonalRange = ContentTonalRange.ULTRA_LIGHT;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private NavigationColor navigationColor = NavigationColor.VERY_LIGHT;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        initTonalRange();
        initColorRange();
        initNavigationRange();
        Log.d("DLS", String.format("[%s]Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                this.getClass().getName(), contentTonalRange, colorRange, navigationColor));

        UITHelper.init(getThemeConfig());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);
        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
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
        initTonalRange();
        initColorRange();
        initNavigationRange();
        return new ThemeConfiguration(colorRange, contentTonalRange, this);
    }

    private void initNavigationRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String navigation = sharedPreferences.getString(UITHelper.NAVIGATION_RANGE, NavigationColor.VERY_LIGHT.name());
        navigationColor = NavigationColor.valueOf(navigation);
    }

    private void initColorRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String color = sharedPreferences.getString(UITHelper.COLOR_RANGE, ColorRange.GROUP_BLUE.name());
        colorRange = ColorRange.valueOf(color);
    }

    private void initTonalRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tonalRange = sharedPreferences.getString(UITHelper.CONTENT_TONAL_RANGE, ContentTonalRange.ULTRA_LIGHT.name());
        contentTonalRange = ContentTonalRange.valueOf(tonalRange);
    }
}
