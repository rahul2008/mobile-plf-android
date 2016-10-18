/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.hamburger)
    ImageView hamburgerIcon;

    @Bind(R.id.theme_settings)
    ImageView themeSettingsIcon;

    @Bind(R.id.set_theme_settings)
    TextView setThemeTextView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ContentColor contentColor;
    private ColorRange colorRange;
    private FragmentManager supportFragmentManager;
    private NavigationColor navigationColor;
    private ThemeHelper themeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(this));

        UITHelper.init(getThemeConfig());
        if (BuildConfig.DEBUG) {
            Log.d(MainActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                    contentColor, colorRange, navigationColor));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbar.setTitle(R.string.catalog_app_name);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.catalog_app_name);

        initSetThemeSettings(toolbar);

        initThemeSettingsIcon(toolbar);

        initBackButton();
        if (savedInstanceState == null) {
            setSupportActionBar(toolbar);

            initDemoListFragment();
        }
    }

    private void initBackButton() {
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (hasBackStack()) {
                    supportFragmentManager.popBackStack();
                    processBackButton();
                } else {
                    Snackbar.make(view, "Hamburger not implemented ", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSetThemeSettings(final Toolbar toolbar) {
        setThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                colorRange = themeHelper.initColorRange();
                navigationColor = themeHelper.initNavigationRange();
                contentColor = themeHelper.initTonalRange();
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

    private void initDemoListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainContainer, new ComponentListFragment());
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
    }

    public ThemeConfiguration getThemeConfig() {
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initTonalRange();
        return new ThemeConfiguration(colorRange, contentColor, navigationColor, this);
    }

    public boolean switchFragment(BaseFragment fragment) {
        supportFragmentManager = getSupportFragmentManager();
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

    private void toggleHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_back_icon);
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void loadThemeSettingsPage() {
        final boolean switchedFragment = switchFragment(new ThemeSettingsFragment());
        if (switchedFragment) {
            setTitle(R.string.page_tittle_theme_settings);
            toggle(setThemeTextView, themeSettingsIcon);
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
        processBackButton();
        super.onBackPressed();
    }

    private void processBackButton() {
        if (hasBackStack()) {
            final Fragment fragmentAtTopOfBackStack = getFragmentAtTopOfBackStack();
            if (!(fragmentAtTopOfBackStack instanceof ThemeSettingsFragment)) {
                toggle(themeSettingsIcon, setThemeTextView);
                showHamburgerIcon();
            }
        } else {
            showHamburgerIcon();
        }
    }

    private void showHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_hamburger_menu);
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
