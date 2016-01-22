
package com.philips.cl.di.regsample.app;

import android.app.Application;


import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.RegistrationConfigurationListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;
import com.philips.dhpclient.util.ServerTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");
		RegistrationHelper.getInstance().setCoppaFlow(true);
		RegistrationHelper.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

		RegistrationConfiguration.getInstance().initialize(getApplicationContext(),new RegistrationConfigurationListener() {
			@Override
			public void onSuccess() {
				initRegistration();
			//	RegistrationHelper.getInstance().intializeRegistrationSettings(getApplicationContext(), Locale.getDefault());
			//	Tagging.init(Locale.getDefault(), getApplicationContext());

			}

			@Override
			public void onFailuer() {

			}
		});


	}

	private void initRegistration() {
		//Configure JanRain
		RegistrationConfiguration.getInstance().getJanRainConfiguration().setClientId("4r36zdbeycca933nufcknn2hnpsz6gxu");

		//Configure PIL
		RegistrationConfiguration.getInstance().getPilConfiguration().setCampaignID("CL20150501_PC_TB_COPPA");
		RegistrationConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
		RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.EVALUATION);

		//Configure Flow
		RegistrationConfiguration.getInstance().getFlow().setEmailVerificationRequired(true);
		RegistrationConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(true);

		//Configure Signin Providers
		HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
		ArrayList<String> values1 = new ArrayList<String>();
		ArrayList<String> values2 = new ArrayList<String>();
		ArrayList<String> values3 = new ArrayList<String>();
		values1.add("googleplus");
		values1.add("facebook");
		values1.add("twitter");
		values2.add("googleplus");
		values2.add("facebook");
		values2.add("twitter");
		values3.add("googleplus");
		values3.add("facebook");
		values3.add("twitter");

		providers.put("NL", values1);
		providers.put("US", values2);
		providers.put("DEFAULT", values3);
		RegistrationConfiguration.getInstance().getSocialProviders().setProviders(providers);

		//Configure HSDP
		RegistrationConfiguration.getInstance().getHsdpConfiguration().setApplicationName("uGrowApp");
		RegistrationConfiguration.getInstance().getHsdpConfiguration().setSharedId("f129afcc-55f4-11e5-885d-feff819cdc9f");
		RegistrationConfiguration.getInstance().getHsdpConfiguration().setSecret("f129b5a8-55f4-11e5-885d-feff819cdc9f");
		RegistrationConfiguration.getInstance().getHsdpConfiguration().setBaseURL("http://ugrow-userregistration15.cloud.pcftest.com");
		RegistrationConfiguration.getInstance().getHsdpConfiguration().setEnvironment(Configuration.EVALUATION);


		RegistrationHelper.getInstance().intializeRegistrationSettings(this, Locale.getDefault());
		Tagging.init(Locale.getDefault(), this);
	}

}
