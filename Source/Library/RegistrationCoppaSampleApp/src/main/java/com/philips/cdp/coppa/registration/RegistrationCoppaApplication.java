
package com.philips.cdp.coppa.registration;

import android.app.Application;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

public class RegistrationCoppaApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RLog.d(RLog.APPLICATION, "RegistrationCoppaApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationCoppaApplication : Janrain initialization with locale : " + Locale.getDefault());
		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");
		RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
		RegistrationHelper.getInstance().initializeUserRegistration(this, Locale.getDefault());
		Tagging.init(Locale.getDefault(), this,"Philips Registration");
	}

}

