package com.philips.cdp.prxclient.prxdatabuilder;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Description : This is the URL Builder base class to build all the PRX relevent URL's.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public abstract class PrxDataBuilder {

    private String mServerInfo = "www.philips.com/prx";
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;
    private Sector sector;
    private Catalog catalog;

    public String getServerInfo() {
        return mServerInfo;
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

    public void setmLocale(String mLocale) {
        this.mLocale = mLocale;
    }

    public String getCatalogCode() {
        return mCatalogCode;
    }

    public void setmCatalogCode(String mCatalogCode) {
        this.mCatalogCode = mCatalogCode;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract String getRequestUrl();

    public Sector getSector() {
        return sector;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
