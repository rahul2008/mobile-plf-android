package com.philips.cl.di.dev.pa.database;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public interface ApplianceDatabase<T extends DICommAppliance> {

	long save(T appliance);
	
	void loadDataForAppliance(T appliance);
	
	int delete(T appliance);
	
}
