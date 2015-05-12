package com.philips.cdp.dicomm.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;

public interface DICommApplianceListener {

	public void onPortUpdate(DICommAppliance appliance, DICommPort<?> port);

	public void onPortError(DICommAppliance appliance, DICommPort<?> port, Error error);

}
