/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.platform.appinfra.AppInfra;

import java.util.HashMap;

import javax.inject.Inject;

public class EWSActivity extends UiKitActivity {

    @Inject
    ScreenFlowController screenFlowController;

    @Inject
    EventingChannel<EventingChannel.ChannelCallback> ewsEventingChannel;

    EWSComponent ewsComponent;

    public static final int DEVICE_CONNECTION_TIMEOUT = 30000;
    public static final String EWS_STEPS = "EWS_STEPS";


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap map = new HashMap<String, String>();
        map.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);
        EWSDependencyProvider.getInstance().initDependencies(new AppInfra.Builder().build(getBaseContext()), DiscoveryManager.getInstance(),map);
        setContentView(R.layout.ews_activity_main);
        ewsComponent = DaggerEWSComponent.
                builder().
                eWSModule(new EWSModule(EWSActivity.this)).build();
        ewsComponent.inject(this);
        ewsEventingChannel.start();
        screenFlowController.start(this, R.id.contentFrame, getRootFragment());
        EWSTagger.collectLifecycleInfo(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenFlowController.stop();
        ewsEventingChannel.stop();
        ewsComponent = null;
        EWSTagger.pauseLifecycleInfo();
        EWSDependencyProvider.getInstance().clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            screenFlowController.homeButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!screenFlowController.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public Fragment getRootFragment() {
        return Fragment.instantiate(this, EWSGettingStartedFragment.class.getName());
    }

    @NonNull
    public EWSComponent getEWSComponent() {
        return ewsComponent;
    }
}
