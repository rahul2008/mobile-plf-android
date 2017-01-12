package com.philips.cdp.registration.settings;

import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegistrationSettingsURL extends RegistrationSettings {

    private static final String DEV_CAPTURE_DOMAIN = "https://philips.dev.janraincapture.com";

    private static final String TEST_CAPTURE_DOMAIN = "https://philips-test.dev.janraincapture.com";

    private static final String EVAL_CAPTURE_DOMAIN = "https://philips.eval.janraincapture.com";

    private static final String PROD_CAPTURE_DOMAIN = "https://philips.janraincapture.com";

    private static final String DEV_CAPTURE_DOMAIN_CHINA = "https://philips-cn-dev.capture.cn.janrain.com";

    private static final String DEV_CAPTURE_DOMAIN_CHINA_EU = "https://philips-china-eu.eu-dev.janraincapture.com";

    private static final String TEST_CAPTURE_DOMAIN_CHINA = "https://philips-cn-test.capture.cn.janrain.com";

    private static final String TEST_CAPTURE_DOMAIN_CHINA_EU = "https://philips-china-test.eu-dev.janraincapture.com";

    private static final String EVAL_CAPTURE_DOMAIN_CHINA = "https://philips-cn-staging.capture.cn.janrain.com";

    private static final String PROD_CAPTURE_DOMAIN_CHINA = "https://philips-cn.capture.cn.janrain.com";

    private static String EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String EVAL_PRX_RESEND_CONSENT_URL = "https://acc.usa.philips.com/prx/registration/resendConsentMail";

    private static String DEV_PRX_RESEND_CONSENT_URL = "https://dev.philips.com/prx/registration/resendConsentMail";

    private static String DEV_EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String DEV_EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String PROD_PRX_RESEND_CONSENT_URL = "https://www.usa.philips.com/prx/registration/resendConsentMail";

    private static String PROD_PRODUCT_REGISTER_URL = "https://www.philips.co.uk/prx/registration/";

    private static String PROD_PRODUCT_REGISTER_LIST_URL = "https://www.philips.co.uk/prx/registration.registeredProducts/";

    private static String TEST_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String TEST_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String TEST_PRX_RESEND_CONSENT_URL = "https://tst.usa.philips.com/prx/registration/resendConsentMail";

    private String STAGE_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private String STAGE_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private String STAGE_PRX_RESEND_CONSENT_URL = "https://acc.usa.philips.com/prx/registration/resendConsentMail";

    private String EVAL_CAPTURE_FLOW_VERSION = "HEAD";

    private JumpConfig jumpConfig;

    private String langCode;

    private String countryCode;

    private static boolean isChinaFlow;

    /**
     * {@code initialiseConfigParameters} method builds configuration for information in {@code EvalRegistrationSettings}
     *
     * @param locale used to add the requested locale to the configuration
     */
    @Override
    public void initialiseConfigParameters(final String locale) {

        String LOG_TAG = "RegistrationAPI";
        Log.i(LOG_TAG, "initialiseCofig, locale = " + locale);

        jumpConfig = new JumpConfig();
        jumpConfig.captureClientId = mCaptureClientId;
        jumpConfig.captureFlowName = "standard";
        jumpConfig.captureTraditionalRegistrationFormName = "registrationForm";
        jumpConfig.captureEnableThinRegistration = false;
        jumpConfig.captureSocialRegistrationFormName = "socialRegistrationForm";
        jumpConfig.captureForgotPasswordFormName = "forgotPasswordForm";
        jumpConfig.captureEditUserProfileFormName = "editProfileForm";
        jumpConfig.captureResendEmailVerificationFormName = "resendVerificationForm";
        jumpConfig.captureTraditionalSignInFormName = "userInformationForm";
        jumpConfig.traditionalSignInType = Jump.TraditionalSignInType.EMAIL;
        jumpConfig.captureFlowVersion = EVAL_CAPTURE_FLOW_VERSION;


        initializePRXLinks(RegistrationConfiguration.getInstance().getRegistrationEnvironment());


        String localeArr[] = locale.split("-");

        if (localeArr != null && localeArr.length > 1) {
            langCode = localeArr[0];
            countryCode = localeArr[1];
        } else {
            langCode = "en";
            countryCode = "US";
        }
        initServiceDiscovery(locale);
    }

    private String getCaptureId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, "eupac7ugz25x8dwahvrbpmndf8");
        map.put(TEST_CAPTURE_DOMAIN, "x7nftvwfz8e8vcutz49p8eknqp");
        map.put(EVAL_CAPTURE_DOMAIN, "nt5dqhp6uck5mcu57snuy8uk6c");
        map.put(PROD_CAPTURE_DOMAIN, "hffxcm638rna8wrxxggx2gykhc");
        map.put(DEV_CAPTURE_DOMAIN_CHINA, "7629q5uxm2jyrbk7ehuwryj7a4");
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, "euwkgsf83m56hqknjxgnranezh");
        map.put(TEST_CAPTURE_DOMAIN_CHINA, "hqmhwxu7jtdcye758vvxux4ryb");
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, "vdgkb3z57jpv93mxub34x73mqu");
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, "czwfzs7xh23ukmpf4fzhnksjmd");
        map.put(PROD_CAPTURE_DOMAIN_CHINA, "zkr6yg4mdsnt7f8mvucx7qkja3");
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain Map : " + map.get(domain));
        return map.get(domain);
    }

    private String getEngageId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, "bdbppnbjfcibijknnfkk");
        map.put(TEST_CAPTURE_DOMAIN, "fhbmobeahciagddgfidm");
        map.put(EVAL_CAPTURE_DOMAIN, "jgehpoggnhbagolnihge");
        map.put(PROD_CAPTURE_DOMAIN, "ddjbpmgpeifijdlibdio");
        map.put(DEV_CAPTURE_DOMAIN_CHINA, "ruaheighoryuoxxdwyfs");
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, "bdbppnbjfcibijknnfkk");
        map.put(TEST_CAPTURE_DOMAIN_CHINA, "jndphelwbhuevcmovqtn");
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, "fhbmobeahciagddgfidm");
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, "uyfpympodtnesxejzuic");
        map.put(PROD_CAPTURE_DOMAIN_CHINA, "cfwaqwuwcwzlcozyyjpa");
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain Map :" + map.get(domain));

        return map.get(domain);
    }

    private void initializePRXLinks(String registrationEnv) {
        if (registrationEnv == null) {
            mProductRegisterUrl = EVAL_PRODUCT_REGISTER_URL;
            mProductRegisterListUrl = EVAL_PRODUCT_REGISTER_LIST_URL;
            mResendConsentUrl = EVAL_PRX_RESEND_CONSENT_URL;
            return;
        }
        if (registrationEnv.equalsIgnoreCase(Configuration.DEVELOPMENT.getValue())) {
            mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
            mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;
            mResendConsentUrl = DEV_PRX_RESEND_CONSENT_URL;
            return;
        }
        if (registrationEnv.equalsIgnoreCase(Configuration.PRODUCTION.getValue())) {
            mProductRegisterUrl = PROD_PRODUCT_REGISTER_URL;
            mProductRegisterListUrl = PROD_PRODUCT_REGISTER_LIST_URL;
            mResendConsentUrl = PROD_PRX_RESEND_CONSENT_URL;
            return;
        }
        if (registrationEnv.equalsIgnoreCase(Configuration.STAGING.getValue())) {
            mProductRegisterUrl = STAGE_PRODUCT_REGISTER_URL;
            mProductRegisterListUrl = STAGE_PRODUCT_REGISTER_LIST_URL;
            mResendConsentUrl = STAGE_PRX_RESEND_CONSENT_URL;
            return;
        }
        if (registrationEnv.equalsIgnoreCase(Configuration.TESTING.getValue())) {
            mProductRegisterUrl = TEST_PRODUCT_REGISTER_URL;
            mProductRegisterListUrl = TEST_PRODUCT_REGISTER_LIST_URL;
            mResendConsentUrl = TEST_PRX_RESEND_CONSENT_URL;
        }
    }

    private void initServiceDiscovery(final String locale) {

        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + RegistrationHelper.getInstance().getCountryCode());

        ArrayList<String> var1 = new ArrayList<>();
        var1.add("userreg.janrain.api");
        var1.add("userreg.landing.emailverif");
        var1.add("userreg.landing.resetpass");
        var1.add("userreg.janrain.cdn");
        var1.add("userreg.janrain.engage");
        var1.add("userreg.smssupported");


        serviceDiscoveryInterface.getServicesWithCountryPreference(var1, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> resultMap) {

                ServiceDiscoveyService serviceDiscoveyService = resultMap.get("userreg.janrain.api");
                if (serviceDiscoveyService != null) {
                    String urlLocal = serviceDiscoveyService.getConfigUrls();
                    String janrainURL = urlLocal.substring(8);//Please don't remove this line.\

                    if(janrainURL.equalsIgnoreCase("philips.capture.cn.janrain.com")){
                        jumpConfig.captureDomain = "philips-cn.capture.cn.janrain.com";
                        jumpConfig.engageAppId = getEngageId(PROD_CAPTURE_DOMAIN_CHINA);
                        jumpConfig.captureAppId = getCaptureId(PROD_CAPTURE_DOMAIN_CHINA);
                    }else{
                        jumpConfig.captureDomain = janrainURL;
                        jumpConfig.engageAppId = getEngageId(urlLocal);
                        jumpConfig.captureAppId = getCaptureId(urlLocal);
                    }

                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.janrain.api :" + urlLocal);

                    if (jumpConfig.engageAppId == null || jumpConfig.captureAppId == null)
                        throw new RuntimeException("Captureid or engageid is null");

                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.engageid :" + getEngageId(urlLocal));
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.captureid :" + getCaptureId(urlLocal));

                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.api");
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                    return;
                }

                serviceDiscoveyService = resultMap.get("userreg.landing.emailverif");
                if (serviceDiscoveyService != null) {
                    jumpConfig.captureRedirectUri = serviceDiscoveyService.getConfigUrls()
                            + "?loc=" + langCode + "_" + countryCode;
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.emailverif :"
                            + serviceDiscoveyService.getConfigUrls());
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.emailverif :"
                            + jumpConfig.captureRedirectUri);

                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.emailverif :");
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                    return;
                }

                serviceDiscoveyService = resultMap.get("userreg.landing.resetpass");
                if (serviceDiscoveyService != null) {
                    String modifiedUrl = serviceDiscoveyService.getConfigUrls().
                            replaceAll("c-w", "myphilips");
                    //https://philips-cn.capture.cn.janrain.com/
                    jumpConfig.captureRecoverUri = modifiedUrl + "&loc=" +
                            langCode + "_" + countryCode;
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.resetpass :"
                            + modifiedUrl);
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.resetpass :"
                            + jumpConfig.captureRecoverUri);
                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.resetpass : ");
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                    return;
                }

                serviceDiscoveyService = resultMap.get("userreg.janrain.cdn");
                if (serviceDiscoveyService != null) {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.janrain.cdn :" +
                            serviceDiscoveyService.getConfigUrls());
                    jumpConfig.downloadFlowUrl = serviceDiscoveyService.getConfigUrls();
                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.cdn : ");
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                    return;
                }

                serviceDiscoveyService = resultMap.get("userreg.smssupported");
                if (serviceDiscoveyService != null) {
                    String smsSupport = serviceDiscoveyService.getConfigUrls();
                    setChinaFlow(true);
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.smssupported :" +
                            smsSupport);
                    jumpConfig.captureLocale = locale;
                    //Must for mobile create account
                    jumpConfig.captureTraditionalSignInFormName = "userInformationMobileForm";
                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.smssupported :" +
                            "Service Deiscover inis at non China local");
                    setChinaFlow(false);
                    jumpConfig.captureLocale = locale;
                    mPreferredCountryCode = countryCode;
                    mPreferredLangCode = langCode;
                    initialize();
                    return;
                }

                serviceDiscoveyService = resultMap.get("userreg.janrain.engage");
                if (serviceDiscoveyService != null) {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.janrain.engage :" +
                            serviceDiscoveyService.getConfigUrls());
                    jumpConfig.engageAppUrl = serviceDiscoveyService.getConfigUrls().substring(8);
                    mPreferredCountryCode = countryCode;
                    mPreferredLangCode = langCode;
                    initialize();
                    RLog.d(RLog.SERVICE_DISCOVERY, " ChinaFlow : " + isChinaFlow());
                } else {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.engage : ");
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                }
            }

            private void initialize() {
                try {
                    RLog.d(RLog.SERVICE_DISCOVERY, "jumpConfig : " + jumpConfig);
                    Jump.reinitialize(mContext, jumpConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof RuntimeException) {
                        mContext.deleteFile("jr_capture_flow");
                        Jump.reinitialize(mContext, jumpConfig);
                    }
                }
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : group : ");
                EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
            }
        });


    }

    public boolean isChinaFlow() {
        return isChinaFlow;
    }

    private void setChinaFlow(boolean chinaFlow) {
        isChinaFlow = chinaFlow;
    }
}
