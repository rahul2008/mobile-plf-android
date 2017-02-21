/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;

class NullApplianceDatabase<T extends Appliance> implements DICommApplianceDatabase<T> {

	@Override
	public long save(T appliance) {
		// NOP
		return 0;
	}

	@Override
	public void loadDataForAppliance(T appliance) {
		// NOP
	}

	@Override
	public int delete(T appliance) {
		// NOP
		return 0;
	}

}
