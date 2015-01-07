package com.philips.cl.di.reg.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

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

public class JanrainConfigurationSettings implements LocaleMatchListener {

	private String mRegistrationVersion = null;

	private String mProductEngageAppId = "ddjbpmgpeifijdlibdio";
	private String mProductCaptureDomain = "philips.janraincapture.com";
	private String mProductCaptureFlowVersion = "HEAD"; // "e67f2db4-8a9d-4525-959f-a6768a4a2269";
	private String mProductCaptureAppId = "hffxcm638rna8wrxxggx2gykhc";
	private String mProductRegisterForgotEmailNative = "https://philips.janraincapture.com/oauth/forgot_password_native/";
	private String mProductRegisterActivationUrl = "https://secure.philips.co.uk/myphilips/activateUser.jsp";
	private String mProductRegisterForgotEmail = "https://secure.philips.co.uk/myphilips/resetPassword.jsp";

	private String mEvalEngageAppId = "jgehpoggnhbagolnihge";
	private String mEvalCaptureDomain = "philips.eval.janraincapture.com";
	private String mEvalCaptureFlowVersion = "HEAD";// "f4a28763-840b-4a13-822a-48b80063a7bf";
	private String mEvalCaptureAppId = "nt5dqhp6uck5mcu57snuy8uk6c";
	private String mEvalRegisterForgotEmailNative = "https://philips.eval.janraincapture.com/oauth/forgot_password_native/";
	private String mEvalRegisterActivationUrl = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";
	private String mEvalRegisterForgotEmail = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

	private String mDeviceEngageAppId = "bdbppnbjfcibijknnfkk";
	private String mDeviceCaptureDomain = "philips.dev.janraincapture.com";
	private String mDeviceCaptureFlowVersion = "HEAD"; // "9549a1c4-575a-4042-9943-45b87a4f03f0";
	private String mDeviceCaptureAppId = "eupac7ugz25x8dwahvrbpmndf8";
	private String mDeviceRegisterForgotEmailNative = "https://philips.dev.janraincapture.com/oauth/forgot_password_native/";
	private String mDeviceRegisterActivationUrl = "https://secure.qat1.consumer.philips.co.uk/myphilips/activateUser.jsp";
	private String mDeviceRegisterForgotEmail = "https://secure.qat1.consumer.philips.co.uk/myphilips/resetPassword.jsp";

	private String mRegister_Forgot_Email_Native;
	private String mRegister_Activation_Url;
	private String mRegister_Forgot_Email;
	private String mCountryCode;
	private String mLanguageCode;

	String mCaptureClientId = null;
	String mLocale = null;
	boolean mIsIntialize = false;
	private Context mContext = null;

	private String LOG_TAG = "RegistrationAPI";

	private static JanrainConfigurationSettings mConfigSettings = null;

	private String mProductRegisterUrl = null;

	private String mProductRegisterListUrl = null;

	private static String DEV_EVAL_PRODUCT_REGISTER_URL = "https://acc.philips.co.uk/prx/registration/";

	private static String DEV_EVAL_PRODUCT_REGISTER_LIST_URL = "https://acc.philips.co.uk/prx/registration.registeredProducts/";

	private static String PROD_PRODUCT_REGISTER_URL = "https://www.philips.co.uk/prx/registration/";

	private static String PROD_PRODUCT_REGISTER_LIST_URL = "https://www.philips.co.uk/prx/registration.registeredProducts/";
	
	public static final String REGISTRATION_API_PREFERENCE = "REGAPI_PREFERENCE";
	
	public static final String MICROSITE_ID = "microSiteID";
	
	private String mPreferredCountryCode;
	
	private String mPreferredLangCode;
	
	public String REGISTRATION_USE_PRODUCTION = "REGISTRATION_USE_PRODUCTION";
	
	public String REGISTRATION_USE_EVAL = "REGISTRATION_USE_EVAL";
	
	public String REGISTRATION_USE_DEVICE = "REGISTRATION_USE_DEVICE";
	
