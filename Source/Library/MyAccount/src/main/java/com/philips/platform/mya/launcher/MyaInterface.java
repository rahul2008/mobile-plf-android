/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswSettings;
import com.philips.platform.mya.*;
import com.philips.platform.mya.activity.MyAccountActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.platform.mya.util.MyaConstants.MYA_DLS_THEME;

public class MyaInterface implements UappInterface {

    private static String applicationName;
    private static String propositionName;

    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity((ActivityLauncher) uiLauncher, (MyaLaunchInput) uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, (MyaLaunchInput) uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher, MyaLaunchInput myaLaunchInput) {
        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            MyaFragment myaFragment = buildFragment(fragmentLauncher.getActionbarListener());

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    myaFragment,
                    MyaConstants.MYAFRAGMENT);

            if (myaLaunchInput.isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(MyaConstants.MYAFRAGMENT);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {
            ignore.getMessage();
        }
    }

    private MyaFragment buildFragment(ActionBarListener listener) {
        MyaFragment myaFragment = new MyaFragment();
        myaFragment.setArguments(applicationName, propositionName);
        myaFragment.setOnUpdateTitleListener(listener);
        return myaFragment;
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, MyaLaunchInput myaLaunchInput) {
        if (null != uiLauncher && myaLaunchInput != null) {
            Intent myAccountIntent = new Intent(myaLaunchInput.getContext(), MyAccountActivity.class);
            myAccountIntent.putExtra(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
            myAccountIntent.putExtra(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
            myAccountIntent.putExtra(MYA_DLS_THEME, uiLauncher.getUiKitTheme());
            myaLaunchInput.getContext().startActivity(myAccountIntent);
        }
    }

    /**
     * Entry point for MyAccount. Please make sure no User registration components are being used before MyaInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        CswDependencies cswDependencies = new CswDependencies(uappDependencies.getAppInfra());
        applicationName = ((MyaDependencies) uappDependencies).getApplicationName();
        propositionName = ((MyaDependencies) uappDependencies).getPropositionName();
        cswDependencies.setApplicationName(applicationName == null ? CatkConstants.APPLICATION_NAME : applicationName);
        cswDependencies.setPropositionName(propositionName == null ? CatkConstants.PROPOSITION_NAME : propositionName);
        CswSettings cswSettings = new CswSettings(uappSettings.getContext());
        CswInterface cswInterface = new CswInterface();
        cswInterface.init(cswDependencies, cswSettings);
    }
}
