package com.philips.cdp.prxclient.request;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description : This is the URL Builder base class to build all the PRX relevent URL's.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public abstract class PrxRequest {

    private Sector mSector;
    private Catalog mCatalog;
    private int maxRetries = 0;
    private int requestTimeOut = 5000;
    private String mCtn;
    private String mServiceId;

    public void init(String ctn, String serviceID, Sector sector, Catalog catalog) {
        this.mCtn = ctn;
        this.mServiceId = serviceID;
        this.mSector = sector;
        this.mCatalog = catalog;
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

    public void getRequestUrlFromAppInfra(final AppInfraInterface appInfra, final OnUrlReceived listener) {
        Map<String, String> replaceUrl = new HashMap<>();
        replaceUrl.put("ctn", mCtn);
        replaceUrl.put("sector", getSector().toString());
        replaceUrl.put("catalog", getCatalog().toString());
        // replaceUrl.put("locale", locale);
        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(mServiceId,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        PrxLogger.i("SUCCESS ***", "" + url);
                        listener.onSuccess(url.toString());
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        PrxLogger.i("ERRORVALUES ***", "" + message);
                        listener.onError(error, message);
                    }
                }, replaceUrl);
    }


    public interface OnUrlReceived extends ServiceDiscoveryInterface.OnErrorListener {
        void onSuccess(String url);

    }


    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    public Map<String, String> getHeaders() {
        return null;
    }

    public Map<String, String> getParams() {
        return null;
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
