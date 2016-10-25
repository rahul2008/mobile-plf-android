/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.events.ColorRangeChangedEvent;
import com.philips.platform.catalogapp.events.NavigationColorChangedEvent;
import com.philips.platform.catalogapp.events.TonalRangeChangedEvent;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.catalogapp.themesettings.PreviewActivity;
import com.philips.platform.catalogapp.themesettings.ThemeHelper;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String HAMBURGER_BUTTON_DISPLAYED = "HAMBURGER_BUTTON_DISPLAYED";
    private static final String THEMESETTINGS_BUTTON_DISPLAYED = "THEMESETTINGS_BUTTON_DISPLAYED";
    protected static final String TITLE_TEXT = "TITLE_TEXT";
    @Bind(R.id.hamburger)
    ImageView hamburgerIcon;

    @Bind(R.id.theme_settings)
    ImageView themeSettingsIcon;

    @Bind(R.id.set_theme_settings)
    TextView setThemeTextView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView title;

    private ContentColor contentColor;
    private ColorRange colorRange;
    private FragmentManager supportFragmentManager;
    private NavigationColor navigationColor;
    private ThemeHelper themeHelper;
    private SharedPreferences defaultSharedPreferences;
    private boolean hamburgerIconVisible;
    private boolean themeSettingsIconVisible;

    private int titleText;

    private NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeHelper = new ThemeHelper(defaultSharedPreferences);

        UITHelper.init(getThemeConfig());
        if (BuildConfig.DEBUG) {
            Log.d(MainActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                    contentColor, colorRange, navigationColor));
        }
        EventBus.getDefault().register(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbar.setTitle(R.string.catalog_app_name);
        supportFragmentManager = getSupportFragmentManager();
        navigationController = new NavigationController(themeSettingsIcon, setThemeTextView, supportFragmentManager,
                hamburgerIcon, hamburgerIconVisible, themeSettingsIconVisible, this, title, titleText);
        navigationController.initSetThemeSettings(toolbar);

        navigationController.initThemeSettingsIcon(toolbar);

        initBackButton();
        if (savedInstanceState == null) {
            setSupportActionBar(toolbar);

            navigationController.initDemoListFragment();
        } else {
            navigationController.showUiFromPreviousState(savedInstanceState);
        }
    }

    private void initBackButton() {
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (navigationController.hasBackStack()) {
                    onBackPressed();
                } else {
                    Snackbar.make(view, "Hamburger is not ready yet", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(TonalRangeChangedEvent event) {
        Log.i("Event", "Settings changed" + event.getMessage());
        contentColor = event.getContentColor();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(ColorRangeChangedEvent event) {
        Log.i("Event", "Settings changed" + event.getMessage());
        colorRange = event.getColorRange();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(NavigationColorChangedEvent event) {
        Log.i("Event", "Settings changed" + event.getMessage());
        navigationColor = event.getNavigationColor();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void restartActivity() {
        startActivity(new Intent(this, com.philips.platform.catalogapp.MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.overridePendingTransition(0, 0);
        startActivity(new Intent(this, PreviewActivity.class));
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initIconState(savedInstanceState);
    }

    protected void initIconState(final Bundle savedInstanceState) {
        hamburgerIconVisible = savedInstanceState.getBoolean(HAMBURGER_BUTTON_DISPLAYED);
        themeSettingsIconVisible = savedInstanceState.getBoolean(THEMESETTINGS_BUTTON_DISPLAYED);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(HAMBURGER_BUTTON_DISPLAYED, hamburgerIconVisible);
        outState.putBoolean(THEMESETTINGS_BUTTON_DISPLAYED, themeSettingsIconVisible);
        outState.putInt(TITLE_TEXT, titleText);
        super.onSaveInstanceState(outState);
    }

    public ThemeConfiguration getThemeConfig() {
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        return new ThemeConfiguration(colorRange, contentColor, navigationColor, this);
    }

    private void saveThemeValues(final String key, final String name) {
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(key, name);
        edit.commit();
    }

    @Override
    public void setTitle(final int titleId) {
        title.setText(titleId);
        titleText = titleId;
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigationController.processBackButton();
    }

    public void showThemeSettingsIcon() {
        navigationController.toggle(themeSettingsIcon, setThemeTextView);
    }

    public void saveThemeSettings() {
        saveThemeValues(UITHelper.COLOR_RANGE, colorRange.name());
        saveThemeValues(UITHelper.NAVIGATION_RANGE, navigationColor.name());
        saveThemeValues(UITHelper.CONTENT_TONAL_RANGE, contentColor.name());
    }

    public void showFragment(final BaseFragment fragment) {
        navigationController.switchFragment(fragment);
    }
}
