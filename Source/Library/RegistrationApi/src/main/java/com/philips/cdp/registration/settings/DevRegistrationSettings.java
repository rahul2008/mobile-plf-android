
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;

public class DevRegistrationSettings extends RegistrationSettings {

    private String DEV_ENGAGE_APP_ID = "bdbppnbjfcibijknnfkk";

    private String DEV_CAPTURE_DOMAIN = "philips.dev.janraincapture.com";

    private String DEV_BASE_CAPTURE_URL = "https://philips.dev.janraincapture.com";

    private String DEV_CAPTURE_FLOW_VERSION = "HEAD"; // "9549a1c4-575a-4042-9943-45b87a4f03f0";

    private String DEV_CAPTURE_APP_ID = "eupac7ugz25x8dwahvrbpmndf8";

    private String DEV_REGISTER_ACTIVATION_URL = "http://10.128.41.111:4503/content/B2C/en_GB/verify-account.html";

    private String DEV_REGISTER_FORGOT_MAIL_URL = "http://10.128.41.111:4503/content/B2C/en_GB/myphilips/reset-password.html?cl=mob";

    private static String DEV_PRX_RESEND_CONSENT_URL = "https://dev.philips.com/prx/registration/resendConsentMail";

    private String LOG_TAG = "RegistrationAPI";

    private static String DEV_EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String DEV_EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";


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

        jumpConfig.engageAppId = DEV_ENGAGE_APP_ID;
        jumpConfig.captureDomain = DEV_CAPTURE_DOMAIN;
        jumpConfig.captureFlowVersion = DEV_CAPTURE_FLOW_VERSION;
        jumpConfig.captureAppId = DEV_CAPTURE_APP_ID;

        mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
        mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;

        mResendConsentUrl = DEV_PRX_RESEND_CONSENT_URL;

        mRegisterBaseCaptureUrl = DEV_BASE_CAPTURE_URL;

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


        jumpConfig.captureRedirectUri = DEV_REGISTER_ACTIVATION_URL;


        jumpConfig.captureRecoverUri = DEV_REGISTER_FORGOT_MAIL_URL + "&loc=" + langCode + "_" + countryCode;
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
        }

    }

}
