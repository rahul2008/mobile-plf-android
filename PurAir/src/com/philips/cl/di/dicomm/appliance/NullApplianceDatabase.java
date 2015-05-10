package com.philips.cl.di.dicomm.appliance;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

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
