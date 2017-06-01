package com.philips.platform.appframework.testmicroappfw.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.testmicroappfw.ui.TestFragment;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class TestFragmentState extends BaseState {
    public static final String TAG = TestFragmentState.class.getSimpleName();

    public TestFragmentState(){
        super(AppStates.TESTMICROAPP);
    }
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate ");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new TestFragment(), TestFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
