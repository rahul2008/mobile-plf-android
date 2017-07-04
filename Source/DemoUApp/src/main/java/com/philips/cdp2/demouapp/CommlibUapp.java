/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.support.v4.app.Fragment;

import com.philips.cdp2.demouapp.fragment.MainFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CommlibUapp implements UappInterface {

    private CommlibUappDependencies dependencies;
    private FragmentLauncher fragmentLauncher;

    public static CommlibUapp instance = new CommlibUapp();

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        if (!(uappDependencies instanceof CommlibUappDependencies)) {
            throw new IllegalArgumentException("This Uapp only accepts CommlibUappDependencies");
        }

        dependencies = (CommlibUappDependencies) uappDependencies;
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (dependencies == null) {
            throw new UnsupportedOperationException("No CommlibUappDependencies set during init.");
        }

        if (!(uiLauncher instanceof FragmentLauncher)) {
            throw new IllegalArgumentException("This Uapp only works with FragmentLaunchers");
        }

        fragmentLauncher = (FragmentLauncher) uiLauncher;

        fragmentLauncher.getFragmentActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(fragmentLauncher.getParentContainerResourceID(), new MainFragment())
                .commit();
    }

    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    public void nextFragment(Fragment fragment) {
        fragmentLauncher.getFragmentActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(fragmentLauncher.getParentContainerResourceID(), fragment)
                .addToBackStack(null)
                .commit();
    }

    public CommlibUappDependencies getDependencies() {
        return dependencies;
    }
}
