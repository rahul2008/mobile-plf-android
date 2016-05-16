
package com.philips.cdp.registration.settings;

import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;

public class EvalRegistrationSettings extends RegistrationSettings {

    private String LOG_TAG = "RegistrationAPI";

    private static String EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String EVAL_PRX_RESEND_CONSENT_URL = "https://acc.usa.philips.com/prx/registration/resendConsentMail";

    private String EVAL_ENGAGE_APP_ID = "jgehpoggnhbagolnihge";

    private String EVAL_CAPTURE_DOMAIN = "philips.eval.janraincapture.com";

    private String EVAL_BASE_CAPTURE_URL = "https://philips.eval.janraincapture.com";

    private String EVAL_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";

    private String EVAL_CAPTURE_APP_ID = "nt5dqhp6uck5mcu57snuy8uk6c";

    /**
     * Activation Account URL
     */

    private String EVAL_REGISTER_ACTIVATION_URL = "https://acc.philips.com/ps/verify-account";


    private String EVAL_REGISTER_FORGOT_MAIL_URL = "https://acc.philips.com/ps/reset-password?cl=mob";


    @Override
    public void initialiseConfigParameters(String locale) {

        Log.i(LOG_TAG, "initialiseCofig, locale = " + locale);

        JumpConfig jumpConfig = new JumpConfig();
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

        jumpConfig.engageAppId = EVAL_ENGAGE_APP_ID;
        jumpConfig.captureDomain = EVAL_CAPTURE_DOMAIN;
        jumpConfig.captureFlowVersion = EVAL_CAPTURE_FLOW_VERSION;
        jumpConfig.captureAppId = EVAL_CAPTURE_APP_ID;

        mProductRegisterUrl = EVAL_PRODUCT_REGISTER_URL;
        mProductRegisterListUrl = EVAL_PRODUCT_REGISTER_LIST_URL;

        mResendConsentUrl = EVAL_PRX_RESEND_CONSENT_URL;

        mRegisterBaseCaptureUrl = EVAL_BASE_CAPTURE_URL;

        String localeArr[] = locale.split("-");
        String langCode = null;
        String countryCode = null;

        if (localeArr != null && localeArr.length > 1) {
            langCode = localeArr[0];
            countryCode = localeArr[1];
        } else {
            langCode = "en";
            countryCode = "US";
        }


        jumpConfig.captureRedirectUri = EVAL_REGISTER_ACTIVATION_URL + "?loc=" + langCode + "_" + countryCode;


        jumpConfig.captureRecoverUri = EVAL_REGISTER_FORGOT_MAIL_URL + "&loc=" + langCode + "_" + countryCode;
        jumpConfig.captureLocale = locale;

        mPreferredCountryCode = countryCode;
        mPreferredLangCode = langCode;

        try {
            Jump.reinitialize(mContext, jumpConfig);

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                Log.i(LOG_TAG, "JANRAIN FAILED TO INITIALISE EOFException");
                //clear flow file
                mContext.deleteFile("jr_capture_flow");
                Jump.reinitialize(mContext, jumpConfig);
            }
            // e.printStackTrace();

        }

    }


}
