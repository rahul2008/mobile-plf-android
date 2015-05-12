package com.philips.cl.di.dicomm.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public interface DICommApplianceListener {
 
	public void onPortUpdate(DICommAppliance appliance, DICommPort<?> port);
 
	public void onPortError(DICommAppliance appliance, DICommPort<?> port, Error error);

}
