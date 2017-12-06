/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.injections.AppModule;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.ActionBarTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class EWSActivity extends DynamicThemeApplyingActivity implements EWSActionBarListener {

    public static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);
    public static final String EWS_STEPS = "EWS_STEPS";
    public static final String KEY_CONTENT_CONFIGURATION = "contentConfiguration";

    @Inject
    Navigator navigator;

    @Inject
    BaseContentConfiguration baseContentConfiguration;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!AppModule.areDependenciesInitialized()){
            this.finish();
            return;
        }
        setContentView(R.layout.ews_activity_main);
        initEWSComponent(getBundle(savedInstanceState));
        setUpToolBar();
        findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelButtonClicked();
            }
        });

        if (savedInstanceState == null) {
            navigator.navigateToGettingStartedScreen();
        }
    }

    private void initEWSComponent(@Nullable Bundle savedInstanceState) {
        ContentConfiguration contentConfiguration =
                BundleUtils.extractParcelableFromIntentOrNull(savedInstanceState, KEY_CONTENT_CONFIGURATION);

        if (contentConfiguration == null) {
            contentConfiguration = new ContentConfiguration();
        }

        DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(this
                        , this.getSupportFragmentManager()
                        , R.id.contentFrame, AppModule.getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(this, contentConfiguration))
                .build().inject(this);

//        EWSDependencyProvider.getInstance().createEWSComponent(this, R.id.contentFrame,
//                contentConfiguration);
//
//        EWSDependencyProvider.getInstance().getEwsComponent().inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ContentConfiguration contentConfiguration =
                BundleUtils.extractParcelableFromIntentOrNull(getIntent().getExtras(), KEY_CONTENT_CONFIGURATION);
        outState.putParcelable(KEY_CONTENT_CONFIGURATION, contentConfiguration);
    }

    private Bundle getBundle(@Nullable Bundle savedInstanceState) {
        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
        } else {
            bundle = savedInstanceState;
        }
        return bundle;
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.ews_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        EWSDependencyProvider.getInstance().clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!wasBackHandledByCurrentlyDisplayedFragment()) {
            if (shouldFinish()) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    private boolean shouldFinish() {
        return getSupportFragmentManager().getBackStackEntryCount() == 1;
    }

    private boolean wasBackHandledByCurrentlyDisplayedFragment() {
        boolean backHandledByFragment = false;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (currentFragment instanceof BackEventListener) {
            BackEventListener backEventListener = (BackEventListener) currentFragment;
            backHandledByFragment = backEventListener.handleBackEvent();
        }
        return backHandledByFragment;
    }

    protected void handleCancelButtonClicked() {
        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        baseFragment.handleCancelButtonClicked();
    }

    public void setToolbarTitle(String s) {
        Toolbar toolbar = findViewById(R.id.ews_toolbar);
        ((ActionBarTextView) toolbar.findViewById(R.id.toolbar_title)).setText(s);
    }

    @Override
    public void closeButton(boolean visibility) {
        findViewById(R.id.ic_close).setVisibility(visibility ? View.VISIBLE : View.GONE);
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
    protected void onResume() {
        super.onResume();
        EWSTagger.collectLifecycleInfo(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EWSTagger.pauseLifecycleInfo();
    }
}
