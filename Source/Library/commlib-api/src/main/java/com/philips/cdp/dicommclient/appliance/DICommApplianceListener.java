/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;

public interface DICommApplianceListener {

	public void onAppliancePortUpdate(Appliance appliance, DICommPort<?> port);

	public void onAppliancePortError(Appliance appliance, DICommPort<?> port, Error error);

}
