package com.philips.cdp2.ews.demoapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.philips.cdp2.ews.demoapplication.themesettinngs.ThemeHelper;
import com.philips.cdp2.ews.demoapplication.themesettinngs.ThemeSettingsFragment;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

public class EWSDemoActivity extends UIDActivity {

    private SharedPreferences defaultSharedPreferences;
    private OptionSelectionFragment optionSelectionFragment;

    public ColorRange colorRange = ColorRange.GROUP_BLUE;
    public ContentColor contentColor = ContentColor.VERY_DARK;
    public AccentRange accentColorRange = AccentRange.ORANGE;
    public NavigationColor navigationColor = NavigationColor.VERY_DARK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_DLS_GroupBlue_UltraLight);
        UIDHelper.init(new ThemeConfiguration(this, colorRange, navigationColor,
                contentColor));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.optionSelectionFragment = new OptionSelectionFragment();
        showConfigurationOptScreen();
        updateColorFromPref();
        setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(com.philips.cdp2.ews.R.id.ews_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        toolbar.inflateMenu(R.menu.option_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_set_theme_settings:
                saveThemeSettings();
                showConfigurationOptScreen();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showConfigurationOptScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, optionSelectionFragment)
                .addToBackStack(optionSelectionFragment.getClass().getCanonicalName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateContentColor(ContentColor contentColor) {
        this.contentColor = contentColor;
    }

    public void updateColorRange(ColorRange colorRange) {
        this.colorRange = colorRange;
    }

    public void updateNavigationColor(NavigationColor navigationColor) {
        this.navigationColor = navigationColor;
    }

    public void updateAccentColor(AccentRange accentColorRange) {
        this.accentColorRange = accentColorRange;
    }

    @SuppressLint("CommitPrefEdits")
    private void saveThemeValues(final String key, final String name) {
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(key, name);
        edit.commit();
    }

    public void saveThemeSettings() {
        saveThemeValues(UIDHelper.COLOR_RANGE, colorRange.name());
        saveThemeValues(UIDHelper.NAVIGATION_RANGE, navigationColor.name());
        saveThemeValues(UIDHelper.CONTENT_TONAL_RANGE, contentColor.name());
        saveThemeValues(UIDHelper.ACCENT_RANGE, accentColorRange.name());
    }

    private void updateColorFromPref() {
        final ThemeHelper themeHelper = new ThemeHelper(defaultSharedPreferences, this);
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        accentColorRange = themeHelper.initAccentRange();
    }

    public ThemeConfiguration getThemeConfig() {
        updateColorFromPref();
        return new ThemeConfiguration(this, colorRange, navigationColor, contentColor, accentColorRange);
    }

    public void openThemeScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new ThemeSettingsFragment()).commit();
    }
}