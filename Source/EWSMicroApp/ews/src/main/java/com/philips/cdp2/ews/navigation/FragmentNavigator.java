package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentNavigator {

    private static final int POP_BACK_STACK_EXCLUSIVE = 0;

    @NonNull private final FragmentManager fragmentManager;

    public FragmentNavigator(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    void push(@NonNull Fragment fragment, int containerId) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getCanonicalName())
                .commit();
    }

    boolean popToFragment(@NonNull String tag) {
        return fragmentManager.popBackStackImmediate(tag, POP_BACK_STACK_EXCLUSIVE);
    }
}