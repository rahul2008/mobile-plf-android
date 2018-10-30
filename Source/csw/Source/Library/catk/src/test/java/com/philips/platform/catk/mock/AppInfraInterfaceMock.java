package com.philips.platform.catk.mock;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.aikm.AIKMInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

public class AppInfraInterfaceMock implements AppInfraInterface {

    private SecureStorageInterface storageInterface;
    private AppConfigurationInterface configInterface;

    public AppInfraInterfaceMock(SecureStorageInterface storageInterface, AppConfigurationInterface configInterface) {
        this.storageInterface = storageInterface;
        this.configInterface = configInterface;
    }

    public AppInfraInterfaceMock() {
    }

    @Override
    public SecureStorageInterface getSecureStorage() {
        return storageInterface;
    }

    @Override
    public AppIdentityInterface getAppIdentity() {
        return null;
    }

    @Override
    public InternationalizationInterface getInternationalization() {
        return null;
    }

    @Override
    public LoggingInterface getLogging() {
        return null;
    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscovery() {
        return null;
    }

    @Override
    public AppTaggingInterface getTagging() {
        return null;
    }

    @Override
    public TimeInterface getTime() {
        return null;
    }

    @Override
    public AppConfigurationInterface getConfigInterface() {
        return configInterface;
    }

    @Override
    public RestInterface getRestClient() {
        return null;
    }

    @Override
    public ABTestClientInterface getAbTesting() {
        return null;
    }

    @Override
    public LanguagePackInterface getLanguagePack() {
        return null;
    }

    @Override
    public AppUpdateInterface getAppUpdate() {
        return null;
    }

    @Override
    public AIKMInterface getAiKmInterface() {
        return null;
    }

    @Override
    public ConsentManagerInterface getConsentManager() {
        return null;
    }

    @Override
    public DeviceStoredConsentHandler getDeviceStoredConsentHandler() {
        return null;
    }

    @Override
    public Context getAppInfraContext() {
        return null;
    }
}
