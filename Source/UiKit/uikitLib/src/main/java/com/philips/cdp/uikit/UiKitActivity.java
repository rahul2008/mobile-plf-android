package com.philips.cdp.uikit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.utils.UikitResources;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitActivity extends AppCompatActivity {

    private TextView titleText;

    private Resources uikitResources;

    @Override
    public Resources getResources() {
        if (uikitResources == null) {
            uikitResources = new UikitResources(super.getResources());
        }
        return uikitResources;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
        if (uikitResources != null) {
            // The real (and thus managed) resources object was already updated
            // by ResourcesManager, so pull the current metrics from there.
            final DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            uikitResources.updateConfiguration(newConfig, newMetrics);
        }
    }


    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFontIconLib();
        setActionBarDefault(getSupportActionBar());
    }

    private void setActionBarDefault(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.uikit_default_action_bar);
            titleText = (TextView) findViewById(R.id.defaultActionBarText);
            titleText.setText(getActivityTitle());
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/centralesansbook.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (validateHamburger()) {
            DrawerLayout philipsDrawerLayout = setLogoAlpha();
            configureStatusBarViews();
            philipsDrawerLayout.setScrimColor(Color.TRANSPARENT);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to imageView.setAlpha(): sticking with deprecated API for now
    private DrawerLayout setLogoAlpha() {
        ImageView vectorDrawableImageView = (ImageView) findViewById(R.id.philips_logo);
        DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        if (vectorDrawableImageView != null)
            vectorDrawableImageView.setAlpha(229);
        return philipsDrawerLayout;
    }

    private void configureStatusBarViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private boolean validateHamburger() {
        return findViewById(R.id.philips_drawer_layout) != null;
    }

    private void initFontIconLib() {
        try {
            FontIconTypefaceHolder.getTypeface();

        } catch (IllegalStateException e) {
            FontIconTypefaceHolder.init(getAssets(), "fonts/puicon.ttf");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (titleText != null)
            titleText.setText(title);
        else
            super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        if (titleText != null)
            titleText.setText(titleId);
        else
            super.setTitle(titleId);
    }

    private String getActivityTitle() {
        String title = "";
        try {
            PackageManager PM = getApplicationContext().getPackageManager();
            PackageInfo PI = getPackageManager().getPackageInfo(getPackageName(), 0);
            title = PI.applicationInfo.loadLabel(PM).toString();
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return title;
    }
}
