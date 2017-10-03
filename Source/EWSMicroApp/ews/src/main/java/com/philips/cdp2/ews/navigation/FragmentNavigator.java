package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentNavigator {

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

    void pop() {

    }

    void jumpTo() {

    }

    void replace() {

    }
}