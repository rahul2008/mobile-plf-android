/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public abstract class DevicePairingBaseFragment extends Fragment implements BackEventListener {
    private static String TAG = DevicePairingBaseFragment.class.getSimpleName();
    private static ActionBarListener mActionbarUpdateListener;

    DevicePairingBaseFragment() {

    }

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle();

    public abstract boolean getBackButtonState();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        this.setActionbarTitle();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onStop() {
        super.onStop();
    }

    public void onStart() {
        super.onStart();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher) {
        FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarListener();

        int containerId = fragmentLauncher.getParentContainerResourceID();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            this.addFragment(fragment, fragmentActivity, containerId);
        }
    }

    protected void showFragment(Fragment fragment) {
        FragmentActivity fragmentActivity = this.getActivity();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            this.addFragment(fragment, fragmentActivity, this.getId());
        }
    }

    private void setActionbarTitle() {
        if (mActionbarUpdateListener != null) {
            mActionbarUpdateListener.updateActionBar(this.getActionbarTitleResId(), this.getBackButtonState());
            mActionbarUpdateListener.updateActionBar(this.getActionbarTitle(), this.getBackButtonState());
        }
    }

    private void addFragment(Fragment fragment, FragmentActivity fragmentActivity, int containerId) {
        try {
            FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();

            String simpleName = fragment.getClass().getSimpleName();
            fragmentTransaction.replace(containerId, fragment, simpleName);
            Fragment currentFrag = fragmentActivity.getSupportFragmentManager().findFragmentById(this.getId());
            if (!(currentFrag instanceof DevicePairingBaseFragment)) {
                fragmentTransaction.addToBackStack("device_pairing_vertical_tag");
            } else {
                fragmentTransaction.addToBackStack(simpleName);
            }

            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException var7) {
            ProdRegLogger.e(TAG, var7.getMessage());
        }

    }

    public boolean clearFragmentStack() {
        FragmentActivity activity = this.getActivity();
        try {
            if (activity != null && !activity.isFinishing()) {
                if (!(activity instanceof DevicePairingLaunchActivity)) {
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    return fragmentManager.popBackStackImmediate("device_pairing_vertical_tag", 1);
                }
                activity.finish();
            }
        } catch (IllegalStateException var3) {
            ProdRegLogger.e(TAG, var3.getMessage());
        }
        return false;
    }

    public boolean handleBackEvent() {
        return false;
    }
}
