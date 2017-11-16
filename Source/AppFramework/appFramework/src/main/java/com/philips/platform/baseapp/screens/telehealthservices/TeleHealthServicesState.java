/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.telehealthservices;

import android.content.Context;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class TeleHealthServicesState extends BaseState implements THSCompletionProtocol {
    private FragmentLauncher fragmentLauncher;

    public TeleHealthServicesState() {
        super(AppStates.TELEHEALTHSERVICES);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher)uiLauncher;

        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).handleFragmentBackStack(null,null,getUiStateData().getFragmentLaunchState());

        launchTeleHealthServices();
    }

    private void launchTeleHealthServices() {

        THSMicroAppLaunchInput microAppLaunchInput = new THSMicroAppLaunchInput("",this);//We are not using this, hence passing empty string
        THSMicroAppInterfaceImpl microAppInterface = getMicroAppInterface();
        microAppInterface.init(new THSMicroAppDependencies(((AppFrameworkApplication)
                fragmentLauncher.getFragmentActivity().getApplicationContext()).getAppInfra()), new THSMicroAppSettings(fragmentLauncher.getFragmentActivity().getApplicationContext()));
        microAppInterface.launch(fragmentLauncher, microAppLaunchInput);
    }

    protected THSMicroAppInterfaceImpl getMicroAppInterface() {
        return new THSMicroAppInterfaceImpl();
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public void didExitTHS(THSExitType thsExitType) {

    }
}
