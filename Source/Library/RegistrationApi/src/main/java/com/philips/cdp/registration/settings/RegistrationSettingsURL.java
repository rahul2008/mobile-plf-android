package com.philips.cdp.registration.settings;

import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;


public class RegistrationSettingsURL extends RegistrationSettings {

    private String LOG_TAG = "RegistrationAPI";

    private static String EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String EVAL_PRX_RESEND_CONSENT_URL = "https://acc.usa.philips.com/prx/registration/resendConsentMail";

    private String EVAL_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";

    private JumpConfig jumpConfig;

    private String langCode;

    private String countryCode;

    /**
     * {@code initialiseConfigParameters} method builds configuration for information in {@code EvalRegistrationSettings}
     *
     * @param locale used to add the requested locale to the configuration
     */
    @Override
    public void initialiseConfigParameters(final String locale) {

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
       // jumpConfig.captureTraditionalSignInFormName = "userInformationForm";

        //Temp: Below code has been modified for CHINA FLOW.
        jumpConfig.captureTraditionalSignInFormName = "userInformationMobileForm";

        jumpConfig.traditionalSignInType = Jump.TraditionalSignInType.EMAIL;
        jumpConfig.captureFlowVersion = EVAL_CAPTURE_FLOW_VERSION;

        // Conditional change
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

        AppIdentityInterface appIdentity = RegistrationHelper.getInstance().getAppInfraInstance().getAppIdentity();
        RLog.d(RLog.SERVICE_DISCOVERY, "Environment : " + appIdentity.getAppState());
        RLog.d(RLog.SERVICE_DISCOVERY, "Locale : " + locale);

        if (appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.STAGING)) ||
                appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.EVALUATION))) {
            jumpConfig.engageAppId = "jgehpoggnhbagolnihge";
            jumpConfig.captureAppId = "nt5dqhp6uck5mcu57snuy8uk6c";
        } else if (appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.PRODUCTION))) {
            jumpConfig.engageAppId = "ddjbpmgpeifijdlibdio";
            jumpConfig.captureAppId = "hffxcm638rna8wrxxggx2gykhc";
        } else if (appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.DEVELOPMENT))) {
            jumpConfig.engageAppId = "bdbppnbjfcibijknnfkk";
            jumpConfig.captureAppId = "euwkgsf83m56hqknjxgnranezh";
            // previews it was "eupac7ugz25x8dwahvrbpmndf8"
        } else if (appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.TESTING)) ||
                appIdentity.getAppState().toString().equalsIgnoreCase(String.valueOf(Configuration.TEST))) {
            jumpConfig.engageAppId = "fhbmobeahciagddgfidm";
            jumpConfig.captureAppId = "vdgkb3z57jpv93mxub34x73mqu";
        }

        initServiceDiscovery(locale);
    }

    private void initServiceDiscovery(final String locale) {

    AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
    final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

    serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.janrain.api", new
            ServiceDiscoveryInterface.OnGetServiceUrlListener() {
        @Override
        public void onError(ERRORVALUES errorvalues, String error) {
            RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.api :" + error);
            EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
        }

        @Override
        public void onSuccess(URL url) {
            String janrainURL = url.toString().substring(8);//Please don't remove this line.
            // Hard coded the url for china will be replsed by service disover
            //jumpConfig.captureDomain = "philips-china-eu.eu-dev.janraincapture.com";
            jumpConfig.captureDomain = "philips-china-test.eu-dev.janraincapture.com";//Testing
            //previews it was like this
            //janrainURL.toString();
            RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.janrain.api :" + url);

      serviceDiscoveryInterface.getServiceUrlWithCountryPreference(
            "userreg.landing.emailverif",
            new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

        @Override
        public void onError(ERRORVALUES errorvalues, String error) {
            RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.emailverif :"
                    + error);
            EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
        }

        @Override
        public void onSuccess(URL url) {
            jumpConfig.captureRedirectUri = url.toString() + "?loc=" + langCode + "_" + countryCode;
            RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.emailverif :" + url.toString());
            RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.emailverif :" + jumpConfig.captureRedirectUri);

            serviceDiscoveryInterface.getServiceUrlWithCountryPreference(
                    "userreg.landing.resetpass", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                @Override
                public void onError(ERRORVALUES errorvalues, String error) {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.landing.resetpass : " + error);
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                }

                @Override
                public void onSuccess(URL url) {
                    String modifiedUrl = url.toString().replaceAll("c-w", "myphilips");
                    jumpConfig.captureRecoverUri = modifiedUrl + "&loc=" + langCode + "_" + countryCode;
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.resetpass :" + modifiedUrl);
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.landing.resetpass :" + jumpConfig.captureRecoverUri);

                    jumpConfig.captureLocale = locale;
                    mPreferredCountryCode = countryCode;
                    mPreferredLangCode = langCode;

                    try {
                        Jump.reinitialize(mContext, jumpConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e instanceof RuntimeException) {
                            mContext.deleteFile("jr_capture_flow");
                            Jump.reinitialize(mContext, jumpConfig);
                        }
                    }
                }
            });
        }
    });
}
    });

         /*tobe used in future
          serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.smssupported", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                @Override
                public void onError(ERRORVALUES errorvalues, String error) {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.smssupported :" + error);
                    //NOtify failed
                }

                @Override
                public void onSuccess(URL url) {
                    RLog.d(RLog.SERVICE_DISCOVERY, " onSuccess  : userreg.smssupported :" + url.toString());
                }
            });*/

    }
}
