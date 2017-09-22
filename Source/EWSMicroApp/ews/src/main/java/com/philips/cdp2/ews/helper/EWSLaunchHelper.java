/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.ews.EWSApplication;
import com.philips.cdp2.ews.microapp.EWSCallback;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.HashMap;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSLaunchHelper implements EWSCallback {

    private Context context;
    private String ewsStateEvent;

    public EWSLaunchHelper(@NonNull Context context) {
        this.context = context;
    }

    public void launch() {
        final AppInfraInterface appInfra = getApplication().getAppInfra();

        EWSInterface ewsInterface = new EWSInterface();
        HashMap<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);

        ewsInterface.init(new EWSDependencies(appInfra, DiscoveryManager.getInstance(), productKeyMap), new UappSettings(context));
        ewsInterface.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, -1), new EWSLauncherInput(this));
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onApplianceDiscovered(@NonNull final String cppId) {
    }

    private EWSApplication getApplication() {
        return (EWSApplication) context.getApplicationContext();
    }
}