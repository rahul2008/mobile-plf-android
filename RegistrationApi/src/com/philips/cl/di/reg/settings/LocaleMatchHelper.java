package com.philips.cl.di.reg.settings;

import android.content.Context;
import android.util.Log;

import com.philips.cl.di.localematch.LocaleMatchListener;
import com.philips.cl.di.localematch.LocaleMatchNotifier;
import com.philips.cl.di.localematch.PILLocale;
import com.philips.cl.di.localematch.PILLocaleManager;
import com.philips.cl.di.localematch.enums.Catalog;
import com.philips.cl.di.localematch.enums.LocaleMatchError;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.localematch.enums.Sector;
import com.philips.cl.di.reg.errormapping.CheckLocale;

public class LocaleMatchHelper implements LocaleMatchListener {

	private String mCountryCode;
	private String mLanguageCode;
	private Context mContext = null;

	private String LOG_TAG = "RegistrationAPI";

	RegistrationHelper mHelper = RegistrationHelper.getInstance();

	public LocaleMatchHelper(Context mContext, String mLanguageCode,
			String mCountryCode) {
		super();
		this.mCountryCode = mCountryCode;
		this.mLanguageCode = mLanguageCode;
		this.mContext = mContext;

		refreshLocale();
	}

	private void refreshLocale() {
		PILLocaleManager localeManager = new PILLocaleManager();
		localeManager.init(mContext, this);
		localeManager.refresh(mContext, mLanguageCode, mCountryCode);
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

			mHelper.getRegistrationSettings().initialiseConfigParameters(
					pilLocaleInstance.getLanguageCode().toLowerCase() + "-"
							+ pilLocaleInstance.getCountrycode().toUpperCase());
		} else {
			Log.i(LOG_TAG,
					"REGAPI, onLocaleMatchRefreshed from app RESULT = NULL");
			String verifiedLocale = verifyInputLocale(mLanguageCode + "-"
					+ mCountryCode);
			mHelper.getRegistrationSettings().initialiseConfigParameters(
					verifiedLocale);
		}

		unRegisterLocaleMatchListener();

	}

	@Override
	public void onErrorOccurredForLocaleMatch(LocaleMatchError error) {

		Log.i(LOG_TAG, "REGAPI, onErrorOccurredForLocaleMatch error = " + error);
		unRegisterLocaleMatchListener();
		String verifiedLocale = verifyInputLocale(mLanguageCode + "-"
				+ mCountryCode);

		mHelper.getRegistrationSettings().initialiseConfigParameters(
				verifiedLocale);

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

}
