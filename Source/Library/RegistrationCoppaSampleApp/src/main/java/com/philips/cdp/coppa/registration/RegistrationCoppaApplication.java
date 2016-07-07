
package com.philips.cdp.coppa.registration;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationCoppaApplication extends Application {


    private static volatile RegistrationCoppaApplication mRegistrationHelper = null;


    /**
     * @return instance of this class
     */
    public synchronized static RegistrationCoppaApplication getInstance() {
        return mRegistrationHelper;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationHelper = this;

        AppInfraSingleton.setInstance( new AppInfra.Builder().build(this));
        AIAppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().getAppInfraInstance().getTagging();
        aiAppTaggingInterface.createInstanceForComponent("User Registration", RegistrationHelper.getRegistrationApiVersion());
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.OPTIN);

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        RLog.init(this);
        RLog.d(RLog.APPLICATION, "RegistrationApplication : onCreate");
        RLog.d(RLog.JANRAIN_INITIALIZE, "RegistrationApplication : Janrain initialization with locale : " + Locale.getDefault());


        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            RLog.i("Last known environment", restoredText);
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initRegistration(Configuration.STAGING);
        }


    }


    public void initRegistration(Configuration configuration) {
        //Store current environment

        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        //Configure JanRain
        RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setDevelopmentId("ad7nn99y2mv5berw5jxewzagazafbyhu");
        registrationClientId.setTestingId("xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7");
        registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu");
        registrationClientId.setStagingId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        registrationClientId.setProductionId("mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3");
        RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

        //Configure PIL
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("77000");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(configuration.getValue());


        //Configure Flow
        RegistrationDynamicConfiguration.getInstance().getFlow().setEmailVerificationRequired(true);
        RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(true);
        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        RegistrationDynamicConfiguration.getInstance().getFlow().setMinAgeLimit(ageMap);

        //Configure Signin Providers
        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
        ArrayList<String> values1 = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();
        ArrayList<String> values3 = new ArrayList<String>();
        values1.add("facebook");
        values1.add("googleplus");

        values2.add("facebook");
        values2.add("googleplus");

        values3.add("facebook");
        values3.add("googleplus");

        providers.put("NL", values1);
        providers.put("US", values2);
        providers.put("default", values3);
        RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(providers);

        //Configure HSDP
        //initHSDP(configuration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(this);
        //Tagging.init(this, "Philips Registration");

    }

}

