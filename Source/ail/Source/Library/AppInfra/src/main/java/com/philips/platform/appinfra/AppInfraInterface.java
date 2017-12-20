package com.philips.platform.appinfra;

import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.aikm.AIKMInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.io.Serializable;

/**
 * App Infra provides a range of modules that are the basis for any mobile application
 * App Infra provides various utility services for the application such as Logging, Tagging, Service discovery etc..
 */
public interface AppInfraInterface extends Serializable {
    /**
     * Gets secure storage.
     *
     * @return the secure storage
     */
    SecureStorageInterface getSecureStorage();

    /**
     * Gets app identity.
     *
     * @return the app identity
     */
    AppIdentityInterface getAppIdentity();

    /**
     * Gets local.
     *
     * @return the local
     */
    InternationalizationInterface getInternationalization();

    /**
     * Gets logging.
     *
     * @return the logging
     */
    LoggingInterface getLogging();

    /**
     * Gets service discovery interface.
     *
     * @return the service discovery interface
     */
    ServiceDiscoveryInterface getServiceDiscovery();

    /**
     * Gets tagging.
     *
     * @return the tagging
     */
    AppTaggingInterface getTagging();

    /**
     * Gets time sync.
     *
     * @return the time sync
     */
    TimeInterface getTime();

    /**
     * Gets time sync.
     *
     * @return the config
     */
    AppConfigurationInterface getConfigInterface();

    /**
     * Gets REST API Manager.
     *
     * @return the config
     */
    RestInterface getRestClient();

    /**
     *  Gets the A/B testing Manager.
     * @return the abTesting
     */
    ABTestClientInterface getAbTesting();

    /**
     *  Gets the language pack Manager.
     * @return the language pack
     */
    LanguagePackInterface getLanguagePack();

    /**
     *  Gets the appupdate interface.
     * @return appupdate interface
     */
    AppUpdateInterface getAppUpdate();

    AIKMInterface getAiKmInterface();
}
