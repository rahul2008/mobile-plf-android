package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.LocalStrategy;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;

public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();		
		DICommClientWrapper.initializeDICommLibrary(this,applianceFactory, null, null);		
	}
	
	private DICommApplianceFactory<GenericAppliance> applianceFactory = new DICommApplianceFactory<GenericAppliance>() {

		@Override
		public boolean canCreateApplianceForNode(NetworkNode networkNode) {
			return true;
		}

		@Override
		public GenericAppliance createApplianceForNode(
				NetworkNode networkNode) {
			return new GenericAppliance(networkNode, new LocalStrategy(new DISecurity()));
		}
	};
}
