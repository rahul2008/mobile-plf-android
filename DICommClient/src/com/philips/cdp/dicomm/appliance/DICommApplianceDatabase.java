package com.philips.cdp.dicomm.appliance;

public interface DICommApplianceDatabase<T extends DICommAppliance> {

	long save(T appliance);

	void loadDataForAppliance(T appliance);

	int delete(T appliance);

}
