/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.ews.annotations.ApplianceRequestType;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.annotations.ApplianceRequestType.GET_WIFI_PROPS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSLogger.class, EWSTagger.class})
public class ApplianceAccessManagerTest {

    private static final String HOME_WIFI_PASSWORD = "BrightEyes123";
    private static final String HOME_WIFI_SSID = "BrightEyes2.4";

    @Mock
    private DevicePort devicePortMock;
    @Mock
    private WifiPort wifiPortMock;
    @Mock
    private EWSGenericAppliance applianceMock;
    @Mock
    private ApplianceAccessManager.FetchCallback mockFetchCallback;
    @Mock
    private ApplianceAccessManager.SetPropertiesCallback mockSetPropertiesCallback;

    @Mock
    private EWSLogger mockEWSLogger;

    @Mock
    private EWSTagger mockEWSTagger;

    private ApplianceAccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        mockStatic(EWSLogger.class);
        stubAppliancePorts();
        accessManager = new ApplianceAccessManager(applianceMock, mockEWSTagger, mockEWSLogger);
    }

    private void stubAppliancePorts() {
        when(applianceMock.getDevicePort()).thenReturn(devicePortMock);
        when(applianceMock.getWifiPort()).thenReturn(wifiPortMock);
    }

    //// TODO: 26/05/17  this is workaround fix to pass tests till we get an update from dicomm.
    @Test
    public void itShouldFetchApplianceDevicePortPropertiesWhenAsked() throws Exception {
        accessManager.fetchDevicePortProperties(null);
        verify(wifiPortMock).reloadProperties();
        verifyRequestType(ApplianceRequestType.GET_WIFI_PROPS);
    }

    //// TODO: 26/05/17  this is workaround fix to pass tests till we get an update from dicomm.
    @Test
    public void itShouldNotFetchApplianceDevicePortPropertiesIfAnotherRequestIsInProgress() throws Exception {
        accessManager.fetchDevicePortProperties(null);
        accessManager.fetchDevicePortProperties(null);
        accessManager.fetchDevicePortProperties(null);
        verify(wifiPortMock, times(1)).reloadProperties();
    }

    @Test
    public void itShouldSaveApplianceWiFIPortSessionDetailsWhenGetWiFIPortPropertiesAreFetched() throws Exception {
        fetchWiFiProperties(GET_WIFI_PROPS);
        verifyRequestType(ApplianceRequestType.UNKNOWN);
    }

    @Test
    public void itShouldSendEventToShowNextScreenWhenWiFiPortPropertiesAreReadSuccessfully() throws Exception {
        fetchWiFiProperties(GET_WIFI_PROPS);
        verify(mockFetchCallback).onDeviceInfoReceived(any(WifiPortProperties.class));
    }

    @Test
    public void itShouldSendPairingSuccessEventWhenApplianceIsConnectedToHomeWifi() throws Exception {
        connectApplianceToHomeWiFi();
        accessManager.getWifiPortListener().onPortUpdate(wifiPortMock);
        verify(wifiPortMock).setWifiNetworkDetails(HOME_WIFI_SSID, HOME_WIFI_PASSWORD);
    }

    @Test
    public void itShouldNotifyCallbackWhenInfoIsPutIntoDevice() throws Exception {
        connectApplianceToHomeWiFi();
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.PUT_WIFI_PROPS);
        accessManager.getWifiPortListener().onPortUpdate(wifiPortMock);
        verify(mockSetPropertiesCallback).onPropertiesSet(any(WifiPortProperties.class));
    }

    @Test
    public void itShouldNotifyCallbackWhenInfoApplianceRequestTypeUNKNOWN() throws Exception {
        connectApplianceToHomeWiFi();
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.UNKNOWN);
        accessManager.getWifiPortListener().onPortUpdate(wifiPortMock);
        verify(mockEWSLogger).e("ApplianceAccessManager","Unknown request type");
    }

    @Test
    public void itShouldNotifyCallbackWhenInfoApplianceRequestTypeUNKNOWNAndSetPropertiesCallbackNotNull() throws Exception {
        connectApplianceToHomeWiFi();
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.UNKNOWN);
        accessManager.getWifiPortListener().onPortError(wifiPortMock, Error.UNKNOWN, "");
        verify(mockEWSTagger).trackActionSendData("technicalError", "EWS:Network:AWSDK:wifiPortError");
        verify(mockSetPropertiesCallback).onFailedToSetProperties();
    }

    private void verifyRequestType(final int requestType) {
        assertEquals(requestType, accessManager.getRequestType());
    }

    @Test
    public void itShouldSendDeviceConnectionErrorEventWhenGetWifiPropsOnErrorReceived() {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.GET_WIFI_PROPS);
        accessManager.getWifiPortListener().onPortError(wifiPortMock, Error.UNKNOWN, "");
        verify(mockEWSTagger).trackActionSendData("technicalError", "EWS:Network:AWSDK:wifiPortError");
    }

    @Test
    public void itShouldSendDeviceConnectionErrorEventWhenGetWifiPropsOnErrorReceivedAndGiveListenBackToFetchDeviceInfo() {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.UNKNOWN);
        accessManager.fetchDevicePortProperties(mockFetchCallback);
        accessManager.getWifiPortListener().onPortError(wifiPortMock, Error.UNKNOWN, "");
        verify(mockEWSTagger).trackActionSendData("technicalError", "EWS:Network:AWSDK:wifiPortError");
        verify(mockFetchCallback).onFailedToFetchDeviceInfo();
    }

    @Test
    public void itShouldSendApplianceConnectErrorEventWhenPutDevicePropsOnErrorReceived() {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.PUT_WIFI_PROPS);
        accessManager.getWifiPortListener().onPortError(wifiPortMock, Error.UNKNOWN, "");
    }

    @Test
    public void itShouldDoNothingWhenWifiPortPropertiesAreNull() {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.PUT_WIFI_PROPS);
        accessManager.getWifiPortListener().onPortUpdate(wifiPortMock);
    }

    @Test
    public void itShouldDoNothingWhenConnectingToApplianceIsCalledButStateIsNotUnknown() throws Exception {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.GET_WIFI_PROPS);
        connectApplianceToHomeWiFi();
        verifyZeroInteractions(wifiPortMock, applianceMock);
    }

    @Test
    public void itShouldDoNothingWhenWifiPortErrorReceivedButTypeIsUnknown() throws Exception {
        accessManager.setApplianceWifiRequestType(ApplianceRequestType.UNKNOWN);
        accessManager.getWifiPortListener().onPortError(wifiPortMock, Error.UNKNOWN, "hypothetical");
        verifyRequestType(ApplianceRequestType.UNKNOWN);
    }

    private WifiPortProperties fetchWiFiProperties(final @ApplianceRequestType int type) {
        WifiPortProperties wifiPortProperties = mock(WifiPortProperties.class);
        when(wifiPortMock.getPortProperties()).thenReturn(wifiPortProperties);
        accessManager.fetchDevicePortProperties(mockFetchCallback);
        accessManager.setApplianceWifiRequestType(type);
        accessManager.getWifiPortListener().onPortUpdate(wifiPortMock);
        return wifiPortProperties;
    }

    private void connectApplianceToHomeWiFi() {
        WifiPortProperties wifiPortProperties = mock(WifiPortProperties.class);
        when(wifiPortMock.getPortProperties()).thenReturn(wifiPortProperties);
        accessManager.connectApplianceToHomeWiFiEvent(HOME_WIFI_SSID, HOME_WIFI_PASSWORD, mockSetPropertiesCallback);
    }
}