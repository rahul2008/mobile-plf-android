package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.ALog;

public class UDPSocketManager extends Thread {
	
	private static final int UDP_PORT = 8080 ;
	
	private UDPEventListener udpEventListener ;
	
	private DatagramSocket socket ;
	
	private boolean stop ;
	
	private MulticastLock multicastLock ;
	
	public UDPSocketManager(UDPEventListener udpEventListener) {
		this.udpEventListener = udpEventListener ;
	}
	
	@Override
	public void run() {
		ALog.d(ALog.SUBSCRIPTION, "started udp listener") ;
		try {
			WifiManager wifi = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
			if (wifi != null) {
				multicastLock = wifi.createMulticastLock(getName());
				multicastLock.setReferenceCounted(true);
				multicastLock.acquire();
			}
			
			socket = new DatagramSocket(UDP_PORT) ;
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (!stop ) {
			byte [] buf = new byte[1024] ;
			DatagramPacket packet = new DatagramPacket(buf, buf.length) ;
			try {
				socket.receive(packet) ;
				
				String packetReceived = new String(packet.getData(), Charset.defaultCharset()).trim();
				if( packetReceived != null &&  packetReceived.length() > 0 && udpEventListener != null) {
					String [] packetsReceived = packetReceived.split("\n") ;
					if(packetsReceived != null && packetsReceived.length > 0 ) {
						ALog.d(ALog.SUBSCRIPTION, "UDP Data Received") ;
						String lastLine = packetsReceived[packetsReceived.length-1];
						udpEventListener.onUDPEventReceived(lastLine) ;
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e2) {
				// NOP -  Received after attempt to close socket.
			}
		}
	}
	
	public void stopUDPListener() {
		ALog.d(ALog.SUBSCRIPTION, "stop UDP") ;
		stop = true ;
		if( socket != null && !socket.isClosed()) {
			socket.close() ;
			socket = null ;
		}
		if (multicastLock != null) {
			multicastLock.release();
			multicastLock = null;
		}
		
	}
}
