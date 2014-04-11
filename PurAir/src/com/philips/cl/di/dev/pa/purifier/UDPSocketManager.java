package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.philips.cl.di.dev.pa.util.ALog;

public class UDPSocketManager extends Thread {
	
	private static final int UDP_PORT = 8080 ;
	
	private UDPEventListener udpEventListener ;
	
	private DatagramSocket socket ;
	
	private boolean stop ;
	
	@Override
	public void run() {
		ALog.i(ALog.SUBSCRIPTION, "started udp listener") ;
		try {
			socket = new DatagramSocket(UDP_PORT) ;
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (!stop ) {
			byte [] buf = new byte[1024] ;
			DatagramPacket packet = new DatagramPacket(buf, buf.length) ;
			try {
				socket.receive(packet) ;
				
				String packetReceived = new String(packet.getData()).trim();
				if( packetReceived != null &&  packetReceived.length() > 0 && udpEventListener != null) {
					ALog.i(ALog.SUBSCRIPTION, "UDP Data Received") ;
					String [] packetsReceived = packetReceived.split("\n") ;
					
					udpEventListener.onUDPEventReceived(packetsReceived[packetsReceived.length-1]) ;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setUDPEventListener(UDPEventListener udpEventListener ) {
		this.udpEventListener = udpEventListener ;
	}
	
	public void stopUDPListener() {
		ALog.i(ALog.SUBSCRIPTION, "stop UDP") ;
		stop = true ;
		socket.close() ;
		socket = null ;
	}
}
