package com.philips.platform.baseapp.screens.telehealthservices;

import android.app.Activity;
import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.IndexSelectionListener;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;

import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 27/03/18.
 */

public class TeleHealthServicesDeepLinkingState extends BaseState implements THSCompletionProtocol {
    private static final String TAG = TeleHealthServicesState.class.getSimpleName();

    private FragmentLauncher fragmentLauncher;

    public TeleHealthServicesDeepLinkingState() {
        super(AppStates.TELE_HEALTHSERVICES_DEEP_LINKING_STATE);
    }


    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher)uiLauncher;

        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).handleFragmentBackStack(null, null,getUiStateData().getFragmentLaunchState());

        launchTeleHealthServices();
    }

    private void launchTeleHealthServices() {

        THSMicroAppLaunchInput microAppLaunchInput = new THSMicroAppLaunchInput("",this,true);//We are not using this, hence passing empty string
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
        RALog.d(TAG, "thsExitType : "+thsExitType);
        Activity activity = fragmentLauncher.getFragmentActivity();
        if (activity instanceof IndexSelectionListener) {
            ((IndexSelectionListener) activity).updateSelectionIndex(0);
        }
    }



}
