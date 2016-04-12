package com.philips.cdp.prxclient.request;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * Description : This is the URL Builder base class to build all the PRX relevent URL's.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public abstract class PrxRequest {

    private String mServerInfo = "www.philips.com/prx";
    private String mLocale = null;
    private Sector mSector;
    private Catalog mCatalog;
    private String mLocaleMatchResult;

    protected String getLocaleMatchResult() {
        if (mLocaleMatchResult != null)
            return mLocaleMatchResult;
        else
            return getLocale();
    }

    public void setLocaleMatchResult(final String mLocaleMatchResult) {
        this.mLocaleMatchResult = mLocaleMatchResult;
    }

    public String getServerInfo() {
        return mServerInfo;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String mLocale) {
        this.mLocale = mLocale;
    }

    public Sector getSector() {
        return mSector;
    }

    public void setSector(final Sector mSector) {
        this.mSector = mSector;
    }

    public Catalog getCatalog() {
        return mCatalog;
    }

    public void setCatalog(Catalog catalog) {
        this.mCatalog = catalog;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract String getRequestUrl();

    public abstract int getRequestType();

    public abstract Map<String, String> getHeaders();

    public abstract Map<String, String> getParams();}