	private JanrainConfigurationSettings() {

	}

	public static JanrainConfigurationSettings getInstance() {
		if (mConfigSettings == null) {
			mConfigSettings = new JanrainConfigurationSettings();
		}
		return mConfigSettings;
	}

	public void init(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale) {
		SharedPreferences pref = context.getSharedPreferences(
				REGISTRATION_API_PREFERENCE, 0);
		Editor editor = pref.edit();
		editor.putString(MICROSITE_ID, microSiteId);
		editor.commit();
		
		mRegistrationVersion = registrationType;
		mCaptureClientId = captureClientId;
		mLocale = locale;
		mIsIntialize = isintialize;
		mContext = context;

		String localeArr[] = locale.split("_");

		if (localeArr != null && localeArr.length > 0) {
			mLanguageCode = localeArr[0].toLowerCase();
			mCountryCode = localeArr[1].toUpperCase();
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
		Log.i(LOG_TAG, "REGAPI, onErrorOccurredForLocaleMatch error = "
				+ error);
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

		// VARIABLES FOR VERSION CHECK TO BE SET IN APP
		if (mRegistrationVersion == REGISTRATION_USE_PRODUCTION) {
			jumpConfig.engageAppId = mProductEngageAppId;
			jumpConfig.captureDomain = mProductCaptureDomain;
			jumpConfig.captureFlowVersion = mProductCaptureFlowVersion;
			jumpConfig.captureAppId = mProductCaptureAppId;
			mRegister_Forgot_Email_Native = mProductRegisterForgotEmailNative;
			mRegister_Activation_Url = mProductRegisterActivationUrl;
			mRegister_Forgot_Email = mProductRegisterForgotEmail;

			mProductRegisterUrl = PROD_PRODUCT_REGISTER_URL;
			mProductRegisterListUrl = PROD_PRODUCT_REGISTER_LIST_URL;

		} else if (mRegistrationVersion == REGISTRATION_USE_EVAL) {
			jumpConfig.engageAppId = mEvalEngageAppId;
			jumpConfig.captureDomain = mEvalCaptureDomain;
			jumpConfig.captureFlowVersion = mEvalCaptureFlowVersion;
			jumpConfig.captureAppId = mEvalCaptureAppId;
			mRegister_Forgot_Email_Native = mEvalRegisterForgotEmailNative;
			mRegister_Activation_Url = mEvalRegisterActivationUrl;
			mRegister_Forgot_Email = mEvalRegisterForgotEmail;

			mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
			mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;

		} else if (mRegistrationVersion == REGISTRATION_USE_DEVICE) {
			jumpConfig.engageAppId = mDeviceEngageAppId;
			jumpConfig.captureDomain = mDeviceCaptureDomain;
			jumpConfig.captureFlowVersion = mDeviceCaptureFlowVersion;
			jumpConfig.captureAppId = mDeviceCaptureAppId;

			mRegister_Forgot_Email_Native = mDeviceRegisterForgotEmailNative;
			mRegister_Activation_Url = mDeviceRegisterActivationUrl;
			mRegister_Forgot_Email = mDeviceRegisterForgotEmail;

			mProductRegisterUrl = DEV_EVAL_PRODUCT_REGISTER_URL;
			mProductRegisterListUrl = DEV_EVAL_PRODUCT_REGISTER_LIST_URL;
		}

		String localeArr[] = locale.split("-");
		String langCode = null;
		String countryCode = null;

		if (localeArr != null && localeArr.length > 0) {
			langCode = localeArr[0];
			countryCode = localeArr[1];
		}

		jumpConfig.captureCountryCode = countryCode;
		jumpConfig.captureLanguageCode = langCode;
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

	public String getProductRegisterUrl() {
		return mProductRegisterUrl;
	}

	public String getProductRegisterListUrl() {
		return mProductRegisterListUrl;
	}
	
	public String getPreferredCountryCode() {
		return mPreferredCountryCode;
	}
	
	public String getPreferredLangCode() {
		return mPreferredLangCode;
	}

}
