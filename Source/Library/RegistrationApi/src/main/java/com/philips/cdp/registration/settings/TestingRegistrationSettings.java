
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

/**
 * {@code TestingRegistrationSettings} class represents Production environment related configuration.
 *
 */
public class TestingRegistrationSettings extends RegistrationSettings {

    private String LOG_TAG = "RegistrationAPI";

    private static String TEST_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String TEST_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String TEST_PRX_RESEND_CONSENT_URL = "https://tst.usa.philips.com/prx/registration/resendConsentMail";


    private String TEST_ENGAGE_APP_ID = "fhbmobeahciagddgfidm";

    private String TEST_CAPTURE_DOMAIN = "philips-test.dev.janraincapture.com";

    private String TEST_BASE_CAPTURE_URL = "https://philips-test.dev.janraincapture.com";

    private String TEST_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";

    private String TEST_CAPTURE_APP_ID = "x7nftvwfz8e8vcutz49p8eknqp";

    /**
     * Activation Account URL
     */

    private String TEST_REGISTER_ACTIVATION_URL = "http://tst.philips.com/ps/verify-account";

    private String TEST_REGISTER_FORGOT_MAIL_URL = "https://tst.philips.com/ps/reset-password?cl=mob";

    /**
     * {@code initialiseConfigParameters} method builds configuration for information in {@code TestingRegistrationSettings}
     * @param locale used to add the requested locale to the configuration
     */
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

        jumpConfig.engageAppId = TEST_ENGAGE_APP_ID;
        jumpConfig.captureDomain = TEST_CAPTURE_DOMAIN;
        jumpConfig.captureFlowVersion = TEST_CAPTURE_FLOW_VERSION;
        jumpConfig.captureAppId = TEST_CAPTURE_APP_ID;

        mProductRegisterUrl = TEST_PRODUCT_REGISTER_URL;
        mProductRegisterListUrl = TEST_PRODUCT_REGISTER_LIST_URL;

        mResendConsentUrl = TEST_PRX_RESEND_CONSENT_URL;

        mRegisterBaseCaptureUrl = TEST_BASE_CAPTURE_URL;

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


            jumpConfig.captureRedirectUri = TEST_REGISTER_ACTIVATION_URL + "?loc=" + langCode + "_" + countryCode;


        jumpConfig.captureRedirectUri = TEST_REGISTER_FORGOT_MAIL_URL + "&loc=" + langCode + "_" + countryCode;
        jumpConfig.captureLocale = locale;

        mPreferredCountryCode = countryCode;
        mPreferredLangCode = langCode;

        try {
            Jump.reinitialize(mContext, jumpConfig);
        } catch (Exception e) {
            if(e instanceof RuntimeException){
                Log.i(LOG_TAG, "JANRAIN FAILED TO INITIALISE EOFException");
                //clear flow file
                mContext.deleteFile("jr_capture_flow");
                Jump.reinitialize(mContext, jumpConfig);
            }
        }
    }
}
