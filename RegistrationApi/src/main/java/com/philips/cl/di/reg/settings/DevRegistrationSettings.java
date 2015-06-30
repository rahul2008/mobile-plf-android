
package com.philips.cl.di.reg.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;

public class DevRegistrationSettings extends RegistrationSettings {

	private String DEVICE_ENGAGE_APP_ID = "bdbppnbjfcibijknnfkk";

	private String DEVICE_CAPTURE_DOMAIN = "philips.dev.janraincapture.com";

	private String DEVICE_CAPTURE_FLOW_VERSION = "HEAD"; // "9549a1c4-575a-4042-9943-45b87a4f03f0";

	private String DEVICE_CAPTURE_APP_ID = "eupac7ugz25x8dwahvrbpmndf8";

	private String DEVICE_REGISTER_ACTIVATION_URL = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";

	private String DEVICE_REGISTER_FORGOT_MAIL_URL = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

	private static String DEV_PRX_RESEND_CONSENT_URL = "http://10.128.41.113:4503/registration/resendConsentMail";

	private static String DEV_REGISTER_COPPA_ACTIVATION_URL = "http://10.128.41.111:4503/content/B2C/en_US/user-registration/coppa-consent.html";

	private String mCountryCode;

	private String mLanguageCode;

	String mCaptureClientId = null;

	String mLocale = null;

	boolean mIsIntialize = false;

	private Context mContext = null;

	private String LOG_TAG = "RegistrationAPI";

	private static String DEV_EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

	private static String DEV_EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

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

		jumpConfig.engageAppId = DEVICE_ENGAGE_APP_ID;
		jumpConfig.captureDomain = DEVICE_CAPTURE_DOMAIN;
		jumpConfig.captureFlowVersion = DEVICE_CAPTURE_FLOW_VERSION;
		jumpConfig.captureAppId = DEVICE_CAPTURE_APP_ID;

		mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
		mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;

		mResendConsentUrl = DEV_PRX_RESEND_CONSENT_URL;
		mRegisterCoppaActivationUrl = DEV_REGISTER_COPPA_ACTIVATION_URL;

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
			jumpConfig.captureRedirectUri = DEV_REGISTER_COPPA_ACTIVATION_URL + "?country="
			        + countryCode + "&catalogType=CONSUMER&language=" + langCode;
		} else {
			jumpConfig.captureRedirectUri = DEVICE_REGISTER_ACTIVATION_URL + "?country="
			        + countryCode + "&catalogType=CONSUMER&language=" + langCode;
		}

		jumpConfig.captureRecoverUri = DEVICE_REGISTER_FORGOT_MAIL_URL + "?country=" + countryCode
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
