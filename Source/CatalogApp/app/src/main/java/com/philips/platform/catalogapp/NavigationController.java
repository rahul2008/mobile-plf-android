package com.philips.platform.catalogapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.catalogapp.fragments.ComponentListFragment;
import com.philips.platform.catalogapp.themesettings.ThemeSettingsFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationController {

    private FragmentManager supportFragmentManager;

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

    private MainActivity mainActivity;

    public NavigationController(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    protected void processBackButton() {
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

    private void toggleHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_back_icon);
        mainActivity.hamburgerIconVisible = false;
    }

    protected void initDemoListFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(R.id.mainContainer, new ComponentListFragment());
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
        themeSettingsIconVisible(true);
    }

    private void themeSettingsIconVisible(final boolean visible) {
        mainActivity.themeSettingsIconVisible = visible;
    }

    protected void initSetThemeSettings(final Toolbar toolbar) {
        setThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mainActivity.saveThemeSettings();

                mainActivity.restartActivity();
            }
        });
    }

    private void loadThemeSettingsPage() {
        final ThemeSettingsFragment themeSettingsFragment = new ThemeSettingsFragment();
        final boolean switchedFragment = switchFragment(themeSettingsFragment);
        if (switchedFragment) {
            mainActivity.setTitle(R.string.page_tittle_theme_settings);
            toggle(setThemeTextView, themeSettingsIcon);
            themeSettingsIconVisible(false);
        }
    }

    protected void showUiFromPreviousState(final Bundle savedInstanceState) {
        mainActivity.initIconState(savedInstanceState);
        processBackButton();
        if (mainActivity.hamburgerIconVisible) {
            showHamburgerIcon();
        }

        if (mainActivity.themeSettingsIconVisible) {
            toggle(themeSettingsIcon, setThemeTextView);
        } else {
            toggle(setThemeTextView, themeSettingsIcon);
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
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
        toggleHamburgerIcon();
        mainActivity.setTitle(fragment.getPageTitle());
        return true;
    }

    private void showHamburgerIcon() {
        hamburgerIcon.setImageResource(R.drawable.ic_hamburger_menu);
        mainActivity.hamburgerIconVisible = true;
        title.setText(R.string.catalog_app_name);
        mainActivity.titleText = R.string.catalog_app_name;
        toggle(themeSettingsIcon, setThemeTextView);
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

    protected void toggle(final View visibleView, final View goneView) {
        if (visibleView != null) {
            visibleView.setVisibility(View.VISIBLE);
        }
        if (goneView != null) {
            goneView.setVisibility(View.GONE);
        }
    }

    protected void initThemeSettingsIcon(final Toolbar toolbar) {
        themeSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                loadThemeSettingsPage();
            }
        });
    }

    public void init(final Bundle savedInstanceState) {
        this.supportFragmentManager = mainActivity.getSupportFragmentManager();
        ButterKnife.bind(this, mainActivity);
        initSetThemeSettings(toolbar);

        initThemeSettingsIcon(toolbar);
        if (savedInstanceState == null) {
            mainActivity.setSupportActionBar(toolbar);

            initDemoListFragment();
            mainActivity.setTitle(R.string.catalog_app_name);
        } else {
            showUiFromPreviousState(savedInstanceState);
        }
        initBackButton();
    }

    private void initBackButton() {
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (hasBackStack()) {
                    mainActivity.onBackPressed();
                    processBackButton();
                } else {
                    Snackbar.make(view, "Hamburger is not ready yet", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTitleText(final int titleId) {
        title.setText(titleId);
    }

    public void showThemeSettings() {
        toggle(themeSettingsIcon, setThemeTextView);
    }
}
