package com.philips.cdp.dicommclient.appliance;

public class NullApplianceDatabase<T extends DICommAppliance> implements DICommApplianceDatabase<T> {

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
