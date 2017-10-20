/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.communication.EventingChannel;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.navigation.ActivityNavigator;
import com.philips.cdp2.ews.navigation.FragmentNavigator;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class EWSActivity extends UiKitActivity {

    public static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);
    public static final String EWS_STEPS = "EWS_STEPS";
    public static final String KEY_CONTENT_CONFIGURATION = "contentConfiguration";

    @Inject
    EventingChannel<EventingChannel.ChannelCallback> ewsEventingChannel;

    EWSComponent ewsComponent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO clean up this onCreate()
        initMicroAppDependencies();
        setContentView(R.layout.ews_activity_main);
        setUpToolBar();
        setUpCancelButton();

        ewsComponent = createEWSComponent(getBundle(savedInstanceState));
        ewsComponent.inject(this);
        ewsEventingChannel.start();

        EWSTagger.collectLifecycleInfo(this);

        //TODO move this initialization.
        Navigator navigator = new Navigator(new FragmentNavigator(getSupportFragmentManager()),
                new ActivityNavigator(this));
        navigator.navigateToGettingStartedScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ContentConfiguration contentConfiguration =
                BundleUtils.extractParcelableFromIntentOrNull(getIntent().getExtras(), KEY_CONTENT_CONFIGURATION);
        outState.putParcelable(KEY_CONTENT_CONFIGURATION, contentConfiguration);
    }

    private Bundle getBundle(Bundle savedInstanceState) {
        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
        } else {
            bundle = savedInstanceState;
        }
        return bundle;
    }

    private EWSComponent createEWSComponent(@Nullable Bundle bundle) {
        ContentConfiguration contentConfiguration =
                BundleUtils.extractParcelableFromIntentOrNull(bundle, KEY_CONTENT_CONFIGURATION);

        // TODO Handle null config object
        if (contentConfiguration == null) {
            contentConfiguration = new ContentConfiguration();
        }

        return DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(EWSActivity.this, getSupportFragmentManager()))
                .eWSConfigurationModule(new EWSConfigurationModule(this, contentConfiguration.getBaseContentConfiguration(),
                        contentConfiguration.getHappyFlowContentConfiguration()))
                .build();
    }

    private void setUpCancelButton() {
        findViewById(R.id.ic_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelButtonClicked();
            }
        });
    }

    private void initMicroAppDependencies() {
        Map<String, String> map = new HashMap<>();
        map.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);
        EWSDependencyProvider
                .getInstance().initDependencies(new AppInfra.Builder().build(getBaseContext()), map);
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.ews_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    public void showCloseButton() {
        findViewById(R.id.ic_close).setVisibility(View.VISIBLE);
    }

    public void hideCloseButton() {
        findViewById(R.id.ic_close).setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ewsEventingChannel.stop();
        ewsComponent = null;
        EWSTagger.pauseLifecycleInfo();
        EWSDependencyProvider.getInstance().clear();
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

    @NonNull
    public EWSComponent getEWSComponent() {
        return ewsComponent;
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

}
