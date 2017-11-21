package com.philips.platform.csw.mock;

import com.philips.platform.appinfra.AppInfraInterface;
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

import java.util.Map;

public class AppInfraInterfaceMock implements AppInfraInterface {

    @Override
    public SecureStorageInterface getSecureStorage() {
        return null;
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
        return new LoggingInterface() {
            @Override
            public LoggingInterface createInstanceForComponent(String s, String s1) {
                return this;
            }

            @Override
            public void log(LogLevel logLevel, String s, String s1) {

            }

            @Override
            public void log(LogLevel logLevel, String s, String s1, Map<String, ?> map) {

            }
        };
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
        return null;
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
}
