package com.philips.cl.di.dev.pa.database;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public interface ApplianceDatabase<T extends DICommAppliance> {

	void save(T object);
	
	void loadDataForAppliance(T object);
	
}
