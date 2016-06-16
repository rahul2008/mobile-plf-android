
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cl.di.regsample.app;

import android.app.Application;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.PILConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.SigninProviders;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
		RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");
		RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
		initRegistration();

		//	RegistrationHelper.getInstance().initializeUserRegistration(getApplicationContext(), Locale.getDefault());
		//	Tagging.init(Locale.getDefault(), getApplicationContext());
	}

	private void initRegistration() {
		//Configure JanRain

	/*	RegistrationClientId registrationClientId = new RegistrationClientId();
		registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu");
		RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

		//Configure PIL
		RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setCampaignID("CL20150501_PC_TB_COPPA");
		RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
		RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.EVALUATION);

		//Configure Flow
		RegistrationDynamicConfiguration.getInstance().getFlow().setEmailVerificationRequired(true);
		RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(true);

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
		RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(providers);
		*/


	/*	HSDPInfo hsdpInfo = new HSDPInfo();
		hsdpInfo.setApplicationName("uGrowApp");
		hsdpInfo.setSharedId("f129afcc-55f4-11e5-885d-feff819cdc9f");
		hsdpInfo.setSecreteId("f129b5a8-55f4-11e5-885d-feff819cdc9f");
		hsdpInfo.setBaseURL("http://ugrow-userregistration15.cloud.pcftest.com");



		//Configure HSDP
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.STAGING,hsdpInfo);
*/
		/*HSDPInfo hsdpInfo = new HSDPInfo();

		hsdpInfo.setApplicationName("uGrow");
		hsdpInfo.setSharedId("c62362a0-f02c-11e5-9ce9-5e5517507c66");
		hsdpInfo.setSecreteId("c623685e-f02c-11e5-9ce9-5e5517507c66");
		hsdpInfo.setBaseURL("https://user-registration-assembly-testing.us-east.philips-healthsuite.com");
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);
*/

		HSDPInfo hsdpInfo = new HSDPInfo();
		hsdpInfo.setApplicationName("uGrow");
		hsdpInfo.setSharedId("c62362a0-f02c-11e5-9ce9-5e5517507c66");
		hsdpInfo.setSecreteId("c623685e-f02c-11e5-9ce9-5e5517507c66");
		hsdpInfo.setBaseURL("https://user-registration-assembly-testing.us-east.philips-healthsuite.com");
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);
		String languageCode = Locale.getDefault().getLanguage();
		String countryCode = Locale.getDefault().getCountry();

		PILLocaleManager localeManager = new PILLocaleManager(this);
		localeManager.setInputLocale(languageCode,countryCode);

		RegistrationHelper.getInstance().initializeUserRegistration(this);
		Tagging.init( this, "Philips Registration");

	}







	private void setDynamicConfiguiration(final Configuration developmentEnvironment) {

		final JanRainConfiguration janRainConfiguration = new JanRainConfiguration();
		final RegistrationClientId registrationClientId = new RegistrationClientId();
		registrationClientId.setDevelopmentId("yjcktbvtvp5wtbtanu3398ymhc5t4mdg");
		registrationClientId.setEvaluationId("fc39pbyxwc6255yma54e547yhvzb76vr");
		registrationClientId.setProductionId("tpqhkn3zd4yxqegyyhd7xyysq4gmh6ee");
		registrationClientId.setTestingId("auy6rtk5zbwwxnzsq52vr9p7dgtyzhc5");
		janRainConfiguration.setClientIds(registrationClientId);
		RegistrationDynamicConfiguration.getInstance().setJanRainConfiguration(janRainConfiguration);

		PILConfiguration pilConfiguration = new PILConfiguration();
		pilConfiguration.setMicrositeId("81448");
		pilConfiguration.setRegistrationEnvironment(developmentEnvironment);
		RegistrationDynamicConfiguration.getInstance().setPilConfiguration(pilConfiguration);

		Flow flow = new Flow();
		flow.setEmailVerificationRequired(true);
		flow.setTermsAndConditionsAcceptanceRequired(true);
		RegistrationDynamicConfiguration.getInstance().setFlow(flow);

		SigninProviders signinProviders = new SigninProviders();
		HashMap<String, ArrayList<String>> providers = new HashMap<>();
		ArrayList<String> defaultSignInProviders = new ArrayList<>();
		defaultSignInProviders.add("facebook");
		defaultSignInProviders.add("googleplus");
		providers.put("default", defaultSignInProviders);

		signinProviders.setProviders(providers);
		RegistrationDynamicConfiguration.getInstance().setSignInProviders(signinProviders);


		//For HSDP

		HSDPInfo hsdpInfo = new HSDPInfo();
		hsdpInfo.setApplicationName("uGrow");
		hsdpInfo.setSharedId("******");
		hsdpInfo.setSecreteId("*****");
		hsdpInfo.setBaseURL("*******");
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.DEVELOPMENT,hsdpInfo);




	}



}




