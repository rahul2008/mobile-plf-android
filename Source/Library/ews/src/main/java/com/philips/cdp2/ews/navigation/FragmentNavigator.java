package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentNavigator {

    @VisibleForTesting
    static final int POP_BACK_STACK_INCLUSIVE = 1;

    @NonNull
    private final FragmentManager fragmentManager;
    private final int containerId;

    public FragmentNavigator(@NonNull FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    void push(@NonNull Fragment fragment, int containerId) {
        if (!fragmentManager.isStateSaved()) {
            fragmentManager.popBackStack(fragment.getClass().getCanonicalName(), POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getCanonicalName());
        if (fragmentManager.isStateSaved()) {
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction.commit();
        }
    }

    void pop() {
        fragmentManager.popBackStackImmediate();
    }

    public int getContainerId() {
        return containerId;
    }

    public boolean shouldFinish() {
        return fragmentManager.getBackStackEntryCount() == 1;
    }
}