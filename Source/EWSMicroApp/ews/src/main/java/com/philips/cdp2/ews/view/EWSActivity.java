/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.communication.EventingChannel;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.navigation.FragmentNavigator;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.platform.appinfra.AppInfra;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class EWSActivity extends UiKitActivity {

    public static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);
    public static final String EWS_STEPS = "EWS_STEPS";

//    @Inject
//    ScreenFlowController screenFlowController;

    @Inject
    EventingChannel<EventingChannel.ChannelCallback> ewsEventingChannel;

    EWSComponent ewsComponent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Do something about this onCreate()
        HashMap map = new HashMap<String, String>();
        map.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);
        EWSDependencyProvider.getInstance().initDependencies(new AppInfra.Builder().build(getBaseContext()), DiscoveryManager.getInstance(),map);
        setContentView(R.layout.ews_activity_main);

        setUpToolBar();

        ewsComponent = DaggerEWSComponent.
                builder().
                eWSModule(new EWSModule(EWSActivity.this, getSupportFragmentManager())).build();
        ewsComponent.inject(this);
        ewsEventingChannel.start();
//        screenFlowController.start(this, R.id.contentFrame, getRootFragment());

        EWSTagger.collectLifecycleInfo(this);

        Navigator navigator = new Navigator(new FragmentNavigator(getSupportFragmentManager()));
        navigator.navigateToGettingStartedScreen();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        screenFlowController.stop();
        ewsEventingChannel.stop();
        ewsComponent = null;
        EWSTagger.pauseLifecycleInfo();
        EWSDependencyProvider.getInstance().clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            screenFlowController.homeButtonPressed();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        if (!screenFlowController.onBackPressed()) {
            super.onBackPressed();
//        }
    }

    @NonNull
    public EWSComponent getEWSComponent() {
        return ewsComponent;
    }
}
