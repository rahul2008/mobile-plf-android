
package com.philips.cl.di.regsample.app;

import android.app.Application;


import com.philips.cdp.tagging.Tagging;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

import java.util.Locale;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//Config.setContext(getApplicationContext());
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE,
		        "RegistrationApplication : Janrain initialization with locale : "
		                + Locale.getDefault());


		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");

		Locale mlocale = Locale.getDefault();
		if (RegistrationHelper.getInstance().isCoppaFlow()) {
			mlocale = new Locale("en_US");
		}
		Tagging.init(mlocale, this);
		RegistrationHelper.getInstance().intializeRegistrationSettings(this,
				Locale.getDefault());

	}
}
