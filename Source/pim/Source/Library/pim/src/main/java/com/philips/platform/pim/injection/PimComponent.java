package com.philips.platform.pim.injection;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pim.configration.PimConfiguration;
import com.philips.platform.pim.utilities.PimStorageUtility;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppInfraModule.class, ManagerModule.class})
public interface PimComponent {

    LoggingInterface getLoggingInterface();

    SecureStorageInterface getSecureStorageInterface();

    ServiceDiscoveryInterface getServiceDiscoveryInterface();

    AppTaggingInterface getAppTaggingInterface();

    RestInterface getRestClientInterface();

    PimStorageUtility getPimStorageUtility();

    void inject(PimConfiguration pimConfiguration);
}
