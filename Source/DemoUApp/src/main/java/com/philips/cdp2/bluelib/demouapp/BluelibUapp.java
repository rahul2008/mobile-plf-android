/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp2.bluelib.demouapp.fragment.MainFragment;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.lang.ref.WeakReference;

public class BluelibUapp implements UappInterface {

    private static final BluelibUapp reference = new BluelibUapp();

    public static BluelibUapp get() {
        return reference;
    }

    private BluelibUappAppDependencies dependencies;

    private FragmentLauncher fragmentLauncher;

    private SHNDevice mSelectedDevice;

    @Nullable
    private WeakReference<Context> contextref;

    @Override
    public void init(UappDependencies dependencies, UappSettings settings) {
        if (!(dependencies instanceof BluelibUappAppDependencies)) {
            throw new IllegalArgumentException("This Uapp only accepts BluelibUappAppDependencies");
        }

        this.dependencies = (BluelibUappAppDependencies) dependencies;

        contextref = new WeakReference<>(settings.getContext().getApplicationContext());
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (dependencies == null) {
            throw new IllegalStateException("No CommlibUappDependencies set during init.");
        }

        if (uiLauncher instanceof FragmentLauncher) {
            fragmentLauncher = (FragmentLauncher) uiLauncher;

            fragmentLauncher.getFragmentActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(fragmentLauncher.getParentContainerResourceID(), new MainFragment())
                    .commit();
        } else if (uiLauncher instanceof ActivityLauncher) {
            final Context context = contextref == null ? null : contextref.get();

            if (context == null) {
                throw new IllegalStateException("No context set during init. Call init before launching!");
            }

            Intent intent = new Intent(context, BluelibUappActivity.class);
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

    public BluelibUappAppDependencies getDependencies() {
        return dependencies;
    }

    public void setSelectedDevice(SHNDevice device) {
        mSelectedDevice = device;
    }

    public SHNDevice getSelectedDevice() {
        return mSelectedDevice;
    }

}
