package com.philips.platform.pim.injection;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.configration.PimConfiguration;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppInfraModule.class})
public interface PimComponent {

    LoggingInterface getLoggingInterface();

    SecureStorageInterface getSecureStorageInterface();

    ServiceDiscoveryInterface getServiceDiscoveryInterface();

    AppTaggingInterface getAppTaggingInterface();

    void inject(PimConfiguration pimConfiguration);

}
