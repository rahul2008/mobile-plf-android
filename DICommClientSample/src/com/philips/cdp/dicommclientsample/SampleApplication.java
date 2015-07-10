package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.LocalStrategy;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.KpsConfigurationInfo;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;

import android.app.Application;

public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		CppController.createSharedInstance(getApplicationContext(), kpsConfiguration);
		DiscoveryManager.createSharedInstance(this, CppController.getInstance(), applianceFactory);
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

	private KpsConfigurationInfo kpsConfiguration = new KpsConfigurationInfo() {
		
		@Override
		public int getProductVersion() {
			return 0;
		}
		
		@Override
		public String getProductId() {
			return null;
		}
		
		@Override
		public String getLanguageCode() {
			return null;
		}
		
		@Override
		public String getDevicePortUrl() {
			return null;
		}
		
		@Override
		public String getCountryCode() {
			return null;
		}
		
		@Override
		public String getComponentId() {
			return null;
		}
		
		@Override
		public int getComponentCount() {
			return 0;
		}
		
		@Override
		public String getBootStrapKey() {
			return null;
		}
		
		@Override
		public String getBootStrapId() {
			return null;
		}
		
		@Override
		public int getAppVersion() {
			return 0;
		}
		
		@Override
		public String getAppType() {
			return null;
		}
		
		@Override
		public String getAppId() {
			return null;
		}
	};
}
