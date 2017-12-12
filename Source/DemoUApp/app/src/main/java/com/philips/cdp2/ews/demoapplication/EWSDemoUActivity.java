/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.demoapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.philips.cdp2.ews.demoapplication.microapp.UAppDependencyHelper;
import com.philips.cdp2.ews.demoapplication.themesettinngs.ThemeHelper;
import com.philips.cdp2.ews.demoapplication.themesettinngs.ThemeSettingsFragment;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.ActionBarTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EWSDemoUActivity extends UIDActivity implements EWSActionBarListener {

    private final int TOOLBAR_UPDATE_TIMER = 100;
    private SharedPreferences defaultSharedPreferences;
    private OptionSelectionFragment optionSelectionFragment;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private ContentColor contentColor = ContentColor.VERY_DARK;
    private AccentRange accentColorRange = AccentRange.ORANGE;
    private NavigationColor navigationColor = NavigationColor.VERY_DARK;
    private EWSLauncherInput ewsLauncherInput;
    private ThemeHelper themeHelper;
    private ImageView closeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeHelper = new ThemeHelper(defaultSharedPreferences);
        updateColorFromPref();
        initTheme(UAppDependencyHelper.getThemeConfiguration());
        injectNewTheme(colorRange, contentColor, navigationColor, accentColorRange);
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeImageView = findViewById(R.id.ic_close);
        ewsLauncherInput = new EWSLauncherInput();
        this.optionSelectionFragment = new OptionSelectionFragment();
        showConfigurationOptScreen();
        setUpToolBar();
    }

    private void initTheme(ThemeConfiguration themeConfig) {
        if (themeConfig != null) {
            for (ThemeConfig config : themeConfig.getConfigurations()) {
                if (config instanceof ColorRange) {
                    colorRange = (ColorRange) config;
                } else if (config instanceof ContentColor) {
                    contentColor = (ContentColor) config;
                } else if (config instanceof AccentRange) {
                    accentColorRange = (AccentRange) config;
                } else if (config instanceof NavigationColor) {
                    navigationColor = (NavigationColor) config;
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public EWSLauncherInput getEwsLauncherInput() {
        return ewsLauncherInput;
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
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ewsLauncherInput != null) {
                    ewsLauncherInput.handleCloseButtonClick();
                }
            }
        });
    }

    private void hideShowImageView() {
        if (getCurrentFragment() instanceof OptionSelectionFragment || getCurrentFragment() instanceof ThemeSettingsFragment) {
            closeImageView.setVisibility(View.GONE);
        } else {
            closeImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateMenuOption(Menu menu) {
        if (isThemeScreen()) {
            menu.findItem(R.id.menu_set_theme_settings).setVisible(true);
        } else {
            menu.findItem(R.id.menu_set_theme_settings).setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuOption(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.mainContainer);
    }

    private boolean isThemeScreen() {
        return getCurrentFragment() instanceof ThemeSettingsFragment;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_set_theme_settings) {
            saveThemeSettings();
            restartActivity();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showConfigurationOptScreen() {
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, optionSelectionFragment).commit();
        updateActionBar();
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
        edit.apply();
    }

    public void saveThemeSettings() {
        saveThemeValues(UIDHelper.COLOR_RANGE, colorRange.name());
        saveThemeValues(UIDHelper.NAVIGATION_RANGE, navigationColor.name());
        saveThemeValues(UIDHelper.CONTENT_TONAL_RANGE, contentColor.name());
        saveThemeValues(UIDHelper.ACCENT_RANGE, accentColorRange.name());
    }


    private void updateColorFromPref() {
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
        fragmentReplace(new ThemeSettingsFragment());
    }

    private void fragmentReplace(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, newFragment).addToBackStack(newFragment.getClass().getName()).commit();
        updateActionBar();
    }

    private void updateActionBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
                hideShowImageView();
                isThemeScreen();
            }
        }, TOOLBAR_UPDATE_TIMER);
    }

    @Override
    public void closeButton(boolean isVisible) {
        findViewById(R.id.ic_close).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = findViewById(R.id.ews_toolbar);
        ((ActionBarTextView) toolbar.findViewById(R.id.toolbar_title)).setText(title);
    }

    @Override
    public void updateActionBar(int stringResId, boolean b) {
        setToolbarTitle(getString(stringResId));
    }

    @Override
    public void updateActionBar(String title, boolean b) {
        setToolbarTitle(title);
    }

    private void restartActivity() {
        injectNewTheme(colorRange, contentColor, navigationColor, accentColorRange);
        Intent intent = new Intent(this, EWSDemoUActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void injectNewTheme(ColorRange colorRange, ContentColor contentColor, NavigationColor navigationColor, AccentRange accentRange) {
        if (colorRange != null) {
            this.colorRange = colorRange;
        }
        if (contentColor != null) {
            this.contentColor = contentColor;
        }
        if (navigationColor != null) {
            this.navigationColor = navigationColor;
        }
        if (accentRange != null) {
            this.accentColorRange = accentRange;
        }

        String themeName = String.format("Theme.DLS.%s.%s", this.colorRange.getThemeName(), this.contentColor.getThemeName());
        getTheme().applyStyle(getResources().getIdentifier(themeName, "style", getPackageName()), true);
        ThemeConfiguration themeConfiguration = new ThemeConfiguration(this, this.colorRange, this.contentColor, this.navigationColor, this.accentColorRange);
        UIDHelper.init(themeConfiguration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateActionBar();
    }
}