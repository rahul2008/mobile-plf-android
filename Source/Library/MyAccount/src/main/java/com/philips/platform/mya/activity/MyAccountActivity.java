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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.mya.R;
import com.philips.platform.mya.injection.MyaUiComponent;
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

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.philips.platform.mya.MyaConstants.MYA_DLS_THEME;

public class MyAccountActivity extends UIDActivity implements MyaListener {

    private TextView mTitle;
    private ImageView leftImageView;
    private ConsentBundleConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.injectCalligraphyFonts();
        initDLSThemeIfExists();
        super.onCreate(savedInstanceState);
        config = new ConsentBundleConfig(getIntent().getExtras());
        setContentView(R.layout.mya_myaccounts_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mya_toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.mya_toolbar_title);
        leftImageView = (ImageView) toolbar.findViewById(R.id.mya_toolbar_left_image);
        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        launchTabFragment();
    }

    private void launchTabFragment() {
        MyaInterface myaInterface = new MyaInterface();
        MyaDependencies uappDependencies = new MyaDependencies(MyaInterface.getMyaDependencyComponent().getAppInfra());
        uappDependencies.setApplicationName(config.getApplicationName());
        uappDependencies.setPropositionName(config.getPropositionName());

        myaInterface.init(uappDependencies, new MyaSettings(this, config.getConsentDefinitions()));
        myaInterface.launch(new FragmentLauncher(this, R.id.mainContainer, new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean shouldBackEnable) {
                setTitle(i);
                handleLeftImage(shouldBackEnable);
            }

            @Override
            public void updateActionBar(String s, boolean shouldBackEnable) {
                setTitle(s);
                handleLeftImage(shouldBackEnable);
            }
        }), new MyaLaunchInput(this, this));
    }

    private void handleLeftImage(boolean shouldBackEnable) {
        if (!shouldBackEnable) {
            setLeftImage(R.drawable.mya_cross_icon);
        } else {
            setLeftImage(R.drawable.mya_back_icon);
        }
    }

    @Override
    public boolean onClickMyaItem(String itemName) {
        Log.d("Testing call back = ",itemName);
        return false;
    }

    @Override
    public boolean onLogOut() {
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
        MyaUiComponent myaUiComponent = MyaInterface.getMyaUiComponent();
        if (myaUiComponent != null) {
            ThemeConfiguration themeConfiguration = MyaInterface.getMyaUiComponent().getThemeConfiguration();
            if (getIntent().getExtras() != null && themeConfiguration != null) {
                Bundle extras = getIntent().getExtras();
                UIDHelper.init(themeConfiguration);
                getTheme().applyStyle(extras.getInt(MYA_DLS_THEME), true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean backState = false;
        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            if (currentFrag != null && currentFrag instanceof BackEventListener) {
                backState = ((BackEventListener) currentFrag).handleBackEvent();
            }

            if (!backState) {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
            config = new ConsentBundleConfig(state);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putAll(config.toBundle());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setLeftImage(int resId) {
        leftImageView.setBackgroundResource(resId);
    }


}
