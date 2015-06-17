package com.philips.cl.di.digitalcare.localematch;

import android.content.Context;

import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;
import com.philips.cl.di.localematch.LocaleMatchListener;
import com.philips.cl.di.localematch.PILLocale;
import com.philips.cl.di.localematch.PILLocaleManager;
import com.philips.cl.di.localematch.enums.Catalog;
import com.philips.cl.di.localematch.enums.LocaleMatchError;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.localematch.enums.Sector;

public class LocaleMatchHandler implements LocaleMatchListener {

	private Context mContext = null;
	private final String TAG = LocaleMatchHandler.class.getSimpleName();

	public enum FALLBACK {
		COUNTRY, LANGUAGE
	};

	public LocaleMatchHandler(Context context) {
		mContext = context;
		DigiCareLogger.v(TAG, "Contructor..");
	}

	public void initializeLocaleMatchService() {
		DigiCareLogger.v(TAG, "initializing the Service");
		PILLocaleManager mPLocaleManager = new PILLocaleManager();
		mPLocaleManager.init(mContext, this);
		mPLocaleManager.refresh(mContext,
				DigitalCareConfigManager.getInstance(mContext).getLocale()
						.getLanguage(),
				DigitalCareConfigManager.getInstance(mContext).getLocale()
						.getCountry());
	}

	@Override
	public void onErrorOccurredForLocaleMatch(LocaleMatchError arg0) {
		DigiCareLogger.d(LocaleMatchHandler.class.getSimpleName(),
				"piLocale received on ErrorLIstener");
	}

	@Override
	public void onLocaleMatchRefreshed(String arg0) {
		PILLocaleManager mPILocaleManager = new PILLocaleManager();

		PILLocale mPilLocale = mPILocaleManager
				.currentLocaleWithCountryFallbackForPlatform(
						DigitalCareConfigManager.getInstance(mContext)
								.getLocale().toString(), Platform.PRX,
						Sector.B2C, Catalog.MOBILE);
		if (mPilLocale != null) {
			DigiCareLogger.d(
					TAG,
					"Country Code : " + mPilLocale.getCountrycode()
							+ " & Language Code : "
							+ mPilLocale.getLanguageCode() + " & Locale is : "
							+ mPilLocale.getLocaleCode());
			DigitalCareConfigManager.getInstance(mContext).setLocale(
					mPilLocale.getLocaleCode());
		} else {
			DigiCareLogger.d(TAG, "PILocale received null");
		}
	}
}
