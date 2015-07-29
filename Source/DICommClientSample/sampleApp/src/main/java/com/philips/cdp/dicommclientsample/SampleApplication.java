package com.philips.cdp.dicommclientsample;

import android.app.Application;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;

public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		DICommApplianceFactory<GenericAppliance> applianceFactory = new GenericApplianceFactory();
		CppController cppController = CppController.createSharedInstance(this, new SampleKpsConfigurationInfo());

		// TOOD better to create separate methods than to pass null?
		DICommClientWrapper.initializeDICommLibrary(this,applianceFactory, null, cppController);
	}
}
