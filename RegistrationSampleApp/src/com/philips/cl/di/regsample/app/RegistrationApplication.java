
package com.philips.cl.di.regsample.app;

import java.util.Locale;

import android.app.Application;

import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		RegistrationHelper registrationHelper = RegistrationHelper.getInstance();
		registrationHelper.intializeRegistrationSettings(Janrain.INITIALIZE, this, Locale.getDefault());
		
	}

}
