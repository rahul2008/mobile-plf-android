package com.philips.multiproduct.prx;

/**
 * Created by 310190678 on 01-Feb-16.
 */
public class PrxWrapper {

    private String TAG = PrxWrapper.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private Callback mListener = null;


    public PrxWrapper(String ctn, String sectorCode, String locale, String catalog) {

        this.mCtn = ctn;
        this.mSectorCode = sectorCode;
        this.mLocale = locale;
        this.mCatalogCode = catalog;
    }

    public void requestPrxSummaryData(Callback listener) {
        if (listener != null)
            mListener = listener;
        else
            throw new IllegalStateException("Callback listener is null");



    }


}
