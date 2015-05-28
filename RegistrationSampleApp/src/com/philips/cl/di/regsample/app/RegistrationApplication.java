
package com.philips.cl.di.regsample.app;

import java.util.Locale;

import android.app.Application;
import android.content.Intent;

import com.philips.cl.di.reg.listener.UserRegistrationListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;

public class RegistrationApplication extends Application implements UserRegistrationListener{
	
	private RegistrationHelper registrationHelper;

	@Override
	public void onCreate() {
		super.onCreate();

		registrationHelper = RegistrationHelper.getInstance();
		registrationHelper.intializeRegistrationSettings(Janrain.INITIALIZE, this,
		        Locale.getDefault());
		
		registrationHelper.registerUserRegistrationListener(this);

	}

	@Override
	public void onUserRegistrationComplete() {
		
		Intent intent = new Intent(this, RegistrationSampleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		registrationHelper.unRegisterUserRegistrationListener();
	}

}
