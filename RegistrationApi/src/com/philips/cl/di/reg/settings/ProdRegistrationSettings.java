package com.philips.cl.di.reg.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;

public class ProdRegistrationSettings extends RegistrationSettings {

	private String PRODUCT_ENGAGE_APP_ID = "ddjbpmgpeifijdlibdio";
	private String PRODUCT_CAPTURE_DOMAIN = "philips.janraincapture.com";
	private String PRODUCT_CAPTURE_FLOW_VERSION = "HEAD"; // "e67f2db4-8a9d-4525-959f-a6768a4a2269";
	private String PRODUCT_CAPTURE_APP_ID = "hffxcm638rna8wrxxggx2gykhc";
	private String PRODUCTION_REGISTER_ACTIVATION_URL = "https://secure.philips.co.uk/myphilips/activateUser.jsp";
	private String PRODUCTION_REGISTER_FORGOT_MAIL_URL = "https://secure.philips.co.uk/myphilips/resetPassword.jsp";

	private String mCountryCode;
	private String mLanguageCode;

	String mCaptureClientId = null;
	String mLocale = null;
	boolean mIsIntialize = false;
	private Context mContext = null;

	private String LOG_TAG = "RegistrationAPI";

	private static String PROD_PRODUCT_REGISTER_URL = "https://www.philips.co.uk/prx/registration/";

	private static String PROD_PRODUCT_REGISTER_LIST_URL = "https://www.philips.co.uk/prx/registration.registeredProducts/";

	@Override
	public void intializeRegistrationSettings(Context context,
			String captureClientId, String microSiteId,
			String registrationType, boolean isintialize, String locale) {
		SharedPreferences pref = context.getSharedPreferences(
				REGISTRATION_API_PREFERENCE, 0);
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

		LocaleMatchHelper localeMatchHelper = new LocaleMatchHelper(mContext,
				mLanguageCode, mCountryCode);

	}

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

		jumpConfig.engageAppId = PRODUCT_ENGAGE_APP_ID;
		jumpConfig.captureDomain = PRODUCT_CAPTURE_DOMAIN;
		jumpConfig.captureFlowVersion = PRODUCT_CAPTURE_FLOW_VERSION;
		jumpConfig.captureAppId = PRODUCT_CAPTURE_APP_ID;

		mProductRegisterUrl = PROD_PRODUCT_REGISTER_URL;
		mProductRegisterListUrl = PROD_PRODUCT_REGISTER_LIST_URL;

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

		jumpConfig.captureRedirectUri = PRODUCTION_REGISTER_ACTIVATION_URL
				+ "?country=" + countryCode + "&catalogType=CONSUMER&language="
				+ langCode;
		jumpConfig.captureRecoverUri = PRODUCTION_REGISTER_FORGOT_MAIL_URL
				+ "?country=" + countryCode + "&catalogType=CONSUMER&language="
				+ langCode;
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
