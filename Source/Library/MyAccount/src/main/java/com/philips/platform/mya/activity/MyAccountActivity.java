/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.mya.R;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.philips.platform.mya.util.MyaConstants.MYA_DLS_THEME;

public class MyAccountActivity extends UIDActivity implements MyaListener {

    private TextView mTitle;
    private String applicationName;
    private String propositionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.injectCalligraphyFonts();
        initDLSThemeIfExists();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mya_myaccounts_activity);
        fetchConsentData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.mya_toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        launchTabFragment();
    }

    private void fetchConsentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            applicationName = bundle.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = bundle.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }
    }

    private void launchTabFragment() {
        MyaInterface myaInterface = new MyaInterface();
        myaInterface.init(new MyaDependencies(MyaInterface.getMyaDependencyComponent().getAppInfra()), new MyaSettings(this));
        myaInterface.launch(new FragmentLauncher(this, R.id.fragmentPlaceHolder, new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean b) {
                setTitle(i);
            }

            @Override
            public void updateActionBar(String s, boolean b) {
                setTitle(s);
            }
        }), new MyaLaunchInput(this, this));
    }

    @Override
    public boolean onClickMyaItem(String itemName) {
        return false;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    public void initDLSThemeIfExists() {
        ThemeConfiguration themeConfiguration = MyaInterface.getMyaUiComponent().getThemeConfiguration();
        if (getIntent().getExtras() != null && themeConfiguration !=null) {
            Bundle extras = getIntent().getExtras();
            UIDHelper.init(themeConfiguration);
            getTheme().applyStyle(extras.getInt(MYA_DLS_THEME), true);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(MyaInterface.getMyaUiComponent().getFragmentLauncher().getParentContainerResourceID());
        if (fragment != null && fragment instanceof BackEventListener) {
            boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
            if (!isConsumed) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        applicationName = state.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        propositionName = state.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        state.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
