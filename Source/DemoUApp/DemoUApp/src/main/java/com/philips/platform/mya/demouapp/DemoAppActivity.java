package com.philips.platform.mya.demouapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.philips.platform.mya.demouapp.fragment.LaunchFragment;
import com.philips.platform.mya.demouapp.theme.events.AccentColorChangedEvent;
import com.philips.platform.mya.demouapp.theme.events.ColorRangeChangedEvent;
import com.philips.platform.mya.demouapp.theme.events.ContentTonalRangeChangedEvent;
import com.philips.platform.mya.demouapp.theme.events.NavigationColorChangedEvent;
import com.philips.platform.mya.demouapp.theme.events.OptionMenuClickedEvent;
import com.philips.platform.mya.demouapp.theme.themesettings.NavigationController;
import com.philips.platform.mya.demouapp.theme.themesettings.ThemeHelper;
import com.philips.platform.mya.util.MYALog;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DemoAppActivity extends UIDActivity implements View.OnClickListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_Bright;

    public static final String TITLE_TEXT = "TITLE_TEXT";
    public static final String THEMESETTINGS_ACTIVITY_RESTART = "THEMESETTINGS_ACTIVITY_RESTART";

    private ContentColor contentColor;
    private ColorRange colorRange;
    private NavigationColor navigationColor;
    private NavigationController navigationController;
    private ViewDataBinding activityMainBinding;
    private SharedPreferences defaultSharedPreferences;
    private AccentRange accentColorRange;
    private  int  themeResourceId=0;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initTheme();
        //  initThemeIfExists();
        super.onCreate(savedInstanceState);
        MYALog.enableLogging(true);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        EventBus.getDefault().register(this);


        navigationController = new NavigationController(this, getIntent(), activityMainBinding);
        navigationController.init(savedInstanceState);
        if (savedInstanceState == null) {
            LaunchFragment launchFragment = new LaunchFragment();
            navigationController.switchFragment(launchFragment);


        }
    }


    public void onClick(View view) {

    }

    private void initThemeIfExists() {
        int themeIndex = getIntent().getIntExtra(MyaConstants.MYA_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));



    }

    public void initTheme() {
        final ThemeConfiguration themeConfig = getThemeConfig();
        themeResourceId = getThemeResourceId(getResources(), getPackageName(), colorRange, contentColor,navigationColor, accentColorRange);
        themeConfig.add(navigationColor);
        themeConfig.add(accentColorRange);
        setTheme(themeResourceId);
        // UIDLocaleHelper.getInstance().setFilePath(getCatalogAppJSONAssetPath());

        UIDHelper.init(themeConfig);

    }



    protected ThemeConfiguration getDLSThemeConfiguration(Context context) {
        return new ThemeConfiguration(context, ColorRange.ORANGE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
    }
    public NavigationController getNavigationController() {
        return navigationController;
    }

    @VisibleForTesting
    public void setColorRange(final ColorRange colorRange) {
        this.colorRange = colorRange;
    }

    @VisibleForTesting
    public void setContentColor(final ContentColor contentColor) {
        this.contentColor = contentColor;
    }
    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void saveThemeSettings() {
        saveThemeValues(UIDHelper.COLOR_RANGE, colorRange.name());
        saveThemeValues(UIDHelper.NAVIGATION_RANGE, navigationColor.name());
        saveThemeValues(UIDHelper.CONTENT_TONAL_RANGE, contentColor.name());
        saveThemeValues(UIDHelper.ACCENT_RANGE, accentColorRange.name());
    }
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        navigationController.onSaveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    public ThemeConfiguration getThemeConfig() {
        final ThemeHelper themeHelper = new ThemeHelper(defaultSharedPreferences);
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        accentColorRange = themeHelper.initAccentRange();
        return new ThemeConfiguration(this, colorRange, navigationColor, contentColor, accentColorRange);
    }

    @SuppressLint("CommitPrefEdits")
    private void saveThemeValues(final String key, final String name) {
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(key, name);
        edit.commit();
    }
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        navigationController.onPrepareOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        EventBus.getDefault().post(new OptionMenuClickedEvent(item.toString()));
        int i = item.getItemId();
        if (i == R.id.menu_theme_settings) {
            navigationController.loadThemeSettingsPage();

        } else if (i == R.id.menu_set_theme_settings) {
            saveThemeSettings();
            restartActivity();

        } else if (i == android.R.id.home) {
            if (navigationController.hasBackStack()) {
                onBackPressed();
            }
        }
        return true;
    }


    void restartActivity() {
        Intent intent = new Intent(this, DemoAppActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(THEMESETTINGS_ACTIVITY_RESTART, true);
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationController.initIconState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        navigationController.onCreateOptionsMenu(menu, this);
        return true;
    }

    public int getThemeResourceId()
    {
        return  themeResourceId;
    }

    public  NavigationColor getNavigationColor(){
        return navigationColor;
    }

    public AccentRange getAccentColorRange(){
        return accentColorRange;
    }

    @StyleRes
    int getThemeResourceId(Resources resources, final String packageName, final ColorRange colorRange, final ContentColor contentColor, NavigationColor navigationColor, AccentRange accentColorRange) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange.name()), toCamelCase(contentColor.name()));

        return resources.getIdentifier(themeName, "style", packageName);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(ContentTonalRangeChangedEvent event) {
        contentColor = event.getContentColor();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(ColorRangeChangedEvent event) {
        colorRange = event.getColorRange();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(NavigationColorChangedEvent event) {
        navigationColor = event.getNavigationColor();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(AccentColorChangedEvent event) {
        accentColorRange = event.getAccentRange();
    }
    static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean backState = false;
        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            if (currentFrag != null && currentFrag instanceof BackEventListener) {
                backState = ((BackEventListener) currentFrag).handleBackEvent();
            }

            if (!backState) {
                super.onBackPressed();
            }
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
