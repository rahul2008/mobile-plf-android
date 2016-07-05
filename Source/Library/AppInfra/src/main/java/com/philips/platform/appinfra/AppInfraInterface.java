package com.philips.platform.appinfra;

import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.LocalInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeSyncInterface;

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
    public LocalInterface getLocal();

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
    public ServiceDiscoveryInterface getServiceDiscoveryInterface();

    /**
     * Gets tagging.
     *
     * @return the tagging
     */
    public AIAppTaggingInterface getTagging();

    /**
     * Gets time sync.
     *
     * @return the time sync
     */
    public TimeSyncInterface getTimeSync();
}
