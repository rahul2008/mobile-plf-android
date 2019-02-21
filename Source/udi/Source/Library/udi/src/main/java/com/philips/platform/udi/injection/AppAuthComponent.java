package com.philips.platform.udi.injection;

import com.philips.platform.appinfra.logging.CloudLoggingInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.udi.configration.UdiConfiguration;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppInfraModule.class})
public interface AppAuthComponent {

    LoggingInterface getLoggingInterface();

    SecureStorageInterface getSecureStorageInterface();

    ServiceDiscoveryInterface getServiceDiscoveryInterface();

    AppTaggingInterface getAppTaggingInterface();

    void inject(UdiConfiguration udiConfiguration);

}
