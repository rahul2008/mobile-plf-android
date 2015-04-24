
package com.philips.cl.di.regsample.app;

import android.app.Application;

import com.philips.cl.di.reg.settings.RegistrationSettings;
import com.philips.cl.di.reg.settings.RegistrationSettings.Janrain;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RegistrationSettings registrationSettings = new RegistrationSettings(this);
		registrationSettings.intializeJanrain(Janrain.INITIALIZE);
		
/*		JanrainSettings janrainSettings = JanrainSettings.getInstance();
		registrationSettings.intializeJanrain(Janrain.INITIALIZE, this);*/
		
		
	}

}
