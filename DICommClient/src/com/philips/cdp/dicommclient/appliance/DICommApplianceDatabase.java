/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

public interface DICommApplianceDatabase<T extends DICommAppliance> {

	long save(T appliance);

	void loadDataForAppliance(T appliance);

	int delete(T appliance);

}
