package com.philips.platform.catk.injection;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;

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
    public LoggingInterface providesLoggingInterface() {
        return appInfraInterface.getLogging();
    }

    @Singleton
    @Provides
    public RestInterface providesRestInterface() {
        return appInfraInterface.getRestClient();
    }
}
