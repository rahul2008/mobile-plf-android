/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.mya.injection;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyaUiModule {

    private FragmentLauncher fragmentLauncher;
    private MyaListener myaListener;
    private ThemeConfiguration themeConfiguration;

    public MyaUiModule(UiLauncher uiLauncher, MyaListener myaListener) {
        this.myaListener = myaListener;
        if (uiLauncher instanceof ActivityLauncher) {
            this.themeConfiguration = ((ActivityLauncher) uiLauncher).getDlsThemeConfiguration();
        } else {
            this.fragmentLauncher = (FragmentLauncher) uiLauncher;
        }
    }

    @Singleton
    @Provides
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    @Singleton
    @Provides
    public MyaListener getMyaListener() {
        return myaListener;
    }

    @Singleton
    @Provides
    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }
}
