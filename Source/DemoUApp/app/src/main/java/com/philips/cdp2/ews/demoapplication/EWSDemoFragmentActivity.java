/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.demoapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class EWSDemoFragmentActivity extends EWSDemoBaseActivity implements EWSActionBarListener {

    private EWSLauncherInput ewsLauncherInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(com.philips.cdp2.ews.R.style.Theme_DLS_GroupBlue_UltraLight);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT,
                ContentColor.ULTRA_LIGHT));
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_demo);
        launchEWSFragmentUApp();
        setUpToolBar();
        setUpCancelButton();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(com.philips.cdp2.ews.R.id.ews_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }
    private void setUpCancelButton() {
        FontIconDrawable drawable = new FontIconDrawable(this, getResources().getString(R.string.dls_cross_24), TypefaceUtils.load(getAssets(), "fonts/iconfont.ttf"))
                .sizeRes(R.dimen.ews_gs_icon_size);
        findViewById(R.id.ic_close).setBackground(drawable);
        findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ewsLauncherInput != null) {
                    ewsLauncherInput.handleCloseButtonClick();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void launchEWSFragmentUApp() {
        String selectedOption = getIntent().getExtras().getString(SELECTED_CONFIG);
        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap(), isDefaultValueSelected(selectedOption)), new UappSettings(getApplicationContext()));

        ewsLauncherInput = new EWSLauncherInput();
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this, R.id.fragmentContainerDemo, this);
        ewsInterface.launch(fragmentLauncher, ewsLauncherInput);
    }

    public void setToolbarTitle(String s) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.ews_toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(s);
    }

    @Override
    public void updateActionBar(int i, boolean b) {
        setToolbarTitle(getString(i));
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        setToolbarTitle(s);
    }

    @Override
    public void closeButton(boolean visibility) {
        findViewById(R.id.ic_close).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
