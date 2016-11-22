package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

    public class ConnectivityFragmentState extends BaseState {

        public ConnectivityFragmentState() {
            super(BaseAppState.CONNECTIVITY);
        }

        /**
         * to navigate
         * @param uiLauncher requires UiLauncher
         */
        @Override
        public void navigate(UiLauncher uiLauncher) {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            ((AppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                    addFragment(new ConnectivityFragment(), ConnectivityFragment.TAG);
        }

        @Override
        public void init(Context context) {

        }
    }
