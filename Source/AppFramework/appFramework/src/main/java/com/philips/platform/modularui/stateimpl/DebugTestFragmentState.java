/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DebugTestFragmentState extends UIState {
    AppFrameworkApplication appFrameworkApplication;

    /**
     * constructor
     * @param stateID
     */
    public DebugTestFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * Navigate to the fragment
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new DebugTestFragment(), DebugTestFragment.TAG);
    }

    /**
     * to handle back key
     * @param context requires context
     */

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    @Override
    public void init(UiLauncher uiLauncher) {

    }
    @Override
    public void init(Context context) {

    }
}
