/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.util.DICommLog;

public class UdpReceivingThread extends Thread {

	private static final int UDP_PORT = 8080;
	private final UdpEventListener mUdpEventListener;

	private DatagramSocket socket ;
	private boolean stop ;
	private MulticastLock multicastLock ;

	public UdpReceivingThread(UdpEventListener udpEventListener) {
		mUdpEventListener = udpEventListener;
	}

	@Override
	public void run() {
		DICommLog.i(DICommLog.UDP, "Started UDP socket") ;
		try {
			acquireMulticastLock();

			socket = new DatagramSocket(UDP_PORT) ;

		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (!stop ) {
			byte [] buf = new byte[1024] ;
			DatagramPacket packet = new DatagramPacket(buf, buf.length) ;
			try {
				if (socket == null) {
					socket = new DatagramSocket(UDP_PORT) ;
				}
				socket.receive(packet) ;

				String packetReceived = new String(packet.getData(), Charset.defaultCharset()).trim();
				if( packetReceived != null &&  packetReceived.length() > 0) {
					String [] packetsReceived = packetReceived.split("\n") ;
					if(packetsReceived != null && packetsReceived.length > 0 ) {
						String senderIp = "";
						try {
							senderIp = packet.getAddress().getHostAddress();
						} catch (Exception e) {}

						DICommLog.d(DICommLog.UDP, "UDP Data Received from: " + senderIp) ;
						String lastLine = packetsReceived[packetsReceived.length-1];
						mUdpEventListener.onUDPEventReceived(lastLine, senderIp) ;

					} else {
						DICommLog.d(DICommLog.UDP, "Couldn't split receiving packet: " + packetReceived);
					}
				}


			} catch (IOException e) {
				DICommLog.d(DICommLog.UDP, "UDP exception: " + "Error: " + e.getMessage()) ;
			} catch (NullPointerException e2) {
				// NOP -  Received after attempt to close socket.
				DICommLog.d(DICommLog.UDP, "UDP exception: " + e2.getMessage());
			}
		}
		DICommLog.i(DICommLog.UDP, "Stopped UDP Socket") ;
	}

	public void stopThread() {
		DICommLog.d(DICommLog.UDP, "Requested to stop UDP socket") ;
		stop = true ;
		if (socket != null && !socket.isClosed()) {
			socket.close() ;
			socket = null ;
		}
		releaseMulticastLock();
	}

	private void acquireMulticastLock() {
		WifiManager wifi = (WifiManager) DICommClientWrapper.getContext().getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) {
			multicastLock = wifi.createMulticastLock(getName());
			multicastLock.setReferenceCounted(true);
			multicastLock.acquire();
			DICommLog.d(DICommLog.UDP, "Aquired MulticastLock") ;
		}
	}

	private void releaseMulticastLock() {
		if (multicastLock == null) return;

		multicastLock.release();
		multicastLock = null;
		DICommLog.d(DICommLog.UDP, "Released MulticastLock") ;
	}
}
