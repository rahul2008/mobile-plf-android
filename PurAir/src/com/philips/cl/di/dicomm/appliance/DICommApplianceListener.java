package com.philips.cl.di.dicomm.appliance;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dicomm.port.DICommPort;

public interface DICommApplianceListener {
 
	public void onPortUpdate(DICommAppliance appliance, DICommPort<?> port);
 
	public void onPortError(DICommAppliance appliance, DICommPort<?> port, Error error);

}
