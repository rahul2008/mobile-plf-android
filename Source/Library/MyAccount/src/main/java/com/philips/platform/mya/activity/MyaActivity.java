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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.injection.DaggerMyaUiComponent;
import com.philips.platform.mya.injection.MyaUiComponent;
import com.philips.platform.mya.injection.MyaUiModule;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.src;
import static com.philips.platform.mya.MyaConstants.MYA_DLS_THEME;

// TODO: Deepthi check if this can be removed

public class MyaActivity extends UIDActivity {

    private TextView mTitle;

    // TODO: Deepthi, check below can be removed, catkconstants should be removed
    private String applicationName;
    private String propositionName;

    private ImageView leftImageView;
    private ConsentBundleConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.injectCalligraphyFonts();
        initDLSThemeIfExists();
        super.onCreate(savedInstanceState);
        config = new ConsentBundleConfig(getIntent().getExtras());
        setContentView(R.layout.mya_myaccounts_activity);
        // TODO: Deepthi, remove this API
        fetchConsentData();
        Toolbar toolbar = findViewById(R.id.mya_toolbar);
        mTitle = toolbar.findViewById(R.id.mya_toolbar_title);
        leftImageView = toolbar.findViewById(R.id.mya_toolbar_left_image);
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


    private void fetchConsentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // TODO: Deepthi, discuss with OBE to remove dependency on CSW directly
            applicationName = bundle.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = bundle.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }
    }

    private void handleLeftImage(boolean shouldBackEnable) {
        if (!shouldBackEnable) {
            setLeftImage(R.drawable.mya_cross_icon);
        } else {
            setLeftImage(R.drawable.mya_back_icon);
        }
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

    private void launchTabFragment() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        extras.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.mainContainer, new ActionBarListener() {
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
        });

        MyaUiModule myaUiModule = new MyaUiModule(fragmentLauncher, MyaInterface.getMyaUiComponent().getMyaListener());
        MyaUiComponent myaUiComponent = DaggerMyaUiComponent.builder()
                .myaUiModule(myaUiModule).build();
        MyaInterface.setMyaUiComponent(myaUiComponent);

        MyaTabFragment myaTabFragment = new MyaTabFragment();
        myaTabFragment.setArguments(extras);
        myaTabFragment.showFragment(myaTabFragment, fragmentLauncher);
    }


}
