
package com.philips.cdp.registration.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;

public class TestingRegistrationSettings extends RegistrationSettings {

    private String mCountryCode;

    private String mLanguageCode;

    String mCaptureClientId = null;

    String mLocale = null;

    boolean mIsIntialize = false;

    private Context mContext = null;

    private String LOG_TAG = "RegistrationAPI";

    private static String TEST_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

    private static String TEST_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

    private static String TEST_PRX_RESEND_CONSENT_URL = "https://tst.usa.philips.com/prx/registration/resendConsentMail";

    private static String TEST_REGISTER_COPPA_ACTIVATION_URL = "https://tst.philips.com/ps/user-registration/consent.html";

    private String TEST_ENGAGE_APP_ID = "fhbmobeahciagddgfidm";

    private String TEST_CAPTURE_DOMAIN = "philips-test.dev.janraincapture.com";

    private String TEST_BASE_CAPTURE_URL = "https://philips-test.dev.janraincapture.com";

    private String TEST_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";

    private String TEST_CAPTURE_APP_ID = "x7nftvwfz8e8vcutz49p8eknqp";

    /**
     * Activation Account URL
     */

    private String TEST_REGISTER_ACTIVATION_URL = "http://tst.philips.com/ps/verify-account";
    /**
     * Forgot Password URL
     */
    private String TEST_REGISTER_FORGOT_MAIL_URL = "https://philips-test.dev.janraincapture.com/oauth/forgot_password_native/";

    @Override
    public void intializeRegistrationSettings(Context context, String captureClientId,
                                              String microSiteId, String registrationType, boolean isintialize, String locale) {
        SharedPreferences pref = context.getSharedPreferences(REGISTRATION_API_PREFERENCE, 0);
        Editor editor = pref.edit();
        editor.putString(MICROSITE_ID, microSiteId);
        editor.commit();

        mCaptureClientId = captureClientId;
        mLocale = locale;
        mIsIntialize = isintialize;
        mContext = context;

        String localeArr[] = locale.split("_");

        if (localeArr != null && localeArr.length > 1) {
            mLanguageCode = localeArr[0].toLowerCase();
            mCountryCode = localeArr[1].toUpperCase();
        } else {
            mLanguageCode = "en";
            mCountryCode = "US";
        }

        LocaleMatchHelper localeMatchHelper = new LocaleMatchHelper(mContext, mLanguageCode,
                mCountryCode);
        Log.i("registration", "" + localeMatchHelper);
    }

    @Override
    public void initialiseConfigParameters(String locale) {

        Log.i(LOG_TAG, "initialiseCofig, locale = " + locale);

        JumpConfig jumpConfig = new JumpConfig();
        jumpConfig.captureClientId = mCaptureClientId;
        jumpConfig.captureFlowName = getFlowName();
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
        mRegisterCoppaActivationUrl = TEST_REGISTER_COPPA_ACTIVATION_URL;

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

        if (RegistrationHelper.getInstance().isCoppaFlow()) {
            jumpConfig.captureRedirectUri = TEST_REGISTER_COPPA_ACTIVATION_URL;
        } else {
            jumpConfig.captureRedirectUri = TEST_REGISTER_ACTIVATION_URL + "?loc=" + langCode + "_" + countryCode;
        }

        jumpConfig.captureRecoverUri = TEST_REGISTER_FORGOT_MAIL_URL + "?country=" + countryCode
                + "&catalogType=CONSUMER&language=" + langCode;
        jumpConfig.captureLocale = locale;

        mPreferredCountryCode = countryCode;
        mPreferredLangCode = langCode;

        try {
            if (mIsIntialize) {
                Jump.init(mContext, jumpConfig);
            } else {
                Jump.reinitialize(mContext, jumpConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "JANRAIN FAILED TO INITIALISE");
        }

    }
}
