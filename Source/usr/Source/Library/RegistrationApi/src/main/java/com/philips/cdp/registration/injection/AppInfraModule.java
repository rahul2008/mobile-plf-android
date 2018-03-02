package com.philips.cdp.registration.injection;


import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppInfraModule {

    private final AppInfraInterface appInfraInterface;

    public AppInfraModule(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    @Provides
    public RestInterface providesRestInterface() {
        return appInfraInterface.getRestClient();
    }

    @Singleton
    @Provides
    public AppInfraWrapper provideAppInfraWrapper() {
        return new AppInfraWrapper(appInfraInterface);
    }

    @Provides
    public TimeInterface provideTimeInterface() {
        return appInfraInterface.getTime();
    }

    @Provides
    public ServiceDiscoveryInterface providesServiceDiscovery() {
        return appInfraInterface.getServiceDiscovery();
    }

    @Singleton
    @Provides
    public AppTaggingInterface providesAppTaggingInterface() {
        AppTaggingInterface appTaggingInterface = appInfraInterface.getTagging().createInstanceForComponent("usr", RegistrationHelper.getRegistrationApiVersion());
        return appTaggingInterface;
    }

    @Singleton
    @Provides
    public ABTestClientInterface providesAbTestClientInterface() {
        return appInfraInterface.getAbTesting();
    }

    @Singleton
    @Provides
    public LoggingInterface providesLoggingInterface() {
        return appInfraInterface.getLogging();
    }

    @Provides
    public ServiceDiscoveryWrapper providesServiceDiscoveryWrapper() {
        return new ServiceDiscoveryWrapper(appInfraInterface.getServiceDiscovery());
    }
}
