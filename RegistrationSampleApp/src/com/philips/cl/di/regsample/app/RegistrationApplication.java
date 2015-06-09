
package com.philips.cl.di.regsample.app;

import java.util.Locale;

import android.app.Application;

import com.adobe.mobile.Config;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;
import com.philips.cl.di.reg.ui.utils.RLog;

public class RegistrationApplication extends Application{
	
	private RegistrationHelper registrationHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		Config.setContext(getApplicationContext());
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Application INIT,LOCALE : "+Locale.getDefault());
		
		registrationHelper = RegistrationHelper.getInstance();
		registrationHelper.intializeRegistrationSettings(Janrain.INITIALIZE, this,
		        Locale.getDefault());
	}
}
