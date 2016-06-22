
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
 * {@code ProdRegistrationSettings} class represents Production environment related configuration.
 *
 */
public class ProdRegistrationSettings extends RegistrationSettings {

    private String PROD_ENGAGE_APP_ID = "ddjbpmgpeifijdlibdio";

    private String PROD_CAPTURE_DOMAIN = "philips.janraincapture.com";

    private String PROD_BASE_CAPTURE_URL = "https://philips.janraincapture.com";


    private String PROD_CAPTURE_FLOW_VERSION = "HEAD"; // "e67f2db4-8a9d-4525-959f-a6768a4a2269";

    private String PROD_CAPTURE_APP_ID = "hffxcm638rna8wrxxggx2gykhc";

    private String PROD_REGISTER_ACTIVATION_URL = "https://www.philips.com/ps/verify-account";

    private String PROD_REGISTER_FORGOT_MAIL_URL = "https://www.philips.com/ps/reset-password?cl=mob";

    private static String PROD_PRX_RESEND_CONSENT_URL = "https://www.usa.philips.com/prx/registration/resendConsentMail";

    private String LOG_TAG = "RegistrationAPI";

    private static String PROD_PRODUCT_REGISTER_URL = "https://www.philips.co.uk/prx/registration/";

    private static String PROD_PRODUCT_REGISTER_LIST_URL = "https://www.philips.co.uk/prx/registration.registeredProducts/";


    /**
     * {@code initialiseConfigParameters} method builds configuration for information in {@code ProdRegistrationSettings}
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

        jumpConfig.engageAppId = PROD_ENGAGE_APP_ID;
        jumpConfig.captureDomain = PROD_CAPTURE_DOMAIN;
        jumpConfig.captureFlowVersion = PROD_CAPTURE_FLOW_VERSION;
        jumpConfig.captureAppId = PROD_CAPTURE_APP_ID;

        mProductRegisterUrl = PROD_PRODUCT_REGISTER_URL;
        mProductRegisterListUrl = PROD_PRODUCT_REGISTER_LIST_URL;

        mResendConsentUrl = PROD_PRX_RESEND_CONSENT_URL;

        mRegisterBaseCaptureUrl = PROD_BASE_CAPTURE_URL;

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


        jumpConfig.captureRedirectUri = PROD_REGISTER_ACTIVATION_URL + "?loc=" + langCode + "_" + countryCode;


        jumpConfig.captureRecoverUri = PROD_REGISTER_FORGOT_MAIL_URL + "&loc=" + langCode + "_" + countryCode;
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
