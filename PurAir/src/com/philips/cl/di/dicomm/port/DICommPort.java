package com.philips.cl.di.dicomm.port;

public abstract class DICommPort {
	
	public abstract boolean isResponseForThisPort(String response);
	
	public abstract void processResponse(String response);

}
