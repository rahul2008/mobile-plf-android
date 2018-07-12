package com.philips.platform.core.injection;

import com.philips.platform.appinfra.AppInfraInterface;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = { ApplicationModule.class })
@Singleton
public interface AppInfraComponent {

    void inject(AppInfraInterface appInfra);

    AppInfraInterface getAppInfra();
}

