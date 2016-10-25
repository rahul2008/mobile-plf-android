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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.philips.platform.catalogapp.fragments.ComponentListFragment;
import com.philips.platform.catalogapp.themesettings.PreviewActivity;
import com.philips.platform.catalogapp.themesettings.ThemeHelper;
import com.philips.platform.catalogapp.themesettings.ThemeSettingsFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String HAMBURGER_BUTTON_DISPLAYED = "HAMBURGER_BUTTON_DISPLAYED";
    private static final String THEMESETTINGS_BUTTON_DISPLAYED = "THEMESETTINGS_BUTTON_DISPLAYED";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeHelper = new ThemeHelper(defaultSharedPreferences);

        UITHelper.init(getThemeConfig());
        if (BuildConfig.DEBUG) {
            Log.d(MainActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                    contentColor, colorRange, navigationColor));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbar.setTitle(R.string.catalog_app_name);

        initSetThemeSettings(toolbar);

        initThemeSettingsIcon(toolbar);

        initBackButton();
        supportFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            setSupportActionBar(toolbar);

            initDemoListFragment();
        } else {
            initIconState(savedInstanceState);
            processBackButton();
            if (hamburgerIconVisible) {
                showHamburgerIcon();
            }

            if (themeSettingsIconVisible) {
                toggle(themeSettingsIcon, setThemeTextView);
            } else {
                toggle(setThemeTextView, themeSettingsIcon);
            }
        }
    }

    private void initBackButton() {
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (hasBackStack()) {
                    onBackPressed();
                } else {
                    Snackbar.make(view, "Hamburger is not ready yet", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(TonalRangeChangedEvent event) {
        Log.i("Event", "Settings changed" + event.getMessage());
        contentColor = event.getContentColor();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ColorRangeChangedEvent event) {
        Log.i("Event", "Settings changed" + event.getMessage());
        colorRange = event.getColorRange();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(NavigationColorChangedEvent event) {
        navigationColor = event.getNavigationColor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initSetThemeSettings(final Toolbar toolbar) {
        setThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                saveThemeValues(UITHelper.COLOR_RANGE, colorRange.name());
                saveThemeValues(UITHelper.NAVIGATION_RANGE, navigationColor.name());
                saveThemeValues(UITHelper.CONTENT_TONAL_RANGE, contentColor.name());

                restartActivity();
            }
        });
    }

    void restartActivity() {
        startActivity(new Intent(this, com.philips.platform.catalogapp.MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.overridePendingTransition(0, 0);
        startActivity(new Intent(this, PreviewActivity.class));
    }

    private void initThemeSettingsIcon(final Toolbar toolbar) {
        themeSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                loadThemeSettingsPage();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initIconState(savedInstanceState);
    }

    private void initIconState(final Bundle savedInstanceState) {
        hamburgerIconVisible = savedInstanceState.getBoolean(HAMBURGER_BUTTON_DISPLAYED);
        themeSettingsIconVisible = savedInstanceState.getBoolean(THEMESETTINGS_BUTTON_DISPLAYED);
    }

    private void initDemoListFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(R.id.mainContainer, new ComponentListFragment());
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
        themeSettingsIconVisible(true);
    }

    private void themeSettingsIconVisible(final boolean visible) {
        themeSettingsIconVisible = visible;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(HAMBURGER_BUTTON_DISPLAYED, hamburgerIconVisible);
        outState.putBoolean(THEMESETTINGS_BUTTON_DISPLAYED, themeSettingsIconVisible);

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

    public boolean switchFragment(BaseFragment fragment) {

        final List<Fragment> fragments = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragmentFromList : fragments) {
                if (fragmentFromList instanceof ThemeSettingsFragment && fragment instanceof ThemeSettingsFragment) {
                    return false;
                }
            }
        }
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
        toggleHamburgerIcon();
        setTitle(fragment.getPageTitle());
        return true;
    }

    @Override
    public void setTitle(final int titleId) {
        title.setText(titleId);
    }

    private void toggleHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_back_icon);
        hamburgerIconVisible = false;
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void loadThemeSettingsPage() {
        final ThemeSettingsFragment themeSettingsFragment = new ThemeSettingsFragment();
        final boolean switchedFragment = switchFragment(themeSettingsFragment);
        if (switchedFragment) {
            setTitle(R.string.page_tittle_theme_settings);
            toggle(setThemeTextView, themeSettingsIcon);
            themeSettingsIconVisible(false);
        }
    }

    private void toggle(final View visibleView, final View goneView) {
        if (visibleView != null) {
            visibleView.setVisibility(View.VISIBLE);
        }
        if (goneView != null) {
            goneView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        processBackButton();
    }

    private void processBackButton() {
        if (hasBackStack()) {
            final Fragment fragmentAtTopOfBackStack = getFragmentAtTopOfBackStack();
            if (!(fragmentAtTopOfBackStack instanceof ThemeSettingsFragment)) {
                toggle(themeSettingsIcon, setThemeTextView);
                toggleHamburgerIcon();
            } else {
                showHamburgerIcon();
            }
        } else if (supportFragmentManager != null && supportFragmentManager.getBackStackEntryCount() == 0) {
            showHamburgerIcon();
        }
    }

    private void showHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_hamburger_menu);
        hamburgerIconVisible = true;
        title.setText(R.string.catalog_app_name);
        toggle(themeSettingsIcon, setThemeTextView);
    }

    private boolean hasBackStack() {
        return supportFragmentManager != null && supportFragmentManager.getBackStackEntryCount() > 0;
    }

    @NonNull
    private Fragment getFragmentAtTopOfBackStack() {
        return getFragmentAtBackStackIndex(supportFragmentManager.getBackStackEntryCount() - 1);
    }

    @NonNull
    private Fragment getFragmentAtBackStackIndex(final int index) {
        final FragmentManager.BackStackEntry backStackEntry = supportFragmentManager.getBackStackEntryAt(index);
        final String name = backStackEntry.getName();
        return supportFragmentManager.findFragmentByTag(name);
    }
}
