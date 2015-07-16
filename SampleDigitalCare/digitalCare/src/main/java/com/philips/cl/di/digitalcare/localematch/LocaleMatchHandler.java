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

import java.util.Locale;

/**
 * This is bridge class to interact with LocaleMatch SDK to check the country
 * fallback with respect to the ConsumerCare values set by the Launcher
 * Application.
 *
 * @author naveen@philips.com
 */
public class LocaleMatchHandler implements LocaleMatchListener {

    private final String TAG = LocaleMatchHandler.class.getSimpleName();
    private Context mContext = null;
    private DigitalCareConfigManager mConfigManager = null;
    private String mLanguageCode = null;
    private String mCountryCode = null;
    private Locale mLocale = null;
    private PILLocaleManager mPLocaleManager = null;

    public LocaleMatchHandler(Context context) {
        mContext = context;
        mPLocaleManager = new PILLocaleManager();
        mPLocaleManager.init(mContext, this);
        mConfigManager = DigitalCareConfigManager.getInstance();
        DigiCareLogger.v(TAG, "Contructor..");
    }

    public void initializeLocaleMatchService(String langCode, String countryCode) {
        DigiCareLogger.v(TAG, "initializing the Service  " + mLanguageCode + " " + mCountryCode);
        DigiCareLogger.v(TAG, "initializing the Service  mLocale " + mLocale);
        mLanguageCode = langCode;
        mCountryCode = countryCode;
        mLocale = new Locale(mLanguageCode, mCountryCode);

        mPLocaleManager.refresh(mContext, mLanguageCode,
                mCountryCode);
    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError arg0) {
        DigiCareLogger.v(LocaleMatchHandler.class.getSimpleName(),
                "piLocale received on ErrorLIstener");
        DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocale(mLocale);
    }

    @Override
    public void onLocaleMatchRefreshed(String arg0) {
        PILLocaleManager mPILocaleManager = new PILLocaleManager();
        DigiCareLogger.v(TAG, "onLocaleMatchRefreshed(), Sector from Config : "
                + mConfigManager.getConsumerProductInfo().getSector());

        String mSector = mConfigManager.getConsumerProductInfo().getSector();
        int mSectorValue = isSectorExistsInLocaleMatch(mSector);
        if (mSectorValue != 0) {
            DigiCareLogger.v(TAG, "mLocale : " + mLocale.toString());
            DigiCareLogger.v(TAG, "Sector exists : " + setSector(mSectorValue));
            DigiCareLogger.v(TAG, "Platform : " + Platform.PRX);
            DigiCareLogger.v(TAG, "Consumer : " + Catalog.CONSUMER);
if(mPILocaleManager == null){
    DigiCareLogger.v(TAG, "Manager = null");
}

            PILLocale mPilLocale = mPILocaleManager.currentLocaleWithCountryFallbackForPlatform(
                    mLanguageCode+"_"+mCountryCode, Platform.PRX,
                            setSector(mSectorValue), Catalog.CONSUMER);
            if (mPilLocale != null) {
                DigiCareLogger.v(TAG, "PILocale is Not null");
                DigiCareLogger.v(
                        TAG,
                        "Country Code : " + mPilLocale.getCountrycode()
                                + " & Language Code : "
                                + mPilLocale.getLanguageCode()
                                + " & Locale is : "
                                + mPilLocale.getLocaleCode());
                Locale locale = new Locale(mPilLocale.getLanguageCode(), mPilLocale.getCountrycode());
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocale(locale);

            } else {
                DigiCareLogger.v(TAG, "PILocale received null");
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocale(mLocale);
            }

        } else {
            DigiCareLogger.v(TAG, "Sector Not exists");
            DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocale(mLocale);
        }
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