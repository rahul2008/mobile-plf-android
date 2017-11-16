package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class EWSFragmentState extends BaseState {
    public final String TAG = EWSFragmentState.class.getSimpleName();
    private Context context;

    public EWSFragmentState() {
        super(AppStates.EWS);
    }

    /**
     * to navigate
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate called ");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        AppInfraInterface appInfra = new AppInfra.Builder().build(context);
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap(), true), new UappSettings(context));
        EWSLauncherInput ewsLauncherInput = new EWSLauncherInput();

        ewsInterface.launch(fragmentLauncher, ewsLauncherInput);
    }


    @NonNull
    private UappDependencies createUappDependencies(AppInfraInterface appInfra,
                                                    Map<String, String> productKeyMap, Boolean b) {
        return new EWSDependencies(appInfra, productKeyMap,
                new ContentConfiguration(new BaseContentConfiguration(),
                        new HappyFlowContentConfiguration.Builder().build(),
                        new TroubleShootContentConfiguration.Builder().build()));
    }


    @NonNull
    protected Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, "Device name");
        return productKeyMap;
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }

}
