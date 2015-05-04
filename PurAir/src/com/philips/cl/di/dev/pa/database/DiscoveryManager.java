package com.philips.cl.di.dev.pa.database;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public class DiscoveryManager<T extends DICommAppliance> {
	
	public List<T> getAllAppliances() {
		return new ArrayList<T>();
	}

}
