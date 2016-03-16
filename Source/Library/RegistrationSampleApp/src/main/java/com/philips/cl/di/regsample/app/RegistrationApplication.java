
package com.philips.cl.di.regsample.app;

import android.app.Application;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
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
		RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());
		Tagging.enableAppTagging(true);
		Tagging.setTrackingIdentifier("integratingApplicationAppsId");
		Tagging.setLaunchingPageName("demoapp:home");
		RegistrationConfiguration.getInstance().setCoppaFlow(false);
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
		hsdpInfo.setSecretId("f129b5a8-55f4-11e5-885d-feff819cdc9f");
		hsdpInfo.setBaseURL("http://ugrow-userregistration15.cloud.pcftest.com");



		//Configure HSDP
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.STAGING,hsdpInfo);
*/

		/*HSDPInfo hsdpInfo = new HSDPInfo();
		hsdpInfo.setApplicationName("uGrowApp");
		hsdpInfo.setSharedId("f129afcc-55f4-11e5-885d-feff819cdc9f");
		hsdpInfo.setSecretId("f129b5a8-55f4-11e5-885d-feff819cdc9f");
		hsdpInfo.setBaseURL("https://newuser-registration-assembly15.cloud.pcftest.com");
		RegistrationDynamicConfiguration.getInstance().getHsdpConfiguration().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);
*/
		RegistrationHelper.getInstance().initializeUserRegistration(this, Locale.getDefault());
		Tagging.init(Locale.getDefault(), this,"Philips Registration");
	}
}

