package com.philips.platform.mya;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MyaInterface implements UappInterface {


    private MyaSettings myaSettings;

    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher,
                                  UappLaunchInput uappLaunchInput) {
        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            MyaFragment myaFragment = buildFragment(fragmentLauncher.getActionbarListener());

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    myaFragment,
                    MyaConstants.MYAFRAGMENT);

            if (((MyaLaunchInput)
                    uappLaunchInput).isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(MyaConstants.MYAFRAGMENT);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {

        }
    }

    private MyaFragment buildFragment(ActionBarListener listener) {
        MyaFragment myaFragment = new MyaFragment();
        Bundle bundle = MyaFragment.buildBundle(myaSettings.applicationName, myaSettings.propositionName);
        myaFragment.setArguments(bundle);
        myaFragment.setOnUpdateTitleListener(listener);
        return myaFragment;
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (null != uiLauncher && uappLaunchInput != null) {
            Intent myAccountIntent = new Intent(((MyaLaunchInput) uappLaunchInput).getContext(), MyAccountActivity.class);
            myAccountIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            ((MyaLaunchInput) uappLaunchInput).getContext().startActivity(myAccountIntent);
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
        myaSettings = (MyaSettings)uappSettings;
    }
}
