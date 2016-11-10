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
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;


public class RegistrationSettingsURL extends RegistrationSettings {

    private String LOG_TAG = "RegistrationAPI";

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
        jumpConfig.captureTraditionalSignInFormName = "userInformationForm";
        jumpConfig.traditionalSignInType = Jump.TraditionalSignInType.EMAIL;
        jumpConfig.captureFlowVersion = EVAL_CAPTURE_FLOW_VERSION;


        assignBasedonEnvironment(RegistrationConfiguration.getInstance().getRegistrationEnvironment());


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

        switch (appIdentity.getAppState()) {
            case STAGING:
            case ACCEPTANCE:
                jumpConfig.engageAppId = "jgehpoggnhbagolnihge";
                jumpConfig.captureAppId = "nt5dqhp6uck5mcu57snuy8uk6c";
                break;
            case PRODUCTION:
                jumpConfig.engageAppId = "ddjbpmgpeifijdlibdio";
                jumpConfig.captureAppId = "hffxcm638rna8wrxxggx2gykhc";
                break;
            case DEVELOPMENT:
                jumpConfig.engageAppId = "bdbppnbjfcibijknnfkk";
                jumpConfig.captureAppId = "eupac7ugz25x8dwahvrbpmndf8";
                break;
            case TEST:
                jumpConfig.engageAppId = "fhbmobeahciagddgfidm";
                jumpConfig.captureAppId = "x7nftvwfz8e8vcutz49p8eknqp";
                break;
            default:
                throw new RuntimeException("Invalid app state " + appIdentity.getAppState());
        }

        initServiceDiscovery(locale);
    }

    private void assignBasedonEnvironment(String registrationEnv) {
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

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.janrain.api", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String error) {
                        RLog.d(RLog.SERVICE_DISCOVERY, " onError  : userreg.janrain.api :" + error);
                        EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        String janrainURL = url.toString().substring(8);
                        jumpConfig.captureDomain = janrainURL.toString();
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
