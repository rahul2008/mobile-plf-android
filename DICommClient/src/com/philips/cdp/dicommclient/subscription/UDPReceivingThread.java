package com.philips.cdp.dicommclient.subscription;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.philips.cdp.dicomm.util.ALog;
import com.philips.cdp.dicomm.util.DICommContext;

public class UDPReceivingThread extends Thread {

	private static final int UDP_PORT = 8080 ;
	private static List<UDPEventListener> udpEventListenerList ;
	private static UDPReceivingThread udpReceivingThread;

	private DatagramSocket socket ;
	private boolean stop ;
	private MulticastLock multicastLock ;

	private UDPReceivingThread() {

	}

	public static synchronized UDPReceivingThread getInstance() {
		if( udpReceivingThread == null ) {
			udpReceivingThread = new UDPReceivingThread() ;
			udpEventListenerList = new ArrayList<UDPEventListener>() ;
		}
		return udpReceivingThread ;
	}

	public void addUDPEventListener(UDPEventListener udpEventListener) {
		if( udpEventListenerList != null && !udpEventListenerList.contains(udpEventListener)) {
			udpEventListenerList.add(udpEventListener) ;
		}
	}

	public void removeUDPEventListener(UDPEventListener udpEventListener) {
		if( udpEventListenerList != null ) {
			udpEventListenerList.remove(udpEventListener) ;
		}
	}

	@Override
	public void run() {
		ALog.i(ALog.UDP, "Started UDP socket") ;
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
				if( packetReceived != null &&  packetReceived.length() > 0 && udpEventListenerList != null) {
					String [] packetsReceived = packetReceived.split("\n") ;
					if(packetsReceived != null && packetsReceived.length > 0 ) {
						String senderIp = "";
						try {
							senderIp = packet.getAddress().getHostAddress();
						} catch (Exception e) {}

						ALog.d(ALog.UDP, "UDP Data Received from: " + senderIp) ;
						String lastLine = packetsReceived[packetsReceived.length-1];
						notifyUDPEventListeners(lastLine, senderIp) ;

					} else {
						ALog.d(ALog.UDP, "Couldn't split receiving packet: " + packetReceived);
					}
				}


			} catch (IOException e) {
				ALog.d(ALog.UDP, "UDP exception: " + "Error: " + e.getMessage()) ;
			} catch (NullPointerException e2) {
				// NOP -  Received after attempt to close socket.
				ALog.d(ALog.UDP, "UDP exception: " + e2.getMessage());
			}
		}
		ALog.i(ALog.UDP, "Stopped UDP Socket") ;
	}

	private void notifyUDPEventListeners(String lastLine, String senderIp) {
		if(udpEventListenerList != null ) {
			for (UDPEventListener udpEventListener : udpEventListenerList) {
				udpEventListener.onUDPEventReceived(lastLine, senderIp) ;
			}
		}

	}

	public void stopUDPListener() {
		ALog.d(ALog.UDP, "Requested to stop UDP socket") ;
		if(udpEventListenerList!=null && udpEventListenerList.size() ==0 ){
		stop = true ;
		if( socket != null && !socket.isClosed()) {
			socket.close() ;
			socket = null ;
		}
		releaseMulticastLock();
		reset();
		}
	}

	private void reset() {
		udpReceivingThread = null ;
		udpEventListenerList = null ;
	}

	private void acquireMulticastLock() {
		WifiManager wifi = (WifiManager) DICommContext.getContext().getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) {
			multicastLock = wifi.createMulticastLock(getName());
			multicastLock.setReferenceCounted(true);
			multicastLock.acquire();
			ALog.d(ALog.UDP, "Aquired MulticastLock") ;
		}
	}

	private void releaseMulticastLock() {
		if (multicastLock == null) return;

		multicastLock.release();
		multicastLock = null;
		ALog.d(ALog.UDP, "Released MulticastLock") ;
	}
}
