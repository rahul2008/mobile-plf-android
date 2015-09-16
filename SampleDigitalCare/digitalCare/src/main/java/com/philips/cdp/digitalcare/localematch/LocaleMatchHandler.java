package com.philips.cdp.digitalcare.localematch;

import android.content.Context;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;

import java.util.HashMap;
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
    private static HashMap<String, String> mPRXMap = null;
    private LocaleMatchHandlerObserver mLocaleMatchHandlerObserver=null;

    public LocaleMatchHandler(Context context) {
        mContext = context;
        mPLocaleManager = new PILLocaleManager();
        mPLocaleManager.init(mContext, this);
        mConfigManager = DigitalCareConfigManager.getInstance();
        DigiCareLogger.v(TAG, "Contructor..");
    }

    public void initializeLocaleMatchService(String langCode, String countryCode) {
        DigiCareLogger.v(TAG, "initializing the Service  " + mLanguageCode + " " + mCountryCode);
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
        DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithCountryFallBack(mLocale);
    }

    @Override
    public void onLocaleMatchRefreshed(String arg0) {
        PILLocaleManager mPILocaleManager = new PILLocaleManager();
        DigiCareLogger.v(TAG, "onLocaleMatchRefreshed(), Sector from Config : "
                + mConfigManager.getConsumerProductInfo().getSector());

        String mSector = mConfigManager.getConsumerProductInfo().getSector();
        int mSectorValue = isSectorExistsInLocaleMatch(mSector);
        if (mSectorValue != 0) {

            PILLocale mPilLocaleWithCountryFallBack = mPILocaleManager.currentLocaleWithCountryFallbackForPlatform(
                    mLanguageCode + "_" + mCountryCode, Platform.PRX,
                    setSector(mSectorValue), Catalog.CONSUMER);
            PILLocale mPilLocaleWithLanguageFallBack = mPILocaleManager.currentLocaleWithLanguageFallbackForPlatform(
                    mLanguageCode + "_" + mCountryCode, Platform.PRX,
                    setSector(mSectorValue), Catalog.CONSUMER);


            if (mPilLocaleWithCountryFallBack != null) {
                DigiCareLogger.v(TAG, "PILocale is Not null");
                Locale locale = new Locale(mPilLocaleWithCountryFallBack.getLanguageCode(), mPilLocaleWithCountryFallBack.getCountrycode());
                DigiCareLogger.d(TAG, mPilLocaleWithCountryFallBack.getCountrycode() + "" + mPilLocaleWithCountryFallBack.getCountrycode());
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithCountryFallBack(locale);
                DigitalCareConfigManager.getInstance().getObserver().notificationReceived();

            } else {
                DigiCareLogger.v(TAG, "PILocale country fallback received null");
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithCountryFallBack(mLocale);
                DigitalCareConfigManager.getInstance().getObserver().notificationReceived();

            }

            if(mPilLocaleWithLanguageFallBack!=null){
                Locale locale = new Locale(mPilLocaleWithLanguageFallBack.getLanguageCode(), mPilLocaleWithLanguageFallBack.getCountrycode());
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithLanguageFallBack(locale);
                DigitalCareConfigManager.getInstance().getObserver().notificationReceived();
            }else{
                DigiCareLogger.v(TAG, "PILocale language fallback received null");
                DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithLanguageFallBack(mLocale);
                DigitalCareConfigManager.getInstance().getObserver().notificationReceived();
            }


        } else {
            DigiCareLogger.v(TAG, "Sector Not exists");
            //DigitalCareConfigManager.getInstance().setLocaleMatchResponseLocaleWithCountryFallBack(mLocale);
            DigitalCareConfigManager.getInstance().getObserver().notificationReceived();
        }
        initializePRXMap();
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

    public static void initializePRXMap(){
        mPRXMap = new HashMap<String,String>();
        mPRXMap.put("en_CA","www.philips.ca");
        mPRXMap.put("fr_CA","www.philips.ca");
        mPRXMap.put("en_US","www.usa.philips.com");
        mPRXMap.put("es_AR","www.philips.com.ar");
        mPRXMap.put("pt_BR","www.philips.com.br");
        mPRXMap.put("es_CL","www.philips.cl");
        mPRXMap.put("es_CE","www.centralamerica.philips.com");
        mPRXMap.put("es_CO","www.philips.com.co");
        mPRXMap.put("es_MX","www.philips.com.mx");
        mPRXMap.put("es_PE","www.philips.com.pe");
        mPRXMap.put("iw_IL","www.philips.co.il");
        mPRXMap.put("en_SA","www.mea.philips.com");
        mPRXMap.put("ar_RW","www.philips.com.sa");
        mPRXMap.put("en_ZA","www.philips.co.za");
        mPRXMap.put("nl_BE","www.philips.be");
        mPRXMap.put("fr_BE","www.philips.be");
        mPRXMap.put("bg_BG","www.philips.bg");
        mPRXMap.put("cs_CZ","www.philips.cz");
        mPRXMap.put("da_DK","www.philips.dk");
        mPRXMap.put("de_DE","www.philips.de");
        mPRXMap.put("et_EE","www.philips.ee");
        mPRXMap.put("es_ES","www.philips.es");
        mPRXMap.put("el_GR","www.philips.gr");
        mPRXMap.put("fr_FR","www.philips.fr");
        mPRXMap.put("hr_HR","www.philips.hr");
        mPRXMap.put("en_IE","www.philips.ie");
        mPRXMap.put("it_IT","www.philips.it");
        mPRXMap.put("lv_LV","www.philips.lv");
        mPRXMap.put("lt_LT","www.philips.lt");
        mPRXMap.put("hu_HU","www.philips.hu");
        mPRXMap.put("nl_NL","www.philips.nl");
        mPRXMap.put("no_NO","www.philips.no");
        mPRXMap.put("de_AT","www.philps.at");
        mPRXMap.put("pl_PL","www.philips.pl");
        mPRXMap.put("pt_PT","www.philips.pt");
        mPRXMap.put("ro_RO","www.philips.ro");
        mPRXMap.put("ru_RU","www.philips.ru");
        mPRXMap.put("si_SI","www.philips.si");
        mPRXMap.put("sk_SK","www.philips.sk");
        mPRXMap.put("de_CH","www.philips.ch");
        mPRXMap.put("fr_CH","www.philips.ch");
        mPRXMap.put("fi_FI","www.philips.fi");
        mPRXMap.put("sv_SE","www.philips.se");
        mPRXMap.put("en_GB","www.philips.co.uk");
        mPRXMap.put("ru_UA","www.philips.ua");
        mPRXMap.put("en_AU","www.philips.com.au");
        mPRXMap.put("zh_CN","www.philips.com.cn");
        mPRXMap.put("en_HK","www.philips.com.hk");
        mPRXMap.put("zh_HK","www.philips.com.hk");
        mPRXMap.put("en_IN","www.philips.co.in");
        mPRXMap.put("en_ID","www.philips.co.id");
        mPRXMap.put("ja_JP","www.philips.co.jp");
        mPRXMap.put("ko_KR","www.philips.co.kr");
        mPRXMap.put("en_MY","www.philips.com.my");
        mPRXMap.put("en_NZ","www.philips.co.nz");
        mPRXMap.put("en_PK","www.philips.com.pk");
        mPRXMap.put("en_PH","www.philips.com.ph");
        mPRXMap.put("en_SG","www.philips.com.sg");
        mPRXMap.put("zh_TW","www.philips.com.tw");
        mPRXMap.put("th_TH","www.philips.co.th");
        mPRXMap.put("tr_TR","www.philips.com.tr");
        mPRXMap.put("vi_VN","www.philips.com.vn");
    }

    public static String getPRXUrl(String locale) {
        if (locale != null && mPRXMap.containsKey(locale)) {
            return mPRXMap.get(locale);
        }
        return "www.philips.com";
    }

}