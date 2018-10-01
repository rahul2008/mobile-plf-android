package com.philips.platform.csw.mock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.adobe.mobile.Analytics;
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
import com.philips.platform.pif.chi.ConsentHandlerInterface;

import java.util.Map;

public class AppInfraInterfaceMock implements AppInfraInterface {

    public RestInterfaceMock restInterfaceMock = new RestInterfaceMock();
    public ConsentManagerInterface consentManagerInterface;

    public String appTaggingComponentId;
    public String appTaggingComponentVersion;
    public String taggedActionPageName;
    public Map<String, String> taggedValues;
    public String taggedPage;
    public Map<String, String> taggedPageValues;

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

            @Override
            public void setHSDPUserUUID(String userUUID) {

            }

            @Override
            public String getCloudLoggingConsentIdentifier() {
                return null;
            }
        };
    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscovery() {
        return null;
    }

    @Override
    public AppTaggingInterface getTagging() {
        return new AppTaggingInterface() {


            PrivacyStatus status;

            @Override
            public AppTaggingInterface createInstanceForComponent(String componentId, String componentVersion) {
                appTaggingComponentId = componentId;
                appTaggingComponentVersion = componentVersion;
                return this;
            }

            @Override
            public void setPreviousPage(String s) {

            }

            @Override
            public PrivacyStatus getPrivacyConsent() {
                return status;
            }

            @Override
            public void setPrivacyConsent(PrivacyStatus privacyStatus) {
                status = privacyStatus;
            }

            @Override
            public void trackPageWithInfo(String s, String s1, String s2) {

            }

            @Override
            public void trackPageWithInfo(String pageName, Map<String, String> keyValues) {
                taggedPage = pageName;
                taggedPageValues = keyValues;
            }

            @Override
            public void trackActionWithInfo(String s, String s1, String s2) {

            }

            @Override
            public void trackActionWithInfo(String pageName, Map<String, String> keyValues) {
                taggedActionPageName = pageName;
                taggedValues = keyValues;
            }

            @Override
            public void collectLifecycleInfo(Activity activity, Map<String, Object> map) {

            }

            @Override
            public void collectLifecycleInfo(Activity activity) {

            }

            @Override
            public void pauseLifecycleInfo() {

            }

            @Override
            public void trackVideoStart(String s) {

            }

            @Override
            public void trackVideoEnd(String s) {

            }

            @Override
            public void trackSocialSharing(SocialMedium socialMedium, String s) {

            }

            @Override
            public void trackLinkExternal(String s) {

            }

            @Override
            public void trackFileDownload(String s) {

            }

            @Override
            public void trackTimedActionStart(String s) {

            }

            @Override
            public void trackTimedActionStart(String actionStart, Map<String, Object> contextData) {

            }

            @Override
            public void trackTimedActionEnd(String s) {

            }

            @Override
            public void trackTimedActionEnd(String actionEnd, Analytics.TimedActionBlock<Boolean> logic) {

            }

            @Override
            public boolean getPrivacyConsentForSensitiveData() {
                return false;
            }

            @Override
            public void setPrivacyConsentForSensitiveData(boolean b) {

            }

            @Override
            public String getTrackingIdentifier() {
                return null;
            }

            @Override
            public void unregisterTaggingData(BroadcastReceiver broadcastReceiver) {

            }

            @Override
            public void registerTaggingData(BroadcastReceiver broadcastReceiver) {

            }

            @Override
            public ConsentHandlerInterface getClickStreamConsentHandler() {
                return null;
            }

            @Override
            public String getClickStreamConsentIdentifier() {
                return null;
            }

            @Override
            public void registerClickStreamHandler(ConsentManagerInterface consentManager) {

            }
        };
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
        return restInterfaceMock;
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
        return consentManagerInterface;
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
