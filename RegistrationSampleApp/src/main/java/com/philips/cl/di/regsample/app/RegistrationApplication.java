
package com.philips.cl.di.regsample.app;

import android.app.Application;


import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.configuration.HSDPClientInfo;
import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.PILConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.SigninProviders;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
		RegistrationHelper.getInstance().setCoppaFlow(false);
		RegistrationHelper.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
			//Configure JanRain
		RegistrationClientId clientId = new RegistrationClientId("4r36zdbeycca933nufcknn2hnpsz6gxu");
		JanRainConfiguration janRainConfiguration = new JanRainConfiguration(clientId);
		RegistrationConfiguration.getInstance().setJanRainConfiguration(janRainConfiguration);
		//Configure PIL
		PILConfiguration pilConfiguration = new PILConfiguration();
		pilConfiguration.setCampaignID("CL20150501_PC_TB_COPPA");
		pilConfiguration.setMicrositeId("77000");
		pilConfiguration.setRegistrationEnvironment("Evaluation");
		RegistrationConfiguration.getInstance().setPilConfiguration(pilConfiguration);

		//Configure Flow
		Flow flow = new Flow();
		flow.setTermsAndConditionsAcceptanceRequired(true);
		flow.setEmailVerificationRequired(true);
		RegistrationConfiguration.getInstance().setFlow(flow);
		//Configure Signin Providers
		SigninProviders signinProviders = new SigninProviders();
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
		signinProviders.setProviders(providers);
		RegistrationConfiguration.getInstance().setSocialProviders(signinProviders);

		//Configure HSDP

		HSDPClientInfo hsdpClientInfo = new HSDPClientInfo("f129afcc-55f4-11e5-885d-feff819cdc9f", "f129b5a8-55f4-11e5-885d-feff819cdc9f");
		hsdpClientInfo.setApplicationName("uGrowApp");
		hsdpClientInfo.setBaseURL("http://ugrow-userregistration15.cloud.pcftest.com");
		HashMap<String, HSDPClientInfo> hsdpClientInfos = new HashMap<>();
		hsdpClientInfos.put("Evaluation", hsdpClientInfo);
		HSDPConfiguration hsdpConfiguration = new HSDPConfiguration(hsdpClientInfos);
		RegistrationConfiguration.getInstance().setHsdpConfiguration(hsdpConfiguration);

		RegistrationHelper.getInstance().intializeRegistrationSettings(this,
				Locale.getDefault());
		Tagging.init(Locale.getDefault(), this);

	}



}
