/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp2.commlib.core.appliance.Appliance;

public interface DICommApplianceDatabase<T extends Appliance> {

	long save(T appliance);

	void loadDataForAppliance(T appliance);

	int delete(T appliance);

}
