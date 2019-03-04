package com.philips.platform.pim.injection;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.BuildConfig;
import com.philips.platform.pim.utilities.ServiceDiscoveryWrapper;

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


    @Provides
    public ServiceDiscoveryInterface providesServiceDiscovery() {
        return appInfraInterface.getServiceDiscovery();
    }

    @Singleton
    @Provides
    public AppTaggingInterface providesAppTaggingInterface() {
        AppTaggingInterface appTaggingInterface = appInfraInterface.getTagging().createInstanceForComponent("udi", BuildConfig.VERSION_NAME);
        return appTaggingInterface;
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

    @Singleton
    @Provides
    public SecureStorageInterface providesSecureStorageInterface() {
        return appInfraInterface.getSecureStorage();
    }
}
