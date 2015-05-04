package com.philips.cl.di.dev.pa.database;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dicomm.port.DevicePort;
import com.philips.cl.di.dicomm.port.FirmwarePort;
import com.philips.cl.di.dicomm.port.PairingPort;
import com.philips.cl.di.dicomm.port.WifiPort;
import com.philips.cl.di.dicomm.port.WifiUIPort;

public interface DICommApplianceInterface {
	
	void subscribe();
	
	void unsubscribe();
	
	public void stopResubscribe();

    public DevicePort getDevicePort();

    public FirmwarePort getFirmwarePort();
    
    public PairingPort getPairingPort();

    public WifiPort getWifiPort();

    public WifiUIPort getWifiUIPort();
    
    public String getName();

    public void setConnectionState(ConnectionState connectionState);

}
