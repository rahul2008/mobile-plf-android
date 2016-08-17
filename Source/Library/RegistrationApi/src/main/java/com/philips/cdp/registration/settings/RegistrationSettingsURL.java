package com.philips.cdp.registration.settings;

import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by 310190722 on 8/16/2016.
 */
public class RegistrationSettingsURL extends RegistrationSettings {

    private String LOG_TAG = "RegistrationAPI";

    private static String EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String EVAL_PRX_RESEND_CONSENT_URL = "https://acc.usa.philips.com/prx/registration/resendConsentMail";

    private String EVAL_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";

    private JumpConfig jumpConfig;

    private String langCode;

    private String countryCode;

    private String DEV_CAPTURE_DOMAIN = "https://philips.dev.janraincapture.com";
    private String TEST_CAPTURE_DOMAIN = "https://philips-test.dev.janraincapture.com";
    private String EVAL_CAPTURE_DOMAIN = "https://philips.eval.janraincapture.com";
    private String PROD_CAPTURE_DOMAIN = "https://philips.janraincapture.com";

    /**
     * {@code initialiseConfigParameters} method builds configuration for information in {@code EvalRegistrationSettings}
     *
     * @param locale used to add the requested locale to the configuration
     */
    @Override
    public void initialiseConfigParameters(String locale) {

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
        mProductRegisterUrl = EVAL_PRODUCT_REGISTER_URL;
        mProductRegisterListUrl = EVAL_PRODUCT_REGISTER_LIST_URL;
        mResendConsentUrl = EVAL_PRX_RESEND_CONSENT_URL;

        String localeArr[] = locale.split("-");

        if (localeArr != null && localeArr.length > 1) {
            langCode = localeArr[0];
            countryCode = localeArr[1];
        } else {
            langCode = "en";
            countryCode = "US";
        }

        AppInfraInterface appInfra = AppInfraSingleton.getInstance();
        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();


        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String error) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.api :" + error);
            }

            @Override
            public void onSuccess(URL url) {
                String janrainURL = url.toString().substring(8);
                jumpConfig.captureDomain = janrainURL.toString();
                HashMap engageAppIdMap = new HashMap<String, HashMap>();
                engageAppIdMap = getEngageAppId();

                HashMap captureAppIdMap = new HashMap<String, HashMap>();
                captureAppIdMap = getCaptureId();
                jumpConfig.engageAppId = (String) engageAppIdMap.get(url.toString());
                jumpConfig.captureAppId = (String) captureAppIdMap.get(url.toString());
                RLog.d("Configuration", " engageAppId :" + jumpConfig.engageAppId);
                RLog.d("Configuration", " captureAppId :" + jumpConfig.captureAppId);
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.janrain.api :" + url);
            }
        });

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.landing.emailverif", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String error) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.emailverif :" + error);
            }

            @Override
            public void onSuccess(URL url) {
                jumpConfig.captureRedirectUri = url.toString() + "?loc=" + langCode + "_" + countryCode;
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.emailverif :" + url.toString());
            }
        });

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.landing.resetpass", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String error) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.resetpass : " + error);
            }

            @Override
            public void onSuccess(URL url) {
                jumpConfig.captureRecoverUri = url.toString() + "&loc=" + langCode + "_" + countryCode;
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.resetpass :" + url.toString());
            }
        });

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.smssupported", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String error) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.smssupported :"+error);
            }

            @Override
            public void onSuccess(URL url) {
                RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.smssupported :"+url.toString());
            }
        });


        jumpConfig.captureLocale = locale;
        mPreferredCountryCode = countryCode;
        mPreferredLangCode = langCode;

        try {
            Jump.reinitialize(mContext, jumpConfig);

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                Log.i(LOG_TAG, "JANRAIN FAILED TO INITIALISE EOFException");
                mContext.deleteFile("jr_capture_flow");
                Jump.reinitialize(mContext, jumpConfig);
            }
            // e.printStackTrace();

        }
    }

    private HashMap getEngageAppId() {
        HashMap engageAppIdMap = new HashMap<String, String>();
        engageAppIdMap.put(DEV_CAPTURE_DOMAIN, "bdbppnbjfcibijknnfkk");
        engageAppIdMap.put(EVAL_CAPTURE_DOMAIN, "jgehpoggnhbagolnihge");
        engageAppIdMap.put(TEST_CAPTURE_DOMAIN, "fhbmobeahciagddgfidm");
        engageAppIdMap.put(PROD_CAPTURE_DOMAIN, "ddjbpmgpeifijdlibdio");

        return engageAppIdMap;
    }

    private HashMap getCaptureId() {

        HashMap captureAppIdMap = new HashMap<String, String>();
        captureAppIdMap.put(DEV_CAPTURE_DOMAIN, "eupac7ugz25x8dwahvrbpmndf8");
        captureAppIdMap.put(EVAL_CAPTURE_DOMAIN, "nt5dqhp6uck5mcu57snuy8uk6c");
        captureAppIdMap.put(TEST_CAPTURE_DOMAIN, "x7nftvwfz8e8vcutz49p8eknqp");
        captureAppIdMap.put(PROD_CAPTURE_DOMAIN, "hffxcm638rna8wrxxggx2gykhc");
        return captureAppIdMap;
    }
}
