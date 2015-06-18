package com.philips.cl.di.digitalcare.localematch;

import android.content.Context;

import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;
import com.philips.cl.di.localematch.LocaleMatchListener;
import com.philips.cl.di.localematch.LocaleMatchNotifier;
import com.philips.cl.di.localematch.PILLocale;
import com.philips.cl.di.localematch.PILLocaleManager;
import com.philips.cl.di.localematch.enums.Catalog;
import com.philips.cl.di.localematch.enums.LocaleMatchError;
import com.philips.cl.di.localematch.enums.Platform;
import com.philips.cl.di.localematch.enums.Sector;

/**
 * This is bridge class to interact with LocaleMatch SDK to check the country
 * fallback with respect to the ConsumerCare values set by the Launcher
 * Application.
 * 
 * @author naveen@philips.com
 */
public class LocaleMatchHandler implements LocaleMatchListener {

	private Context mContext = null;
	private final String TAG = LocaleMatchHandler.class.getSimpleName();
	private DigitalCareConfigManager mConfigManager = null;

	public enum FALLBACK {
		COUNTRY, LANGUAGE
	};

	public LocaleMatchHandler(Context context) {
		mContext = context;
		mConfigManager = DigitalCareConfigManager.getInstance(mContext);
		DigiCareLogger.v(TAG, "Contructor..");
	}

	public void initializeLocaleMatchService() {
		DigiCareLogger.v(TAG, "initializing the Service");
		PILLocaleManager mPLocaleManager = new PILLocaleManager();
		mPLocaleManager.init(mContext, this);
		mPLocaleManager.refresh(mContext, mConfigManager.getLocale()
				.getLanguage(), mConfigManager.getLocale().getCountry());
	}

	@Override
	public void onErrorOccurredForLocaleMatch(LocaleMatchError arg0) {
		DigiCareLogger.v(LocaleMatchHandler.class.getSimpleName(),
				"piLocale received on ErrorLIstener");

		unRegisterLocaleMatchListener();
	}

	@Override
	public void onLocaleMatchRefreshed(String arg0) {
		PILLocaleManager mPILocaleManager = new PILLocaleManager();

		DigiCareLogger.v(TAG, "Sector from Config : "
				+ mConfigManager.getConsumerProductInfo().getSector());

		String mSector = mConfigManager.getConsumerProductInfo().getSector();

		int mSectorValue = isSectorExistsInLocaleMatch(mSector);

		if (mSectorValue != 0) {

			DigiCareLogger.v(TAG, "Sector exists : " + setSector(mSectorValue));

			PILLocale mPilLocale = mPILocaleManager
					.currentLocaleWithCountryFallbackForPlatform(
							DigitalCareConfigManager.getInstance(mContext)
									.getLocale().toString(), Platform.PRX,
							setSector(mSectorValue), Catalog.CONSUMER);
			if (mPilLocale != null) {
				DigiCareLogger.v(
						TAG,
						"Country Code : " + mPilLocale.getCountrycode()
								+ " & Language Code : "
								+ mPilLocale.getLanguageCode()
								+ " & Locale is : "
								+ mPilLocale.getLocaleCode());
				mConfigManager.setLocale(mPilLocale.getLocaleCode());
			} else {
				DigiCareLogger.v(TAG, "PILocale received null");
			}

		} else {
			DigiCareLogger.v(TAG, "Sector Not exists");
		}

		unRegisterLocaleMatchListener();
	}

	private void unRegisterLocaleMatchListener() {
		DigiCareLogger.v(TAG, "unregistering receiver");
		LocaleMatchNotifier notifier = LocaleMatchNotifier.getIntance();
		notifier.unRegisterLocaleMatchChange(this);
	}

	protected int isSectorExistsInLocaleMatch(String sector) {
		if (sector.contains(Sector.B2B_HC.toString()))
			return 1;
		else if (sector.contains(Sector.B2B_LI.toString()))
			return 2;
		else if (sector.contains(Sector.B2C.toString()))
			return 3;
		else if (sector.contains(Sector.DEFAULT.toString()))
			return 4;
		else
			return 0;
	}

	protected Sector setSector(int value) {
		switch (value) {
		case 1:
			return Sector.B2B_HC;
		case 2:
			return Sector.B2B_LI;
		case 3:
			return Sector.B2C;
		default:
			return Sector.DEFAULT;
		}
	}
}