package com.philips.cl.di.dev.pa.database;

import java.util.List;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public class VerticalApp {
	
	private DiscoveryManager<DICommAppliance> discoveryManager;
	
	
	public VerticalApp() {
		List<DICommAppliance> list = discoveryManager.getAllAppliances();
	}

}
