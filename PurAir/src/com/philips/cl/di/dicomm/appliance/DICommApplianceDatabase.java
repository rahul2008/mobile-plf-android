package com.philips.cl.di.dicomm.appliance;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public interface DICommApplianceDatabase<T extends DICommAppliance> {

	long save(T appliance);
	
	void loadDataForAppliance(T appliance);
	
	int delete(T appliance);
	
}
