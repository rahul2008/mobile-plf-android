/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.ews.annotations.ApplianceRequestType;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Tag;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;


@SuppressWarnings("WeakerAccess")
@Singleton
public class ApplianceAccessManager {

    public interface FetchCallback {
        void onDeviceInfoReceived(@NonNull WifiPortProperties properties);

        void onFailedToFetchDeviceInfo();
    }

    public interface SetPropertiesCallback {
        void onPropertiesSet(@NonNull WifiPortProperties wifiPortProperties);

        void onFailedToSetProperties();
    }

    public static final String TAG = "ApplianceAccessManager";
    @ApplianceRequestType
    int requestType = ApplianceRequestType.UNKNOWN;
    @Nullable
    private FetchCallback fetchCallback;
    @Nullable
    private SetPropertiesCallback putCallback;
    private EWSGenericAppliance appliance;
    private EWSTagger ewsTagger;
    private EWSLogger ewsLogger;

    private DICommPortListener<WifiPort> wifiPortListener = new DICommPortListener<WifiPort>() {

        @Override
        public void onPortUpdate(final WifiPort port) {
            port.removePortListener(wifiPortListener);
            ewsLogger.d(TAG, "start onPortUpdate for :" + requestType);
            WifiPortProperties wifiPortProperties = port.getPortProperties();

            if (wifiPortProperties != null) {
                switch (requestType) {
                    case ApplianceRequestType.GET_WIFI_PROPS:
                        ewsLogger.d(TAG, "Step 3 : Got wifi properties, showing the password entry screen");
                        if (fetchCallback != null) {
                            fetchCallback.onDeviceInfoReceived(wifiPortProperties);
                        }
                        break;
                    case ApplianceRequestType.PUT_WIFI_PROPS:
                        ewsLogger.d(TAG, "Step 4.1 : Setting the wifi properties to the device succesfull");
                        if (putCallback != null) {
                            putCallback.onPropertiesSet(wifiPortProperties);
                        }
                        break;
                    case ApplianceRequestType.GET_DEVICE_PROPS:
                    case ApplianceRequestType.PUT_DEVICE_PROPS:
                    case ApplianceRequestType.UNKNOWN:
                    default:
                        ewsLogger.e(TAG, "Unknown request type");
                        break;
                }
                requestType = ApplianceRequestType.UNKNOWN;
                ewsLogger.d("TAG", "stop onPortUpdate for :" + requestType);
            }
        }

        @Override
        public void onPortError(WifiPort wifiPort, Error error, @Nullable String s) {
            ewsLogger.d(TAG, "Step Failed : Port error " + wifiPort.toString() + " Error : " + error + " data " + error);
            ewsTagger.trackActionSendData(Tag.KEY.TECHNICAL_ERROR, Tag.ERROR.WIFI_PORT_ERROR);
            onErrorReceived();
            if (fetchCallback != null) {
                fetchCallback.onFailedToFetchDeviceInfo();
            }
            if (putCallback != null) {
                putCallback.onFailedToSetProperties();
            }
        }

    };

    @Inject
    public ApplianceAccessManager(
            @NonNull final @Named("ews.temporary.appliance") EWSGenericAppliance appliance,
            @NonNull final EWSTagger ewsTagger, @NonNull final EWSLogger ewsLogger) {
        this.appliance = appliance;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
    }

    void fetchDevicePortProperties(@NonNull FetchCallback callback) {
        ewsLogger.d(TAG, "STEP 2 : Appliance found, fetching properties from device");
        if (requestType == ApplianceRequestType.UNKNOWN) {
            fetchCallback = callback;
            fetchWiFiPortProperties();
        }
    }

    void fetchWiFiPortProperties() {
        requestType = ApplianceRequestType.GET_WIFI_PROPS;
        final WifiPort wifiPort = appliance.getWifiPort();
        wifiPort.addPortListener(wifiPortListener);
        wifiPort.reloadProperties();
    }

    void onErrorReceived() {
        switch (requestType) {
            case ApplianceRequestType.GET_WIFI_PROPS:
            case ApplianceRequestType.GET_DEVICE_PROPS:
                ewsLogger.d("TAG", "onPortError for requestType:" + requestType);
                break;
            case ApplianceRequestType.PUT_WIFI_PROPS:
                break;
            default:
                ewsLogger.e(TAG, "Unknown request type in properties");
                break;
        }

    }

    public void connectApplianceToHomeWiFiEvent(@NonNull final String homeWiFiSSID, @NonNull final String homeWiFiPassword, @NonNull final SetPropertiesCallback callback) {
        if (requestType == ApplianceRequestType.UNKNOWN) {
            putCallback = callback;
            requestType = ApplianceRequestType.PUT_WIFI_PROPS;
            WifiPort wifiPort = appliance.getWifiPort();
            wifiPort.addPortListener(wifiPortListener);

            wifiPort.setWifiNetworkDetails(homeWiFiSSID, homeWiFiPassword);
            ewsLogger.d(TAG, "Step 4 : Setting the wifi properties to the device");
        } else {
            ewsLogger.d("TAG", "PUT_WIFI_PROPS requestType:" + requestType);
        }
    }

    @VisibleForTesting
    DICommPortListener<WifiPort> getWifiPortListener() {
        return wifiPortListener;
    }

    @VisibleForTesting
    int getRequestType() {
        return requestType;
    }

    @VisibleForTesting
    void setApplianceWifiRequestType(@ApplianceRequestType int type) {
        this.requestType = type;
    }
}