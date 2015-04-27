package com.philips.cl.di.reg.settings;

import com.janrain.android.Jump;
import com.janrain.android.JumpConfig;
import com.philips.cl.di.localematch.LocaleMatchListener;
import com.philips.cl.di.localematch.LocaleMatchNotifier;
import com.philips.cl.di.localematch.PILLocale;
import com.philips.cl.di.localematch.PILLocaleManager;
import com.philips.cl.di.localematch.enums.Catalog;
import com.philips.cl.di.localematch.enums.LocaleMatchError;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.localematch.enums.Sector;
import com.philips.cl.di.reg.errormapping.CheckLocale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class EvalRegistrationSettings extends RegistrationSettings implements
		LocaleMatchListener {

	private String mCountryCode;
	private String mLanguageCode;

	String mCaptureClientId = null;
	String mLocale = null;
	boolean mIsIntialize = false;
	private Context mContext = null;

	private String LOG_TAG = "RegistrationAPI";

	private static String DEV_EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

	private static String DEV_EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

	private String EVAL_ENGAGE_APP_ID = "jgehpoggnhbagolnihge";
	private String EVAL_CAPTURE_DOMAIN = "philips.eval.janraincapture.com";
	private String EVAL_CAPTURE_FLOW_VERSION = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";
	private String EVAL_CAPTURE_APP_ID = "nt5dqhp6uck5mcu57snuy8uk6c";
	//private String EVAL_REGISTER_FORGOT_EMAIL_NATIVE = "https://philips.eval.janraincapture.com/oauth/forgot_password_native/"; //not used
	private String EVAL_REGISTER_ACTIVATION_URL = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";
	private String EVAL_REGISTER_FORGOT_MAIL_URL = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

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

		PILLocaleManager localeManager = new PILLocaleManager();
		localeManager.init(context, this);
		localeManager.refresh(context, mLanguageCode, mCountryCode);

	}

	@Override
	public void onLocaleMatchRefreshed(String locale) {

		PILLocaleManager manager = new PILLocaleManager();
		PILLocale pilLocaleInstance = manager
				.currentLocaleWithLanguageFallbackForPlatform(locale,
						Platform.JANRAIN, Sector.B2C, Catalog.MOBILE);

		if (null != pilLocaleInstance) {
			Log.i(LOG_TAG,
					"REGAPI, onLocaleMatchRefreshed from app RESULT = "
							+ pilLocaleInstance.getCountrycode()
							+ pilLocaleInstance.getLanguageCode()
							+ pilLocaleInstance.getLocaleCode());
			initialiseConfigParameters(pilLocaleInstance.getLanguageCode()
					.toLowerCase()
					+ "-"
					+ pilLocaleInstance.getCountrycode().toUpperCase());
		} else {
			Log.i(LOG_TAG,
					"REGAPI, onLocaleMatchRefreshed from app RESULT = NULL");
			String verifiedLocale = verifyInputLocale(mLanguageCode + "-"
					+ mCountryCode);
			initialiseConfigParameters(verifiedLocale);
		}

		unRegisterLocaleMatchListener();

	}

	@Override
	public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {

		Log.i(LOG_TAG, "REGAPI, onErrorOccurredForLocaleMatch error = " + error);
		unRegisterLocaleMatchListener();
		String verifiedLocale = verifyInputLocale(mLanguageCode + "-"
				+ mCountryCode);
		initialiseConfigParameters(verifiedLocale);

	}

	private void unRegisterLocaleMatchListener() {
		LocaleMatchNotifier notifier = LocaleMatchNotifier.getIntance();
		notifier.unRegisterLocaleMatchChange(this);
	}

	private String verifyInputLocale(String locale) {
		CheckLocale checkLocale = new CheckLocale();
		String localeCode = checkLocale.checkLanguage(locale);

		if ("zh-TW".equals(localeCode)) {
			localeCode = "zh-HK";
		}
		return localeCode;
	}

	private void initialiseConfigParameters(String locale) {

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
		//mRegisterForgotEmailNative = EVAL_REGISTER_FORGOT_EMAIL_NATIVE; //not used

		mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
		mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;

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
		
		jumpConfig.captureRedirectUri = EVAL_REGISTER_ACTIVATION_URL
				+ "?country=" + countryCode + "&catalogType=CONSUMER&language="
				+ langCode;
		jumpConfig.captureRecoverUri = EVAL_REGISTER_FORGOT_MAIL_URL
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
