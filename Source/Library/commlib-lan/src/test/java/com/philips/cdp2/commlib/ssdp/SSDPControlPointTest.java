/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.util.HTTP;
import com.philips.cdp2.commlib.lan.util.HTTP.RequestCallback;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint.DeviceListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.URL;

import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SSDPControlPointTest extends RobolectricTest {

    private static final String MOCK_LOCATION = "http://1.2.3.4/mock/location";
    private static final String MOCK_DESCRIPTION_XML = "<root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><URLBase>http://192.168.1.100:80/</URLBase><device><deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType><friendlyName>Hue Bridge (192.168.1.100)</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><manufacturerURL>http://www.philips.com</manufacturerURL><modelDescription>Philips hue Personal Wireless Lighting</modelDescription><modelName>Philips hue bridge 2015</modelName><modelNumber>BSB002</modelNumber><modelURL>http://www.meethue.com</modelURL><serialNumber>00123456789</serialNumber><UDN>uuid:2f402f80-da50-11e1-9b23-00123456789f</UDN><presentationURL>index.html</presentationURL><iconList><icon><mimetype>image/png</mimetype><height>48</height><width>48</width><depth>24</depth><url>hue_logo_0.png</url></icon></iconList></device></root>";

    private SSDPControlPoint ssdpControlPoint;

    @Mock
    private DeviceListener deviceListener;

    @Mock
    HTTP http;

    @Mock
    private SSDPMessage ssdpMessageMock;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        when(ssdpMessageMock.get(SSDPMessage.LOCATION)).thenReturn(MOCK_LOCATION);

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                final RequestCallback requestCallback = invocation.getArgumentAt(2, RequestCallback.class);
                requestCallback.onResponse(MOCK_DESCRIPTION_XML);

                return null;
            }
        }).when(http).get(any(URL.class), anyInt(), any(RequestCallback.class));

        ssdpControlPoint = new SSDPControlPoint() {
            @Override
            HTTP createHttp() {
                return http;
            }
        };
    }

    @Test
    public void whenSsdpControlPointIsStarted_thenItShouldReportStarted() {
        ssdpControlPoint.start();

        assertThat(ssdpControlPoint.isStarted());
    }

    @Test
    public void whenSsdpControlPointIsStopped_thenItShouldNotReportStarted() {
        ssdpControlPoint.stop();

        assertThat(ssdpControlPoint.isStarted()).isFalse();
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
