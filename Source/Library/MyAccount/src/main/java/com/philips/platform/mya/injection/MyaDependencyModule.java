package com.philips.platform.mya.injection;

import com.philips.platform.appinfra.AppInfra;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyaDependencyModule {

    private AppInfra appInfra;

    public MyaDependencyModule(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    @Singleton
    @Provides
    public AppInfra getAppInfra() {
        return appInfra;
    }

}
