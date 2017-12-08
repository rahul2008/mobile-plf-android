/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan.subscription;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.util.ContextProvider;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UdpReceivingThreadTest extends RobolectricTest {

    private static final String UDP_PAYLOAD = "Wh9uPTclcEMwlUBOMKvssNieG3ZCthAZKubKkZEqR6LkWJspvLVgE0YMIiqvJF4cbitBrfJGa8U4AdzgND97YeW1NMvpxG1zrva+uK1xtTPvwiPieSlrdlTxoE5RCVDfVx16k++O66GxvgN62feOjYCgEpntVwZqtHGxjdyOh4TIbEJXvPuplQ/k5KgJdQxVjgWYKNEbRfAO8Io1RdEcljMlkR098t38KkXasiQcyXN/CDbk/23jX9Uet8ySrZy6VA9fkjDeH/9H9bXUPTRYhq1426MHugCL5e7W5onxOlY=";
    private static final String UDP_HEADERS =
            "Accept-Encoding: identity\n" +
            "Host: 192.168.1.255:8080\n" +
            "Content-Type: application/octet-stream\n" +
            "Content-Transfer-Encoding: base64\n" +
            "Content-Length: 300\n" +
            "x-di-udp-count:          0\n" +
            "\n";
    private static final String SENDER_ADDRESS = "127.0.0.1";

    @Mock
    private Application contextMock;
    @Mock
    private UdpEventListener eventListenerMock;
    @Mock
    private WifiManager wifiMock;
    @Mock
    private WifiManager.MulticastLock lockMock;
    @Mock
    private DatagramSocket socketMock;

    private UdpReceivingThread thread;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ContextProvider.setTestingContext(contextMock);

        when(contextMock.getApplicationContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiMock);
        when(wifiMock.createMulticastLock(anyString())).thenReturn(lockMock);
        mockSocketReceive();
    }

    @Test
    public void givenThreadSetUp_whenValidUdpPacketReceived_thenShouldCallListener() throws Exception {
        String udpData = generateTestData("/di/v1/products/1/polarisrobot", UDP_PAYLOAD);
        createObjectUnderTest(udpData, socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verify(eventListenerMock).onUDPEventReceived(UDP_PAYLOAD, "polarisrobot", SENDER_ADDRESS);
    }

    @Test
    public void givenThreadSetUp_whenEmptyUdpPacketReceived_thenShouldNotCallListener() throws Exception {
        createObjectUnderTest("", socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verifyNoMoreInteractions(eventListenerMock);
    }

    @Test
    public void givenThreadSetUp_whenEmptyLinesUdpPacketReceived_thenShouldNotCallListener() throws Exception {
        createObjectUnderTest("\n", socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verifyNoMoreInteractions(eventListenerMock);
    }

    @Test
    public void givenThreadSetUp_whenNestedPortInUrlOfUdpPacket_thenShouldCallListener() throws Exception {
        String udpData = generateTestData("/di/v1/products/1/schedule/1", UDP_PAYLOAD);
        createObjectUnderTest(udpData, socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verify(eventListenerMock).onUDPEventReceived(UDP_PAYLOAD, "schedule/1", SENDER_ADDRESS);
    }

    @Test
    public void givenThreadSetUp_whenAirPortInUrlOfUdpPacket_thenShouldCallListener() throws  Exception {
        String udpData = generateTestData("/di/v1/products/1/air", UDP_PAYLOAD);
        createObjectUnderTest(udpData, socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verify(eventListenerMock).onUDPEventReceived(UDP_PAYLOAD, "air", SENDER_ADDRESS);
    }

    @Test
    public void givenThreadSetUp_whenNestedAirPortInUrlOfUdpPacket_thenShouldCallListener() throws  Exception {
        String udpData = generateTestData("/di/v1/products/1/air/nested", UDP_PAYLOAD);
        createObjectUnderTest(udpData, socketMock, SENDER_ADDRESS);

        thread.receiveDatagram();

        verify(eventListenerMock).onUDPEventReceived(UDP_PAYLOAD, "air/nested", SENDER_ADDRESS);
    }

    private String generateTestData(String url, String payload) {
        return "PUT " + url + " HTTP/1.1\n" + UDP_HEADERS + payload;
    }

    private void mockSocketReceive() throws IOException {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(socketMock).receive(any(DatagramPacket.class));
    }

    private void createObjectUnderTest(final String testPacketData, final DatagramSocket testSocket, final String testSender) {
        thread = new UdpReceivingThread(eventListenerMock) {
            @NonNull
            @Override
            protected String readPacketData(DatagramPacket packet) {
                return testPacketData;
            }

            @Override
            protected DatagramSocket createSocket() throws SocketException {
                return testSocket;
            }

            @Override
            protected String readPacketSender(DatagramPacket packet) {
                return testSender;
            }
        };
    }
}