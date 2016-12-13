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
        int navigationColor = NavigationColor.BRIGHT.ordinal();
        if (getIntent() != null && getIntent().getExtras() != null) {
            final Bundle extras = getIntent().getExtras();
            navigationColor = extras.getInt("NavigationColor", 1);
        }
        UIDHelper.injectCalligraphyFonts();
        UIDHelper.init(getThemeConfig(navigationColor));
        super.onCreate(savedInstanceState);
    }

    private ThemeConfiguration getThemeConfig(final int navigationColor) {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.values()[navigationColor], this);
    }

    public void switchTo(final int layout) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        setContentView(layout);
                        if (layout == com.philips.platform.uid.test.R.layout.main_layout) {
                            toolbar = (Toolbar) findViewById(com.philips.platform.uid.R.id.uid_toolbar);
                            toolbar.setNavigationContentDescription(getString(com.philips.platform.uid.test.R.string.navigation_content_desc));
                            toolbar.setNavigationIcon(com.philips.platform.uid.test.R.drawable.ic_hamburger_menu);
                            UIDHelper.setupToolbar(BaseTestActivity.this);
                            toolbar.setTitle(getString(com.philips.platform.uid.test.R.string.catalog_app_name));
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
