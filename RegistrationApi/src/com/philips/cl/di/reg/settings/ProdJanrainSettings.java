/*package com.philips.cl.di.reg.settings;

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

public class ProdJanrainSettings extends RegistrationHelper implements LocaleMatchListener{

	private String PRODUCT_ENGAGE_APP_ID = "ddjbpmgpeifijdlibdio";
	private String PRODUCT_CAPTURE_DOMAIN = "philips.janraincapture.com";
	private String PRODUCT_CAPTURE_FLOW_VERSION = "HEAD"; // "e67f2db4-8a9d-4525-959f-a6768a4a2269";
	private String PRODUCT_CAPTURE_APP_ID = "hffxcm638rna8wrxxggx2gykhc";
	private String PRODUCT_REGISTER_FORGOT_EMAIL_NATIVE = "https://philips.janraincapture.com/oauth/forgot_password_native/";
	private String PRODUCTION_REGISTER_ACTIVATION_URL = "https://secure.philips.co.uk/myphilips/activateUser.jsp";
	private String PRODUCTION_REGISTER_FORGOT_MAIL_URL = "https://secure.philips.co.uk/myphilips/resetPassword.jsp";

	//private String mRegisterForgotEmailNative; //not used
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
	public void intializeJanrainSettings(Context context,
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


	@Override
	public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {
		Log.i(LOG_TAG, "REGAPI, onErrorOccurredForLocaleMatch error = " + error);
		unRegisterLocaleMatchListener();
		String verifiedLocale = verifyInputLocale(mLanguageCode + "-"
				+ mCountryCode);
		initialiseConfigParameters(verifiedLocale);
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

		jumpConfig.engageAppId = PRODUCT_ENGAGE_APP_ID;
		jumpConfig.captureDomain = PRODUCT_CAPTURE_DOMAIN;
		jumpConfig.captureFlowVersion = PRODUCT_CAPTURE_FLOW_VERSION;
		jumpConfig.captureAppId = PRODUCT_CAPTURE_APP_ID;
		//mRegisterForgotEmailNative = PRODUCT_REGISTER_FORGOT_EMAIL_NATIVE; // not used

		mProductRegisterUrl = PROD_PRODUCT_REGISTER_URL;
		mProductRegisterListUrl = PROD_PRODUCT_REGISTER_LIST_URL;

		String localeArr[] = locale.split("-");
		String langCode = null;
		String countryCode = null;

		if (localeArr != null && localeArr.length > 1) {
			langCode = localeArr[0];
			countryCode = localeArr[1];
		}else{
			langCode = "en";
			countryCode = "US";
		}

		jumpConfig.captureRedirectUri = PRODUCTION_REGISTER_ACTIVATION_URL + "?country=" + countryCode +"&catalogType=CONSUMER&language=" + langCode;
		jumpConfig.captureRecoverUri = PRODUCTION_REGISTER_FORGOT_MAIL_URL + "?country=" + countryCode + "&catalogType=CONSUMER&language=" + langCode;
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
*/