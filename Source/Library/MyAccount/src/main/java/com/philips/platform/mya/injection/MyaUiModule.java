package com.philips.platform.mya.injection;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyaUiModule {

    private FragmentLauncher fragmentLauncher;
    private MyaListener myaListener;

    public MyaUiModule(FragmentLauncher fragmentLauncher, MyaListener myaListener, ThemeConfiguration themeConfiguration) {
        this.fragmentLauncher = fragmentLauncher;
        this.myaListener = myaListener;
    }

    @Singleton
    @Provides
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    @Singleton
    @Provides
    public MyaListener getMyaListener() {
        return myaListener;
    }

}
