/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseTestActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UIDHelper.injectCalligraphyFonts();
        UIDHelper.init(getThemeConfig());
        super.onCreate(savedInstanceState);
    }

    private ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, this);
    }

    public void switchTo(final int layout) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        setContentView(layout);
                        if (layout == com.philips.platform.uid.test.R.layout.main_layout) {
                            toolbar = (Toolbar) findViewById(com.philips.platform.uid.test.R.id.uid_toolbar);
                            toolbar.setNavigationContentDescription("navigationIcon");
                            toolbar.setNavigationIcon(com.philips.platform.uid.test.R.drawable.ic_back_icon);
//                            toolbar.setTitleTextAppearance(getApplicationContext(), com.philips.platform.uid.test.R.style.UIDTitleText);
                            toolbar.setTitleMarginStart(getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.uid_navigation_bar_title_margin_left_right));
                            toolbar.setTitleMarginEnd(getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.uid_navigation_bar_title_margin_left_right));
                            toolbar.setTitle(com.philips.platform.uid.test.R.string.catalog_app_name);
                            setSupportActionBar(toolbar);
                        }
                    }
                }
        );
    }

    public void switchFragment(final Fragment fragment) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(com.philips.platform.uid.test.R.id.container, fragment);
                        fragmentTransaction.commitNowAllowingStateLoss();
                    }
                });
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(com.philips.platform.uid.test.R.menu.notificationbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        return true;
    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(com.philips.platform.uid.test.R.id.uid_toolbar);
    }
}
