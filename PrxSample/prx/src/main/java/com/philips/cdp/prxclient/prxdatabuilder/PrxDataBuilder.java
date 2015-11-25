package com.philips.cdp.prxclient.prxdatabuilder;

import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Description : This is the URL Builder base class to build all the PRX relevent URL's.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public abstract class PrxDataBuilder {

    private String PRX_BASE_URL = "www.philips.com/prx";
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;

    public String getPRXBaseUrl() {
        return PRX_BASE_URL;
    }

    public String getSectorCode() {
        return mSectorCode;
    }

    public void setmSectorCode(String mSectorCode) {
        this.mSectorCode = mSectorCode;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public String getCatalogCode() {
        return mCatalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.mCatalogCode = catalogCode;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract String getRequestUrl();
}
