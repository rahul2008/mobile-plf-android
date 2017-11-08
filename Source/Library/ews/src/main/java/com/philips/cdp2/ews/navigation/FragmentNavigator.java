package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentNavigator {

    private static final int POP_BACK_STACK_EXCLUSIVE = 0;

    @NonNull private final FragmentManager fragmentManager;
    private final int containerId;

    public FragmentNavigator(@NonNull FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    void push(@NonNull Fragment fragment, int containerId) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getCanonicalName())
                .commit();
    }

    void pop() {
        fragmentManager.popBackStackImmediate();
    }

    boolean popToFragment(@NonNull String tag) {
        return fragmentManager.popBackStackImmediate(tag, POP_BACK_STACK_EXCLUSIVE);
    }

    public int getContainerId() {
        return containerId;
    }
}