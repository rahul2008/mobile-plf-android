package com.philips.cl.di.dicomm.appliance;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.communication.Error;

public interface DICommApplianceListener {
 
	public void onPortUpdate(DICommAppliance appliance, DICommPort<?> port);
 
	public void onPortError(DICommAppliance appliance, DICommPort<?> port, Error error);
	
	public void onApplianceChanged();
}
