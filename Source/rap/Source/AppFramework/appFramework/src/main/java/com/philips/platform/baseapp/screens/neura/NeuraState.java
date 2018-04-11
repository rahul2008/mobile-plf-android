/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.neura;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;


public class NeuraState extends BaseState {

    private Context context;

    /**
     * AppFlowState constructor
     *
     * @since 1.1.0
     */
    public NeuraState() {
        super(AppStates.NEURA);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {

        FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
//        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
//                handleFragmentBackStack( new NeuraConsentManagerFragment(), NeuraConsentManagerFragment.TAG,-1);
        FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                getSupportFragmentManager();
        NeuraConsentManagerFragment neuraConsentManagerFragment = new NeuraConsentManagerFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                neuraConsentManagerFragment,
                NeuraConsentManagerFragment.TAG);
        fragmentTransaction.addToBackStack(NeuraConsentManagerFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }
}
