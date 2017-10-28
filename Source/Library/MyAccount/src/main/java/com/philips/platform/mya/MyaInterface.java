package com.philips.platform.mya;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.philips.platform.mya.catk.utils.ConsentUtil;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MyaInterface implements UappInterface {

    /**
     * Launches the Myaccount interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - MyaLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), (MyaLaunchInput) uappLaunchInput);
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, (MyaLaunchInput) uappLaunchInput);
        }
    }

    private void launchAsFragment(FragmentLauncher fragmentLauncher,
                                  MyaLaunchInput myaLaunchInput) {
        Log.i("launchAsFragment", "LaunchInput: " + myaLaunchInput.getClass().toString());
        try {
            FragmentManager mFragmentManager = fragmentLauncher.getFragmentActivity().
                    getSupportFragmentManager();
            MyaFragment myaFragment = buildFragment(fragmentLauncher.getActionbarListener(), myaLaunchInput);

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(),
                    myaFragment,
                    MyaConstants.MYAFRAGMENT);

            if (((MyaLaunchInput)
                    myaLaunchInput).isAddtoBackStack()) {
                fragmentTransaction.addToBackStack(MyaConstants.MYAFRAGMENT);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException ignore) {

        }
    }

    private MyaFragment buildFragment(ActionBarListener listener, MyaLaunchInput myaLaunchInput) {
        MyaFragment myaFragment = new MyaFragment();
        myaFragment.setArguments(getApplication(myaLaunchInput), getProposition(myaLaunchInput));
        myaFragment.setOnUpdateTitleListener(listener);
        return myaFragment;
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, MyaLaunchInput myaLaunchInput) {
        if (null != uiLauncher && myaLaunchInput != null) {
            Intent myAccountIntent = new Intent(myaLaunchInput.getContext(), MyAccountActivity.class);
            myAccountIntent.putExtra(ConsentUtil.BUNDLE_KEY_APPLICATION_NAME, getApplication(myaLaunchInput));
            myAccountIntent.putExtra(ConsentUtil.BUNDLE_KEY_PROPOSITION_NAME, getProposition(myaLaunchInput));
            myAccountIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            myaLaunchInput.getContext().startActivity(myAccountIntent);
        }
    }

    private String getProposition(MyaLaunchInput myaLaunchInput) {
        return myaLaunchInput.getPropositionName() != null ? myaLaunchInput.getPropositionName() : ConsentUtil.PROPOSITION_NAME;
    }

    private String getApplication(MyaLaunchInput myaLaunchInput) {
        return myaLaunchInput.getApplicationName() != null ? myaLaunchInput.getApplicationName() : ConsentUtil.APPLICATION_NAME;
    }

    /**
     * Entry point for MyAccount. Please make sure no User registration components are being used before MyaInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
    }
}
