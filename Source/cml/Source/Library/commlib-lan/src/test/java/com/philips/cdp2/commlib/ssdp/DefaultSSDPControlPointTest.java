/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.cdp2.commlib.ssdp.DefaultSSDPControlPoint.DeviceListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import static com.philips.cdp2.commlib.ssdp.SSDPDevice.createFromSearchResponse;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.BOOT_ID;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.LOCATION;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_ALIVE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_BYEBYE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NOTIFICATION_SUBTYPE_UPDATE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.USN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SSDPDevice.class, DefaultSSDPControlPoint.class})
public class DefaultSSDPControlPointTest {

    private static final String TEST_LOCATION = "http://1.2.3.4/mock/location";
    private static final String TEST_USN = "uuid:2f402f80-da50-11e1-9b23-00123456789f";
    private static final String TEST_BOOTID = "1337";
    private static final String TEST_LOCATION_2 = "http://2.3.4.5/mock/location";
    private static final String TEST_USN_2 = "uuid:2f402f80-11e1-9b23-da50-00123456789f";
    private static final String TEST_BOOTID_2 = "2337";

    private DefaultSSDPControlPoint ssdpControlPoint;

    @Mock
    private DeviceListener deviceListener;

    @Mock
    private SSDPMessage ssdpMessageMock;

    @Mock
    private SSDPMessage secondSsdpMessageMock;

    @Mock
    private SSDPDevice ssdpDeviceMock;

    @Mock
    private SSDPDevice secondSsdpDeviceMock;

    @Mock
    private Context contextMock;

    @Mock
    private WifiManager wifiManagerMock;

    @Mock
    private WifiManager.MulticastLock lockMock;

    @Mock
    private MulticastSocket broadcastSocketMock;

    @Mock
    private MulticastSocket listenSocketMock;

    @Mock
    private ExecutorService mockDescriptionReceiverExecutor;

    @Captor
    private ArgumentCaptor<Runnable> fetchDescriptionRunnableCaptor;

