/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;

public interface DICommApplianceListener {

	public void onAppliancePortUpdate(DICommAppliance appliance, DICommPort<?> port);

	public void onAppliancePortError(DICommAppliance appliance, DICommPort<?> port, Error error);

}
