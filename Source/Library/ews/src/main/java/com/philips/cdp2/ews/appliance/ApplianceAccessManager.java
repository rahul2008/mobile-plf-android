/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.ews.annotations.ApplianceRequestType;
import com.philips.cdp2.ews.annotations.ConnectionErrorType;
import com.philips.cdp2.ews.communication.events.ApplianceConnectErrorEvent;
import com.philips.cdp2.ews.communication.events.DeviceConnectionErrorEvent;
import com.philips.cdp2.ews.logger.EWSLogger;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
@Singleton
public class ApplianceAccessManager {

    public static final String TAG = "ApplianceAccessManager";

    public interface FetchCallback {
        void onDeviceInfoReceived(@NonNull WifiPortProperties properties);
        void onFailedToFetchDeviceInfo();
    }

    public interface SetPropertiesCallback {
        void onPropertiesSet(@NonNull WifiPortProperties wifiPortProperties);
        void onFailedToSetProperties();
    }

    @Nullable private FetchCallback fetchCallback;
    @Nullable private SetPropertiesCallback putCallback;

    @ApplianceRequestType
    int requestType = ApplianceRequestType.UNKNOWN;
    private EventBus eventBus;
    private EWSGenericAppliance appliance;
    ApplianceSessionDetailsInfo sessionDetailsInfo;
    private String homeWiFiSSID;
    private DICommPortListener<WifiPort> wifiPortListener = new DICommPortListener<WifiPort>() {

        @Override
        public void onPortUpdate(final WifiPort port) {
            port.removePortListener(wifiPortListener);
            EWSLogger.d(TAG, "start onPortUpdate for :" + requestType);
            WifiPortProperties wifiPortProperties = port.getPortProperties();

            if (wifiPortProperties != null) {
                switch (requestType) {
                    case ApplianceRequestType.GET_WIFI_PROPS:
                        EWSLogger.d(EWS_STEPS, "Step 3 : Got wifi properties, showing the password entry screen");
                        if (fetchCallback != null) {
                            fetchCallback.onDeviceInfoReceived(wifiPortProperties);
                        }
                        break;
                    case ApplianceRequestType.PUT_WIFI_PROPS:
                        EWSLogger.d(EWS_STEPS, "Step 4.1 : Setting the wifi properties to the device succesfull");
                        if (putCallback != null) {
                            putCallback.onPropertiesSet(wifiPortProperties);
                        }
                        break;
                    default:
                        EWSLogger.e(TAG, "Unknown request type");
                        break;
                }
                requestType = ApplianceRequestType.UNKNOWN;
                EWSLogger.d("TAG", "stop onPortUpdate for :" + requestType);
            }
        }

        @Override
        public void onPortError(WifiPort wifiPort, Error error, @Nullable String s) {
            EWSLogger.d(EWS_STEPS, "Step Failed : Port error " + wifiPort.toString() + " Error : " + error + " data " + error);
            onErrorReceived();
            if (fetchCallback != null) {
                fetchCallback.onFailedToFetchDeviceInfo();
            }
            if (putCallback != null) {
                //TODO handle failed set
                putCallback.onFailedToSetProperties();
            }
        }

    };

    private DICommPortListener<DevicePort> devicePortListener = new DICommPortListener<DevicePort>() {

        @Override
        public void onPortUpdate(final DevicePort port) {
            DevicePortProperties devicePortProperties = port.getPortProperties();
            if (devicePortProperties != null) {
                sessionDetailsInfo.setDevicePortProperties(devicePortProperties);
                fetchWiFiPortProperties();
            }
        }

        @Override
        public void onPortError(final DevicePort port, final Error error, final String errorData) {
            onErrorReceived();
        }
    };

    @Inject
    public ApplianceAccessManager(@NonNull @Named("ews.event.bus") EventBus eventBus,
                                  @NonNull final @Named("ews.temporary.appliance") EWSGenericAppliance appliance,
                                  @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo) {
        this.eventBus = eventBus;
        this.appliance = appliance;
        this.sessionDetailsInfo = sessionDetailsInfo;
    }

    public void fetchDevicePortProperties(@NonNull FetchCallback callback) {
        EWSLogger.d(TAG, "STEP 2 : Appliance found, fetching properties from device");
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
                EWSLogger.d("TAG", "onPortError for requestType:" + requestType);
                eventBus.post(new DeviceConnectionErrorEvent());
                break;
            case ApplianceRequestType.PUT_WIFI_PROPS:
                eventBus.post(new ApplianceConnectErrorEvent(ConnectionErrorType.WRONG_CREDENTIALS));
                break;
            default:
                EWSLogger.e(TAG, "Unknown request type in properties");
                break;
        }

    }

    public void connectApplianceToHomeWiFiEvent(@NonNull final String homeWiFiSSID, @NonNull final String homeWiFiPassword, @NonNull final SetPropertiesCallback callback) {
        if (requestType == ApplianceRequestType.UNKNOWN) {
            putCallback = callback;
            requestType = ApplianceRequestType.PUT_WIFI_PROPS;
            this.homeWiFiSSID = homeWiFiSSID;
            WifiPort wifiPort = appliance.getWifiPort();
            wifiPort.addPortListener(wifiPortListener);

            wifiPort.setWifiNetworkDetails(homeWiFiSSID, homeWiFiPassword);
            EWSLogger.d(EWS_STEPS, "Step 4 : Setting the wifi properties to the device");
        } else {
            EWSLogger.d("TAG", "PUT_WIFI_PROPS requestType:" + requestType);
        }
    }

    @VisibleForTesting
    DICommPortListener<DevicePort> getDevicePortListener() {
        return devicePortListener;
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