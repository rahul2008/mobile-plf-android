/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class THSLaunchActivity extends UIDActivity implements ActionBarListener {

    private FragmentManager fragmentManager;
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_Bright;
    private Toolbar toolbar;
    private FragmentLauncher fragmentLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_rename_activity_test_ur);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(),R.drawable.pth_back_icon, getTheme()));
        setSupportActionBar(toolbar);
        fragmentLauncher = new FragmentLauncher(this, R.id.uappFragmentLayout, this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {

            THSBaseFragment thsBaseFragment = new THSInitFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            thsBaseFragment.setArguments(bundle);
            thsBaseFragment.setFragmentLauncher(fragmentLauncher);
            thsBaseFragment.setActionBarListener(this);
            fragmentTransaction.replace(R.id.uappFragmentLayout, thsBaseFragment, THSInitFragment.TAG).
                    addToBackStack(THSInitFragment.TAG).commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (currentFrag != null && currentFrag instanceof BackEventListener && !((BackEventListener) currentFrag).handleBackEvent()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setTitle(final CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }


     public void initTheme() {
         int themeIndex = getIntent().getIntExtra(THSConstants.KEY_ACTIVITY_THEME, DEFAULT_THEME);
         if (themeIndex <= 0) {
             themeIndex = DEFAULT_THEME;
         }
         getTheme().applyStyle(themeIndex, true);

         ColorRange colorRange = (ColorRange) getIntent().getSerializableExtra(THSConstants.KEY_COLOR_RANGE);
         NavigationColor navigationColor = (NavigationColor) getIntent().getSerializableExtra(THSConstants.KEY_NAVIGATION_COLOR);
         ContentColor contentColor = (ContentColor) getIntent().getSerializableExtra(THSConstants.KEY_CONTENT_COLOR);
         AccentRange accentRange = (AccentRange) getIntent().getSerializableExtra(THSConstants.KEY_ACCENT_RANGE);

         ThemeConfig[] configArray = getThemeConfigArray(colorRange, navigationColor, contentColor, accentRange);
         UIDHelper.init(new ThemeConfiguration(this, configArray));
     }

    private ThemeConfig[] getThemeConfigArray(ThemeConfig... themeConfigs) {
        List<ThemeConfig> configList = new ArrayList<>();
        for (ThemeConfig config : themeConfigs) {
            if (config != null) {
                configList.add(config);
            }
        }
        ThemeConfig[] configArray = new ThemeConfig[configList.size()];
        return configList.toArray(configArray);
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, getString(i));
        showBackImage(b);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        UIDHelper.setTitle(this, s);
        showBackImage(b);
    }

    private void showBackImage(boolean isVisible) {
        if (isVisible) {
            toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        } else {
            toolbar.setNavigationIcon(null);
        }

    }

}
