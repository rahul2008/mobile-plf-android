package com.philips.platform.mya.injection;

import com.philips.platform.appinfra.AppInfraInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyaDependencyModule {

    private AppInfraInterface appInfra;

    public MyaDependencyModule(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    @Singleton
    @Provides
    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

}
