package com.philips.platform.urdemo;

import android.content.Context;
import android.content.SharedPreferences;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

class InitHsdp{
    private static final String CHINA_CODE = "CN";
    private static final String DEFAULT = "default";
    private static final String URL_ENCODING = "UTF-8";
    private static final String HSDP_CONFIGURATION_APPLICATION_NAME = "HSDPConfiguration.ApplicationName";
    private static final String HSDP_CONFIGURATION_SECRET = "HSDPConfiguration.Secret";
    private static final String HSDP_CONFIGURATION_SHARED = "HSDPConfiguration.Shared";

    private  AppInfraInterface mAppInfraInterface;

    public  void initHSDP(Configuration configuration, Context context, AppInfraInterface appInfra) {
        if(mAppInfraInterface == null){
            mAppInfraInterface = appInfra;
        }
        final AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        SharedPreferences.Editor editor = context.getSharedPreferences("reg_dynamic_config", MODE_PRIVATE).edit();
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
                editor.apply();
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
                SharedPreferences prefs = context.getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").apply();
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
                prefs = context.getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                prefs.edit().remove("reg_hsdp_environment").apply();
                break;
        }
    }
}