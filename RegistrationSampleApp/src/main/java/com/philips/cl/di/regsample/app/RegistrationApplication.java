
package com.philips.cl.di.regsample.app;

import android.app.Application;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE,
		        "RegistrationApplication : Janrain initialization with locale : "
		                + Locale.getDefault());
		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");
		RegistrationHelper.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
		RegistrationHelper.getInstance().intializeRegistrationSettings(this,
				Locale.getDefault());
		Tagging.init(RegistrationHelper.getInstance().getLocale(), this);

	}

}
