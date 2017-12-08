/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ContextProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Map;

public class UdpReceivingThread extends Thread {

    private static final int UDP_PORT = 8080;
    private final UdpEventListener mUdpEventListener;

    private DatagramSocket socket;
    private boolean stop;
    private MulticastLock multicastLock;

    public UdpReceivingThread(UdpEventListener udpEventListener) {
        mUdpEventListener = udpEventListener;
    }

    @Override
    public void run() {
        DICommLog.i(DICommLog.UDP, "Started UDP socket");
        try {
            acquireMulticastLock();

            socket = new DatagramSocket(UDP_PORT);

        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (!stop) {
            receiveDatagram();
        }
        DICommLog.i(DICommLog.UDP, "Stopped UDP Socket");
    }

    @VisibleForTesting
    protected void receiveDatagram() {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket = createSocket();
            socket.receive(packet);

            String packetReceived = readPacketData(packet);
            if (packetReceived != null && packetReceived.length() > 0) {
                String[] packetsReceived = packetReceived.split("\n");
                if (packetsReceived != null && packetsReceived.length > 0) {
                    String senderIp = "";
                    try {
                        senderIp = readPacketSender(packet);
                    } catch (Exception e) {
                    }

                    DICommLog.d(DICommLog.UDP, "UDP Data Received from: " + senderIp);
                    String urlHeader = packetsReceived[0];
                    String portName = parseRequestHeaderLine(urlHeader);
                    String lastLine = packetsReceived[packetsReceived.length - 1];

                    //parsedUrlParts.get("port");
                    mUdpEventListener.onUDPEventReceived(lastLine, portName, senderIp);

                } else {
                    DICommLog.d(DICommLog.UDP, "Couldn't split receiving packet: " + packetReceived);
                }
            }


        } catch (IOException e) {
            DICommLog.d(DICommLog.UDP, "UDP exception: " + "Error: " + e.getMessage());
        } catch (NullPointerException e2) {
            // NOP -  Received after attempt to close socket.
            DICommLog.d(DICommLog.UDP, "UDP exception: " + e2.getMessage());
        }
    }

    public void stopThread() {
        DICommLog.d(DICommLog.UDP, "Requested to stop UDP socket");
        stop = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
        releaseMulticastLock();
    }

    protected DatagramSocket createSocket() throws SocketException {
        if (socket == null) {
            socket = new DatagramSocket(UDP_PORT);
        }
        return socket;
    }

    @NonNull
    protected String readPacketData(DatagramPacket packet) {
        return new String(packet.getData(), Charset.defaultCharset()).trim();
    }

    protected String readPacketSender(DatagramPacket packet) {
        return packet.getAddress().getHostAddress();
    }

    private void acquireMulticastLock() {
        final Context context = ContextProvider.get();
        if (context == null) {
            return;
        }
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifi != null) {
            multicastLock = wifi.createMulticastLock(getName());
            multicastLock.setReferenceCounted(true);
            multicastLock.acquire();
            DICommLog.d(DICommLog.UDP, "Acquired MulticastLock");
        }
    }

    private void releaseMulticastLock() {
        if (multicastLock == null) return;

        multicastLock.release();
        multicastLock = null;
        DICommLog.d(DICommLog.UDP, "Released MulticastLock");
    }

    private String parseRequestHeaderLine(String headerLine) {
        String[] methodSplit = headerLine.split(" ");
        String[] urlSplit = methodSplit[1].split("/");

        String portName = "";
        for(int u = 5; u < urlSplit.length; u++) {
            portName += urlSplit[u];
            if(u < urlSplit.length - 1) {
                portName += "/";
            }
        }
        return portName;
    }
}
