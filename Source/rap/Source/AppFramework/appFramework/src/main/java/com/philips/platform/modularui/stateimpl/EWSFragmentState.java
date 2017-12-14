package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
//import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
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
        RALog.d(TAG, " navigate to EWS Launcher called");
//        EWSInterface ewsInterface = getEwsApp();
//        ewsInterface.init(getUappDependencies(), getUappSettings());
//        ewsInterface.launch(getFragmentLauncher(uiLauncher), new EWSLauncherInput());
    }

//    @NonNull
//    protected EWSInterface getEwsApp() {
//        return new EWSInterface();
//    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher(UiLauncher uiLauncher) {
        return (FragmentLauncher) uiLauncher;
    }

    @NonNull
    protected UappSettings getUappSettings() {
        return new UappSettings(context.getApplicationContext());
    }

    @NonNull
    protected UappDependencies getUappDependencies() {
        AppInfraInterface appInfra = ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();

        /*
        our component required to have 3 different content initialization, in case of the ref app
        we use defaults only - Base, Happy flow and Trouble shooting flow
        */

        return new EWSDependencies(appInfra, createProductMap(),
                new ContentConfiguration(new BaseContentConfiguration.Builder().build(),
                        new HappyFlowContentConfiguration.Builder().build(),
                        new TroubleShootContentConfiguration.Builder().build())) {
            @Override
            public CommCentral getCommCentral() {
                return ((AppFrameworkApplication) context.getApplicationContext()).getCommCentralInstance();
            }
        };
    }


    // creating default product name which will be used by EWS Component
    @NonNull
    protected Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
//        productKeyMap.put(EWSInterface.PRODUCT_NAME, context.getString(R.string.ews_device_name_default));
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
