package com.philips.cdp.dicommclient.appliance;

public class NullApplianceDatabase implements DICommApplianceDatabase<DICommAppliance> {

	@Override
	public long save(DICommAppliance appliance) {
		// NOP
		return 0;
	}

	@Override
	public void loadDataForAppliance(DICommAppliance appliance) {
		// NOP
	}

	@Override
	public int delete(DICommAppliance appliance) {
		// NOP
		return 0;
	}

}
