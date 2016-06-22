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
    private Sector mSector;
    private Catalog mCatalog;
    private String mLocaleMatchResult;
    private int maxRetries = 0;
    private int requestTimeOut = 5000;

    public String getServerInfo() {
        return mServerInfo;
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

    public abstract Map<String, String> getParams();

    public String getLocaleMatchResult() {
        return mLocaleMatchResult;
    }

    public void setLocaleMatchResult(String mLocaleMatchResult) {
        this.mLocaleMatchResult = mLocaleMatchResult;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * @param maxRetries - Set maximum number of retries when request failed
     */
    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getRequestTimeOut() {
        return requestTimeOut;
    }

    /**
     * @param requestTimeOut - Set request time out in milli seconds
     */
    public void setRequestTimeOut(final int requestTimeOut) {
        this.requestTimeOut = requestTimeOut;
    }
}
