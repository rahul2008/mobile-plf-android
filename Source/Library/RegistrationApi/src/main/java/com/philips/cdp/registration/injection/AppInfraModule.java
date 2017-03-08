package com.philips.cdp.registration.injection;


import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.platform.appinfra.AppInfraInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppInfraModule {

    private final AppInfraInterface appInfraInterface;

    public AppInfraModule(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    @Singleton
    @Provides
    public AppInfraWrapper provideAppInfraWrapper() {
        return new AppInfraWrapper(appInfraInterface);
    }
}
