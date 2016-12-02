package com.philips.platform.catalogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.catalogapp.fragments.ComponentListFragment;
import com.philips.platform.catalogapp.themesettings.ThemeSettingsFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationController {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager supportFragmentManager;
    private ActionBar actionBar;
    private MainActivity mainActivity;
    private MenuItem themeSetting;
    private MenuItem setTheme;
    SharedPreferences fragmentPreference;

    public NavigationController(final MainActivity mainActivity, final Intent intent) {
        this.mainActivity = mainActivity;
        fragmentPreference = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        clearLastFragmentOnFreshLaunch(intent);
    }

    protected void processBackButton() {
        if (hasBackStack()) {
            final Fragment fragmentAtTopOfBackStack = getFragmentAtTopOfBackStack();
            if (!(fragmentAtTopOfBackStack instanceof ThemeSettingsFragment)) {
                toggleHamburgerIcon();
            } else {
                showHamburgerIcon();
                storeFragmentInPreference(null);
            }
        } else if (supportFragmentManager != null && supportFragmentManager.getBackStackEntryCount() == 0) {
            showHamburgerIcon();
        }
    }

    private void clearLastFragmentOnFreshLaunch(Intent intent) {
        if (intent != null && !intent.hasExtra(MainActivity.THEMESETTINGS_ACTIVITY_RESTART)) {
            storeFragmentInPreference(null);
        }
    }

    private void toggleHamburgerIcon() {
        toolbar.setNavigationIcon(VectorDrawableCompat.create(mainActivity.getResources(), R.drawable.ic_back_icon, mainActivity.getTheme()));
        mainActivity.hamburgerIconVisible = false;
    }

    protected void initDemoListFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(R.id.mainContainer, new ComponentListFragment());
        transaction.commit();

        themeSettingsIconVisible(true);
    }

    private void themeSettingsIconVisible(final boolean visible) {
        mainActivity.themeSettingsIconVisible = visible;
    }

    public void loadThemeSettingsPage() {
        final ThemeSettingsFragment themeSettingsFragment = new ThemeSettingsFragment();
        final boolean switchedFragment = switchFragment(themeSettingsFragment);
        if (switchedFragment) {
            mainActivity.setTitle(R.string.page_title_theme_settings);

            themeSettingsIconVisible(false);
        }
    }

    protected void showUiFromPreviousState(final Bundle savedInstanceState) {
        mainActivity.initIconState(savedInstanceState);
        processBackButton();
        if (mainActivity.hamburgerIconVisible) {
            showHamburgerIcon();
        }

        final int titleResourceId = savedInstanceState.getInt(MainActivity.TITLE_TEXT, -1);
        initTitle(titleResourceId);
    }

    private void initTitle(final int titleResourceId) {
        if (titleResourceId != -1) {
            mainActivity.setTitle(titleResourceId);
        }
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

        String tag = fragment.getClass().getName();
        if (!(fragment instanceof ThemeSettingsFragment)) {
            storeFragmentInPreference(tag);
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
        toggleHamburgerIcon();
        toolbar.setTitle(fragment.getPageTitle());
        return true;
    }

    private void showHamburgerIcon() {
        toolbar.setNavigationIcon(VectorDrawableCompat.create(mainActivity.getResources(), R.drawable.ic_hamburger_menu, mainActivity.getTheme()));
        mainActivity.hamburgerIconVisible = true;
        toolbar.setTitle(R.string.catalog_app_name);
        toolbar.setTitleMarginStart(mainActivity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        toolbar.setTitleMarginEnd(mainActivity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        mainActivity.titleText = R.string.catalog_app_name;
    }

    protected boolean hasBackStack() {
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

    public void init(final Bundle savedInstanceState) {
        this.supportFragmentManager = mainActivity.getSupportFragmentManager();
        ButterKnife.bind(this, mainActivity);
        mainActivity.setSupportActionBar(toolbar);

        final Resources resources = mainActivity.getResources();
        toolbar.setTitleMargin(resources.getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right), toolbar.getTitleMarginTop(), resources.getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right), toolbar.getTitleMarginBottom());
        actionBar = mainActivity.getSupportActionBar();
        if (savedInstanceState == null) {
            initDemoListFragment();
            showHamburgerIcon();

            restoreLastActiveFragment();
        } else {
            showUiFromPreviousState(savedInstanceState);
        }
    }

    public void onCreateOptionsMenu(final Menu menu, final MainActivity mainActivity) {
        mainActivity.getMenuInflater().inflate(R.menu.catalog_menu, menu);
    }

    public void onPrepareOptionsMenu(final Menu menu, final MainActivity mainActivity) {
        themeSetting = menu.findItem(R.id.menu_theme_settings);
        setTheme = menu.findItem(R.id.menu_set_theme_settings);
    }

    public void setTitleText(final int titleId) {
        toolbar.setTitle(titleId);
    }

    //Needed only untill we have hamburger
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void storeFragmentInPreference(String fragmentTag) {
        fragmentPreference.edit().putString("activeFragment", fragmentTag).commit();
    }

    private String getLastActiveFragmentName() {
        return fragmentPreference.getString("activeFragment", null);
    }

    private void restoreLastActiveFragment() {
        if (getLastActiveFragmentName() != null) {
            String fragmentName = getLastActiveFragmentName();
            try {
                Class<BaseFragment> fragmentClass = (Class<BaseFragment>) Class.forName(fragmentName);
                switchFragment(fragmentClass.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
