package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import static com.janrain.android.engage.JREngage.getApplicationContext;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

    public class ConnectivityFragmentState extends BaseState {

        public ConnectivityFragmentState() {
            super(AppStates.CONNECTIVITY);
        }

        /**
         * to navigate
         * @param uiLauncher requires UiLauncher
         */
        @Override
        public void navigate(UiLauncher uiLauncher) {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                    handleFragmentBackStack( new ConnectivityFragment(), ConnectivityFragment.TAG,getUiStateData().getFragmentLaunchState());
        }

        @Override
        public void init(Context context) {
        }

    @Override
    public void updateDataModel() {

    }
    public String getVersion(Context c){
        return c.getResources().getString(R.string.RA_COCO_Connectivity_version);

    }

    public String getComponentID(Context c)
    {
        return c.getApplicationContext().getResources().getString(R.string.RA_COCO_Connectivity);

    }
}
