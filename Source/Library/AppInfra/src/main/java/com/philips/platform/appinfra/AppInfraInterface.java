package com.philips.platform.appinfra;

import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

/**
 * The interface App infra interface.
 */
public interface AppInfraInterface {
    /**
     * Gets secure storage.
     *
     * @return the secure storage
     */
    public SecureStorageInterface getSecureStorage();

    /**
     * Gets app identity.
     *
     * @return the app identity
     */
    public AppIdentityInterface getAppIdentity();

    /**
     * Gets local.
     *
     * @return the local
     */
    public InternationalizationInterface getInternationalization();

    /**
     * Gets logging.
     *
     * @return the logging
     */
    public LoggingInterface getLogging();

    /**
     * Gets service discovery interface.
     *
     * @return the service discovery interface
     */
    public ServiceDiscoveryInterface getServiceDiscovery();

    /**
     * Gets tagging.
     *
     * @return the tagging
     */
    public AppTaggingInterface getTagging();

    /**
     * Gets time sync.
     *
     * @return the time sync
     */
    public TimeInterface getTime();

    /**
     * Gets time sync.
     *
     * @return the config
     */
    public AppConfigurationInterface getConfigInterface();

    /**
     * Gets REST API Manager.
     *
     * @return the config
     */
    public RestInterface getRestClient();

    /**
     *  Gets the A/B testing Manager.
     * @return the config
     */
    public ABTestClientInterface getAbTesting();
}
