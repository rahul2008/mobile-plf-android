
package com.philips.cl.di.regsample.app;

import android.app.Application;

import com.adobe.mobile.Config;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

import java.util.Locale;

public class RegistrationApplication extends Application {

	private RegistrationHelper mRegistrationHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		Config.setContext(getApplicationContext());
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE,
		        "RegistrationApplication : Janrain initialization with locale : "
		                + Locale.getDefault());

		mRegistrationHelper = RegistrationHelper.getInstance();
		mRegistrationHelper.setCoppaFlow(false);
		mRegistrationHelper.setAnalyticsAppId("IntegratingApplicationAppId");
		mRegistrationHelper.setPreviousPage("RegistrationLaunchingPageName");
		mRegistrationHelper.setIsTagginEnabled(true);
		mRegistrationHelper.intializeRegistrationSettings(this,
				Locale.getDefault());
	}
}
