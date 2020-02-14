
package com.philips.cdp.registration.sample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.multidex.MultiDex;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.uappframework.UappInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class URDemoApplication extends Application {


    private static final String CHINA_CODE = "CN";
    private static final String DEFAULT = "default";
    private static final String URL_ENCODING = "UTF-8";
    private static URDemoApplication mRegistrationSampleApplication = null;

    private AppInfraInterface mAppInfraInterface;

    /**
     * @return instance of this class
     */
    public synchronized static URDemoApplication getInstance() {
        return mRegistrationSampleApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRegistrationSampleApplication = this;
        mAppInfraInterface = new AppInfra.Builder().build(this);
        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {
            String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
            if (restoredHSDPText != null && restoredHSDPText.equalsIgnoreCase(restoredText)) {
                initHSDP(RegUtility.getConfiguration(restoredText));
            }else{
                clearHSDP();
            }
            initRegistration(RegUtility.getConfiguration(restoredText));
        } else {
            initHSDP(Configuration.STAGING);
            initRegistration(Configuration.STAGING);
        }

        boolean abc = RegistrationConfiguration.getInstance().isHsdpFlow();
        RLog.d("hsdp","hsdp"+abc);
    }

    private void clearHSDP() {

        AppConfigurationInterface anInterface = mAppInfraInterface.getConfigInterface();
        final AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        anInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                UR, null, configError);

        anInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                UR, null, configError);


        anInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                UR, null, configError);



        anInterface.setPropertyForKey(HSDP_CONFIGURATION_BASE_URL,
                UR, null, configError);

    }

    public void initRegistration(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }
        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();

        initAppIdentity(configuration);


        UappInterface standardRegistrationInterface = new URDemouAppInterface();
        standardRegistrationInterface.init(new URDemouAppDependencies(mAppInfraInterface), new URDemouAppSettings(this));


        RLog.enableLogging();



        //  mAppInfraInterface.getServiceDiscovery().setHomeCountry("ES");
    }

    public void initHSDP(Configuration configuration) {
        if(mAppInfraInterface == null){
            mAppInfraInterface = new AppInfra.Builder().build(this);
        }
        final AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();


        SharedPreferences.Editor editor = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
        switch (configuration) {
            case EVALUATION:
            {
                AppConfigurationInterface anInterface = mAppInfraInterface.getConfigInterface();

                Map<String, String> hsdpAppNames = new HashMap<>();
                hsdpAppNames.put(CHINA_CODE, "Sonicare");
                hsdpAppNames.put(DEFAULT, "uGrow");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR, hsdpAppNames, configError);

                Map<String, String> hsdpSecrets = new HashMap<>();
                hsdpSecrets.put(CHINA_CODE, "981b4f75-9da5-4939-96e5-3e4e18dd6cb6");
                hsdpSecrets.put(DEFAULT, "EB7D2C2358E4772070334CD868AA6A802164875D6BEE858D13226234350B156AC8C4917885B5552106DC7F9583CA52CB662110516F8AB02215D51778DE1EF1F3");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                        UR, hsdpSecrets, configError);

                Map<String, String> hsdpSharedIds = new HashMap<>();
                hsdpSharedIds.put(CHINA_CODE, "758f5467-bb78-45d2-a58a-557c963c30c1");
                hsdpSharedIds.put(DEFAULT, "e95f5e71-c3c0-4b52-8b12-ec297d8ae960");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                        UR, hsdpSharedIds, configError);

                Map<String, String> hsdpBaseUrls = new HashMap<>();
                try {

                    hsdpBaseUrls.put(CHINA_CODE, URLEncoder.encode("https://user-registration-assembly-staging.cn1.philips-healthsuite.com.cn", URL_ENCODING));
                    hsdpBaseUrls.put(DEFAULT, URLEncoder.encode("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com", URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_BASE_URL,
                        UR, hsdpBaseUrls, configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
            }
            break;
            case DEVELOPMENT: {
                AppConfigurationInterface anInterface = mAppInfraInterface.getConfigInterface();

                Map<String, String> hsdpAppNames = new HashMap<>();
                hsdpAppNames.put(CHINA_CODE, "CDP");
                hsdpAppNames.put(DEFAULT, "uGrow");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR, hsdpAppNames, configError);

                Map<String, String> hsdpSecrets = new HashMap<>();
                hsdpSecrets.put(CHINA_CODE, "057b97e0-f9b1-11e6-bc64-92361f002671");
                hsdpSecrets.put(DEFAULT, "c623685e-f02c-11e5-9ce9-5e5517507c66");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                        UR, hsdpSecrets, configError);

                Map<String, String> hsdpSharedIds = new HashMap<>();
                hsdpSharedIds.put(CHINA_CODE, "fe53a854-f9b0-11e6-bc64-92361f002671");
                hsdpSharedIds.put(DEFAULT, "c62362a0-f02c-11e5-9ce9-5e5517507c66");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                        UR, hsdpSharedIds, configError);

                Map<String, String> hsdpBaseUrls = new HashMap<>();
                try {
                    hsdpBaseUrls.put(CHINA_CODE, URLEncoder.encode("https://user-registration-assembly-hsdpchinadev.cn1.philips-healthsuite.com.cn", URL_ENCODING));
                    hsdpBaseUrls.put(DEFAULT, URLEncoder.encode("https://ugrow-ds-development.cloud.pcftest.com", URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_BASE_URL,
                        UR, hsdpBaseUrls, configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
            }
            break;
            case PRODUCTION:
                SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;


            case STAGING:
            {
                AppConfigurationInterface anInterface = mAppInfraInterface.getConfigInterface();

                Map<String, String> hsdpAppNames = new HashMap<>();
                hsdpAppNames.put(CHINA_CODE, "Sonicare");
                hsdpAppNames.put(DEFAULT, "uGrow");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                        UR, hsdpAppNames, configError);

                Map<String, String> hsdpSecrets = new HashMap<>();
                hsdpSecrets.put(CHINA_CODE, "981b4f75-9da5-4939-96e5-3e4e18dd6cb6");
                hsdpSecrets.put(DEFAULT, "EB7D2C2358E4772070334CD868AA6A802164875D6BEE858D13226234350B156AC8C4917885B5552106DC7F9583CA52CB662110516F8AB02215D51778DE1EF1F3");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                        UR, hsdpSecrets, configError);



                Map<String, String> hsdpSecrets1 =(Map<String, String>) anInterface.getPropertyForKey(HSDP_CONFIGURATION_SECRET,
                        UR,  configError);

                Map<String, String> hsdpSharedIds = new HashMap<>();
                hsdpSharedIds.put(CHINA_CODE, "758f5467-bb78-45d2-a58a-557c963c30c1");
                hsdpSharedIds.put(DEFAULT, "e95f5e71-c3c0-4b52-8b12-ec297d8ae960");

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                        UR, hsdpSharedIds, configError);

                Map<String, String> hsdpBaseUrls = new HashMap<>();
                try {

                    hsdpBaseUrls.put(CHINA_CODE, URLEncoder.encode("https://user-registration-assembly-staging.cn1.philips-healthsuite.com.cn", URL_ENCODING));
                    hsdpBaseUrls.put(DEFAULT, URLEncoder.encode("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com", URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                anInterface.setPropertyForKey(HSDP_CONFIGURATION_BASE_URL,
                        UR, hsdpBaseUrls, configError);

                editor.putString("reg_hsdp_environment", configuration.getValue());
                editor.commit();
            }
            break;

            case TESTING:
                prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").commit();
                break;
        }
    }

    final String AI = "appinfra";
    private void initAppIdentity(Configuration configuration) {

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        mAppInfraInterface.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);

        switch (configuration) {
            case EVALUATION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                mAppInfraInterface.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }
    }

    public AppInfraInterface getAppInfra() {
        return mAppInfraInterface;
    }
}

