/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.philips.cdp2.demouapp.fragment.MainFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CommlibUapp implements UappInterface {

    public static CommlibUapp instance = new CommlibUapp();

    private CommlibUappDependencies dependencies;

    private FragmentLauncher fragmentLauncher;

    private Context context;

    @Override
    public void init(UappDependencies dependencies, UappSettings settings) {
        if (!(dependencies instanceof CommlibUappDependencies)) {
            throw new IllegalArgumentException("This Uapp only accepts CommlibUappDependencies");
        }

        this.dependencies = (CommlibUappDependencies) dependencies;

        context = settings.getContext();
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (dependencies == null) {
            throw new UnsupportedOperationException("No CommlibUappDependencies set during init.");
        }

        if (uiLauncher instanceof FragmentLauncher) {
            fragmentLauncher = (FragmentLauncher) uiLauncher;

            fragmentLauncher.getFragmentActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(fragmentLauncher.getParentContainerResourceID(), new MainFragment())
                    .commit();
        } else if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, CommlibUappActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Unsupported UiLauncher provided");
        }
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
