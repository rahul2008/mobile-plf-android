/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import com.philips.platform.uid.thememanager.*;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.view.widget.SideBar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.*;
import java.util.Locale;

public class BaseTestActivity extends UIDActivity implements DelayerCallback {
    public static final String CONTENT_COLOR_KEY = "ContentColor";
    public static final String NAVIGATION_COLOR_KEY = "NavigationColor";
    public static final String COLOR_RANGE_KEY = "ColorRange";

    private Toolbar toolbar;
    private SideBar sideBar;
    private ActionBarDrawerToggle drawerToggle;
    @Nullable
    private UidIdlingResource mIdlingResource;

    @StyleRes
    static int getColorResourceId(final Resources resources, final String colorRange, final String tonalRange, final String packageName) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange), toCamelCase(tonalRange));

        return resources.getIdentifier(themeName, "style", packageName);
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
        return s.substring(0, 1).toUpperCase(Locale.getDefault()) +
                s.substring(1).toLowerCase(Locale.getDefault());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int navigationColor = NavigationColor.BRIGHT.ordinal();
        int contentColor = ContentColor.ULTRA_LIGHT.ordinal();
        int colorRange = ColorRange.GROUP_BLUE.ordinal();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getIntent() != null && getIntent().getExtras() != null) {
            final Bundle extras = getIntent().getExtras();
            navigationColor = extras.getInt(NAVIGATION_COLOR_KEY, 1);
            contentColor = extras.getInt(CONTENT_COLOR_KEY, 0);
            colorRange = extras.getInt(COLOR_RANGE_KEY, 0);
        }

        setTheme(getThemeResourceId(ContentColor.values()[contentColor], ColorRange.values()[colorRange]));
        UIDHelper.injectCalligraphyFonts();
        UIDLocaleHelper.getInstance().setFilePath(getCatalogAppJSONAssetPath());
        UIDHelper.init(getThemeConfig(navigationColor, contentColor, colorRange));

        super.onCreate(savedInstanceState);
    }

    private ThemeConfiguration getThemeConfig(final int navigationColor, final int contentColor, final int colorRange) {
        return new ThemeConfiguration(this, ColorRange.values()[colorRange], ContentColor.values()[contentColor], NavigationColor.values()[navigationColor], AccentRange.PURPLE);
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
                            sideBar = (SideBar) findViewById(com.philips.platform.uid.test.R.id.test_sidebar);
                            drawerToggle = setupDrawerToggle();

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
                        fragmentTransaction.commitAllowingStateLoss();
                        sendMessage();
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
        sendMessage();
        return true;
    }

    private void sendMessage() {
        MessageDelayer.sendMessage("Hello", this, mIdlingResource);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        return true;
    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(com.philips.platform.uid.test.R.id.uid_toolbar);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, sideBar, toolbar, com.philips.platform.uid.test.R.string.test_sidebar_open,  com.philips.platform.uid.test.R.string.test_sidebar_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new UidIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onDone(final String text) {
        //Do nothing
    }

    private
    @StyleRes
    int getThemeResourceId(ContentColor contentColor, ColorRange colorRange) {
        int colorResourceId = getColorResourceId(getResources(), colorRange.name(), contentColor.name(), getPackageName());
        return colorResourceId;
    }

    public String getCatalogAppJSONAssetPath() {
        try {
            File f = new File(getCacheDir() + "/catalogapp.json");
            InputStream is = getAssets().open("catalogapp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            Log.e("", e.getMessage());
        } catch (IOException e) {
            Log.e("", e.getMessage());
        }
        return null;
    }

    static class MessageDelayer {

        private static final long DELAY_MILLIS = 300;

        static void sendMessage(final String message, final DelayerCallback callback,
                                @Nullable final UidIdlingResource idlingResource) {
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }

            // Delay the execution, return message via callback.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onDone(message);
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                    }
                }
            }, DELAY_MILLIS);
        }
    }
}