    @Before
    public void setUp() {
        initMocks(this);

        ContextProvider.setTestingContext(contextMock);
        when(contextMock.getApplicationContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManagerMock);
        when(wifiManagerMock.createMulticastLock(anyString())).thenReturn(lockMock);
        when(lockMock.isHeld()).thenReturn(true);
        when(ssdpMessageMock.get(USN)).thenReturn(TEST_USN);
        when(ssdpMessageMock.get(BOOT_ID)).thenReturn(TEST_BOOTID);
        when(ssdpMessageMock.get(LOCATION)).thenReturn(TEST_LOCATION);
        when(secondSsdpMessageMock.get(USN)).thenReturn(TEST_USN_2);
        when(secondSsdpMessageMock.get(BOOT_ID)).thenReturn(TEST_BOOTID_2);
        when(secondSsdpMessageMock.get(LOCATION)).thenReturn(TEST_LOCATION_2);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                fetchDescriptionRunnableCaptor.getValue().run();
                return null;
            }
        }).when(mockDescriptionReceiverExecutor).execute(fetchDescriptionRunnableCaptor.capture());

        mockStatic(SSDPDevice.class);
        when(createFromSearchResponse(ssdpMessageMock)).thenReturn(ssdpDeviceMock);

        ssdpControlPoint = new DefaultSSDPControlPoint() {
            @NonNull
            @Override
            MulticastSocket createBroadcastSocket() throws IOException {
                return broadcastSocketMock;
            }

            @NonNull
            @Override
            MulticastSocket createListenSocket() throws IOException {
                return listenSocketMock;
            }

            @NonNull
            @Override
            ExecutorService initDescriptionReceiverThreadpool() {
                return mockDescriptionReceiverExecutor;
            }
        };

        DICommLog.disableLogging();
    }

    @After
    public void tearDown() throws Exception {
        if (ssdpControlPoint != null && ssdpControlPoint.isDiscovering()) {
            ssdpControlPoint.stop();
        }
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

    @Test
    public void whenAnSsdpAliveMessageIsReceived_andAnSsdpByeByeMessageIsReceived_thenAnUnavailableSsdpDeviceShouldBeNotified() {
        ssdpControlPoint.addDeviceListener(deviceListener);

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_ALIVE);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_BYEBYE);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener).onDeviceUnavailable(any(SSDPDevice.class));
    }

    @Test
    public void whenAnSsdpMessageIsReceivedThatHasNoUsnInHeader_thenItIsIgnored() {
        ssdpControlPoint.addDeviceListener(deviceListener);

        when(ssdpMessageMock.get(USN)).thenReturn(null);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verifyNoMoreInteractions(deviceListener);
    }

    @Test
    public void whenControlPointIsCreated_thenSocketsAreNotOpened() throws IOException {

        verify(broadcastSocketMock, never()).joinGroup(any(InetAddress.class));
        verify(listenSocketMock, never()).joinGroup(any(InetAddress.class));
    }

    @Test
    public void whenControlPointIsCreated_thenMulticastLockIsNotAcquired() {

        verify(lockMock, never()).acquire();
    }

    @Test
    public void givenControlPointIsCreated_whenStartIsInvoked_thenSocketsAreOpened() throws IOException {

        ssdpControlPoint.start();

        verify(broadcastSocketMock).joinGroup(any(InetAddress.class));
        verify(listenSocketMock).joinGroup(any(InetAddress.class));
    }

    @Test
    public void whenStartIsInvoked_thenMulticastLockIsAcquired() {

        ssdpControlPoint.start();

        verify(lockMock).acquire();
    }

    @Test
    public void givenControlPointIsStarted_whenStopIsInvoked_thenSocketsAreClosed() throws IOException {
        ssdpControlPoint.start();

        ssdpControlPoint.stop();

        verify(broadcastSocketMock).leaveGroup(any(InetAddress.class));
        verify(broadcastSocketMock).close();
        verify(listenSocketMock).leaveGroup(any(InetAddress.class));
        verify(listenSocketMock).close();
    }

    @Test
    public void givenControlPointIsStarted_whenStopIsInvoked_thenMulticastLockIsReleased() {
        ssdpControlPoint.start();

        ssdpControlPoint.stop();

        verify(lockMock).release();
    }

    @Test
    public void givenDeviceNotDiscovered_whenDeviceDiscoveredFirstTime_thenDescriptionXmlIsRetrieved() {

        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verifyStatic(times(1));
        createFromSearchResponse(ssdpMessageMock);
    }

    @Test
    public void givenDeviceDiscovered_whenSameDeviceDiscoveredAgain_thenDescriptionXmlIsNotRetrievedAgain() {
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verifyStatic(times(1));
        createFromSearchResponse(any(SSDPMessage.class));
    }

    @Test
    public void givenDeviceDiscovered_whenSameDeviceDiscoveredAgain_thenListenerIsNotifiedTwice() {
        ssdpControlPoint.addDeviceListener(deviceListener);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener, times(2)).onDeviceAvailable(any(SSDPDevice.class));
    }

    @Test
    public void givenDeviceDiscovered_whenSameDeviceDiscoveredAgain_thenDeviceInfoGetsUpdated() throws Exception {

        ssdpControlPoint.handleMessage(ssdpMessageMock);

        final SSDPMessage secondMessageMock = mock(SSDPMessage.class);
        when(secondMessageMock.get(USN)).thenReturn(TEST_USN);
        ssdpControlPoint.handleMessage(secondMessageMock);

        verify(ssdpDeviceMock).updateFrom(secondMessageMock);
    }

    @Test
    public void givenControlPointIsStarted_whenStartedAgain_thenSecondStartIsIgnored() throws Exception {
        ssdpControlPoint.start();

        ssdpControlPoint.start();

        verify(broadcastSocketMock, times(1)).joinGroup(any(InetAddress.class));
        verify(listenSocketMock, times(1)).joinGroup(any(InetAddress.class));
    }

    @Test
    public void givenControlPointIsStarted_AndStopped_WhenStartedAgain_thenSecondStartIsPerformed() throws Exception {
        ssdpControlPoint.start();
        ssdpControlPoint.stop();

        ssdpControlPoint.start();

        verify(broadcastSocketMock, times(2)).joinGroup(any(InetAddress.class));
        verify(listenSocketMock, times(2)).joinGroup(any(InetAddress.class));
    }

    @Test
    public void whenTwoDifferentSsdpAliveMessagesAreReceived_thenTheyShouldBeHandledInParallel() {
        ssdpControlPoint = new DefaultSSDPControlPoint() {
            @NonNull
            @Override
            MulticastSocket createBroadcastSocket() throws IOException {
                return broadcastSocketMock;
            }

            @NonNull
            @Override
            MulticastSocket createListenSocket() throws IOException {
                return listenSocketMock;
            }

            // do not override the threadpool init
        };
        ssdpControlPoint.addDeviceListener(deviceListener);

        final Semaphore fetchDescriptionSemaphore = new Semaphore(0);
        when(createFromSearchResponse(secondSsdpMessageMock)).thenAnswer(new Answer<SSDPDevice>() {
            @Override
            public SSDPDevice answer(InvocationOnMock invocation) throws Throwable {
                fetchDescriptionSemaphore.acquire();
                return secondSsdpDeviceMock;
            }
        });

        when(ssdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_ALIVE);
        when(secondSsdpMessageMock.get(NOTIFICATION_SUBTYPE)).thenReturn(NOTIFICATION_SUBTYPE_ALIVE);

        ssdpControlPoint.handleMessage(secondSsdpMessageMock);
        ssdpControlPoint.handleMessage(ssdpMessageMock);

        verify(deviceListener, after(50)).onDeviceAvailable(ssdpDeviceMock);

        fetchDescriptionSemaphore.release();

        verify(deviceListener, after(50)).onDeviceAvailable(secondSsdpDeviceMock);
    }

    

    @Test(expected = TransportUnavailableException.class)
    public void givenSocketsCannotBeOpened_whenStartIsInvoked_thenATransportUnavailableExceptionIsThrown() throws SocketException {
        doThrow(IOException.class).when(broadcastSocketMock).bind(null);

        ssdpControlPoint.start();
    }

    @Test(expected = TransportUnavailableException.class)
    public void givenStateCanNotBeReused_whenStartIsInvoked_thenATransportUnavailableExceptionIsThrown() throws SocketException {
        doThrow(IllegalStateException.class).when(broadcastSocketMock).bind(null);

        ssdpControlPoint.start();
    }
}
