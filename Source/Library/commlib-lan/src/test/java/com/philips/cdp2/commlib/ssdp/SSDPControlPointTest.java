/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint.DeviceListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;

import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SSDPDevice.class})
public class SSDPControlPointTest {

    private static final String MOCK_LOCATION = "http://1.2.3.4/mock/location";

    private SSDPControlPoint ssdpControlPoint;

    @Mock
    private DeviceListener deviceListener;

    @Mock
    private SSDPMessage ssdpMessageMock;

    @Mock
    private SSDPDevice ssdpDeviceMock;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        when(ssdpMessageMock.get(SSDPMessage.LOCATION)).thenReturn(MOCK_LOCATION);

        mockStatic(SSDPDevice.class);
        when(SSDPDevice.createFromUrl(any(URL.class))).thenReturn(ssdpDeviceMock);

        ssdpControlPoint = new SSDPControlPoint();
    }

    @Test
    public void whenAnSsdpAliveMessageIsReceived_thenAnAvailableSsdpDeviceShouldBeNotified() {
        ssdpControlPoint.addDeviceListener(deviceListener);

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_ALIVE);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener).onDeviceAvailable(any(SSDPDevice.class));
    }

    @Test
    public void whenAnSsdpUpdateMessageIsReceived_thenAnAvailableSsdpDeviceShouldBeNotified() {
        ssdpControlPoint.addDeviceListener(deviceListener);

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_UPDATE);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener).onDeviceAvailable(any(SSDPDevice.class));
    }

    @Test
    public void whenAnSsdpByeByeMessageIsReceived_thenAnUnavailableSsdpDeviceShouldBeNotified() {
        ssdpControlPoint.addDeviceListener(deviceListener);

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_BYEBYE);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener).onDeviceUnavailable(any(SSDPDevice.class));
    }

}
