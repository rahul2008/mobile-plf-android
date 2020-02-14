/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInitialisationCompleteListener;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;
import com.philips.platform.appinfra.logging.sync.CloudLogSyncManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.AIL_HOME_COUNTRY;


public class AppInfraLogging implements CloudLoggingInterface, AppInfraInitialisationCompleteListener {

    public final static String CLOUD_CONSENT = "AIL_CloudConsent";

    public static final int LOG_METADATA_INDEX=3;
    public static final int LOG_MESSAGE_INDEX=0;
    public static final int COMPONENT_ID_INDEX=1;
    public static final int COMPONENT_VERSION_INDEX=2;
    public static final int PARAM_SIZE_WITH_METADATA=4;

    private static final long serialVersionUID = -4898715486015827285L;
    private AppInfraInterface mAppInfra;
    private transient Logger mJavaLogger;
    private String componentId, componentVersion;

    private AILCloudLogMetaData ailCloudLogMetaData;

    private LoggingConfiguration loggingConfiguration;

    public AILCloudLogMetaData getAilCloudLogMetaData() {
        return ailCloudLogMetaData;
    }


    public AppInfraLogging(AppInfraInterface aAppInfra) {
        this(aAppInfra, "", "");
    }

    public AppInfraLogging(AppInfraInterface aAppInfra, String componentId, String componentVersion) {
        mAppInfra = aAppInfra;
        loggingConfiguration = new LoggingConfiguration(mAppInfra, componentId, componentVersion);
        mJavaLogger = getJavaLogger(componentId, componentVersion);
        ailCloudLogMetaData = createAilCloudLogInstance();
        this.componentId = componentId;
        this.componentVersion = componentVersion;
    }

    protected AILCloudLogMetaData createAilCloudLogInstance() {
        if (ailCloudLogMetaData == null) {
            ailCloudLogMetaData = new AILCloudLogMetaData();
        }
        return ailCloudLogMetaData;
    }

    @Override
    public LoggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        return new AppInfraLogging(mAppInfra, componentId, componentVersion);
    }


    @Override
    public void log(LogLevel level, String eventId, String message) {
        log(level, eventId, message, null);
        if (TextUtils.isEmpty(componentId)) {
            componentId = mAppInfra.getAppIdentity().getAppName();
        }
        if (TextUtils.isEmpty(componentVersion)) {
            componentVersion = mAppInfra.getAppIdentity().getAppVersion();
        }
    }

    /**
     * Logs message on console and file .
     *
     * @param level   the level {VERBOSE, DEBUG, INFO, WARNING, ERROR}
     * @param eventId the Event name or Tag
     * @param message the message
     * @param map     the dictionary
     * @since 1.0.0
     */
    @Override
    public void log(LogLevel level, String eventId, String message, Map<String, ?> map) {
        if (loggingConfiguration.checkIfComponentLogCriteriaMet(componentId)) {
            Object[] params = getParamObjects();
            if (null != mJavaLogger) {
                params[LOG_MESSAGE_INDEX] = message;
                params[COMPONENT_ID_INDEX] = componentId;
                params[COMPONENT_VERSION_INDEX] = componentVersion;
                params[LOG_METADATA_INDEX] = map;

                switch (level) {
                    case ERROR:
                        mJavaLogger.log(Level.SEVERE, eventId, params);
                        break;
                    case WARNING:
                        mJavaLogger.log(Level.WARNING, eventId, params);
                        break;
                    case INFO:
                        mJavaLogger.log(Level.INFO, eventId, params);
                        break;
                    case DEBUG:
                        mJavaLogger.log(Level.CONFIG, eventId, params);
                        break;
                    case VERBOSE:
                        mJavaLogger.log(Level.FINE, eventId, params);
                        break;
                }
            }
        }
    }

    @Override
    public void setHSDPUserUUID(String userUUID) {
        ailCloudLogMetaData.setUserUUID(userUUID);
    }


    @Override
    public String getCloudLoggingConsentIdentifier() {
        return CLOUD_CONSENT;
    }

    protected Logger getJavaLogger(String componentId, String componentVersion) {
        return LoggerFactory.getLoggerInstance(mAppInfra, loggingConfiguration);
    }

    @NonNull
    Object[] getParamObjects() {
        return new Object[4];
    }

    @Override
    public void onAppInfraInitialised(final AppInfraInterface appInfra) {
        if (loggingConfiguration.isCloudLogEnabled()) {
            updateMetadata(appInfra);
        }
        registerCloudHandler();


    }

    /**
     * Update cloud logging metadata. This instance will hold metadata which will be rquired to save logs in DB.
     *
     * @param appInfra
     */
    public void updateMetadata(AppInfraInterface appInfra) {
        try {
            if (appInfra.getAppIdentity() != null) {
                ailCloudLogMetaData.setAppName(appInfra.getAppIdentity().getAppName());
                ailCloudLogMetaData.setAppState(appInfra.getAppIdentity().getAppState().toString());
                ailCloudLogMetaData.setAppVersion(appInfra.getAppIdentity().getAppVersion());
            }
            if (appInfra.getTagging() != null) {
                ailCloudLogMetaData.setAppId(appInfra.getTagging().getTrackingIdentifier());
            }
            if (appInfra.getInternationalization() != null) {
                ailCloudLogMetaData.setLocale(appInfra.getInternationalization().getUILocaleString());
            }
            if (appInfra.getServiceDiscovery() != null) {
                ailCloudLogMetaData.setHomeCountry(appInfra.getServiceDiscovery().getHomeCountry());
            }
            appInfra.getServiceDiscovery().registerOnHomeCountrySet(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ServiceDiscoveryInterface.AIL_SERVICE_DISCOVERY_HOMECOUNTRY_CHANGE_ACTION)) {
                        String countryCode = (String) intent.getExtras().get(AIL_HOME_COUNTRY);
                        ailCloudLogMetaData.setHomeCountry(countryCode);
                    }
                }
            });
        } catch (IllegalArgumentException e) {
        }

    }

    /**
     * Register cloud handler for cloud logging consent.
     */
    public void registerCloudHandler() {
        DeviceStoredConsentHandler deviceStoredConsentHandler = mAppInfra.getDeviceStoredConsentHandler();
        if (loggingConfiguration != null && loggingConfiguration.isCloudLogEnabled()) {
            CloudLogSyncManager.getInstance(mAppInfra, loggingConfiguration);
        }
        mAppInfra.getConsentManager().registerHandler(Collections.singletonList(CLOUD_CONSENT), deviceStoredConsentHandler);
    }

}
