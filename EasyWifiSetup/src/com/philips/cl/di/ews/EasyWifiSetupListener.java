package com.philips.cl.di.ews;

public interface EasyWifiSetupListener {
	public void connectedToDeviceAp(String res);
	public void foundDeviceAp(String res);
	public void sentSsidToDevice(String res);
	public void connectedToHomeNetwork(String url_base);
}
