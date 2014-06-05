package com.philips.cl.di.dev.pa.util.networkutils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.philips.cl.di.dev.pa.util.ALog;

public class VerifyInternetConnectionThread extends Thread {
	
	private InternetConnectionlistener listener;
	private boolean internet;
	
	public VerifyInternetConnectionThread(InternetConnectionlistener listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		try {
			// www.example.com, 93.184.216.119
	        SocketAddress addr = new InetSocketAddress("www.example.com", 80); 
	        Socket socket = new Socket();
	        socket.connect(addr, 5000);
	        //If network isn't conecctet then throw a IOException else socket is connected successfully
	        
	        socket.close();
	        internet = true;
	    } catch (UnknownHostException e) {
	    	internet = false;
	        e.printStackTrace();
	    } catch (IOException e) {
	    	internet = false;
	        e.printStackTrace();
	    } finally {
	    	ALog.i(ALog.USER_REGISTRATION, "Ping socket: " + internet);
	    	if (listener != null) {
	    		listener.updateInternetState(internet);
	    	}
	    }
	}
}
