/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.mya.MyaUiHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.philips.platform.mya.util.MyaConstants.MYA_DLS_THEME;

public class MyAccountActivity extends UIDActivity implements MyaListener {

    private TextView mTitle;
    private MyaUiHelper myaUiHelper;
    private String applicationName;
    private String propositionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myaUiHelper = MyaUiHelper.getInstance();
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
        myaInterface.init(new MyaDependencies(myaUiHelper.getAppInfra()), null);
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
        if (getIntent().getExtras() != null && myaUiHelper.getThemeConfiguration()!=null) {
            Bundle extras = getIntent().getExtras();
            UIDHelper.init(myaUiHelper.getThemeConfiguration());
            getTheme().applyStyle(extras.getInt(MYA_DLS_THEME), true);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else
            finish();
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
