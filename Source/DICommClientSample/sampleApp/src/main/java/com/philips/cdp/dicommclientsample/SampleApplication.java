package com.philips.cdp.dicommclientsample;

import android.app.Application;
import android.util.Log;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;

public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		CppController cppController = CppController.createSharedInstance(this, new SampleKpsConfigurationInfo());

		// TODO better to create separate methods than to pass null?
		DICommApplianceFactory<AirPurifier> applianceFactory = new AirPurifierFactory();
		DICommClientWrapper.initializeDICommLibrary(this,applianceFactory, null, cppController);
		Log.i("SampleApplication", "DICommClientWrapper.getDICommClientLibVersion() = " + DICommClientWrapper.getDICommClientLibVersion());
	}
}
