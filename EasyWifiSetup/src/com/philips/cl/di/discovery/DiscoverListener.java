package com.philips.cl.di.discovery;

public interface DiscoverListener {

	void ResolvedDeviceIp(String uri);

	String getServiceType();

}
