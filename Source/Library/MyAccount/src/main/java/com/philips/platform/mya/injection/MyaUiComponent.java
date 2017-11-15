package com.philips.platform.mya.injection;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MyaUiModule.class})
@Singleton
public interface MyaUiComponent {

    FragmentLauncher getFragmentLauncher();

    MyaListener getMyaListener();

    ThemeConfiguration getThemeConfiguration();
}
