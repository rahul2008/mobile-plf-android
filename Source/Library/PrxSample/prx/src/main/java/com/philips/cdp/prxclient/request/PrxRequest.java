package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
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

    private PrxConstants.Sector mSector;
    private PrxConstants.Catalog mCatalog;
    private int maxRetries = 0;
    private int requestTimeOut = 5000;
    private final String mCtn;
    private final String mServiceId;

    /**
     * @param ctn ctn of the product.
     * @param serviceId PRX ServiceId.
     */
    public PrxRequest(String ctn, String serviceId) {
        this.mCtn = ctn;
        this.mServiceId = serviceId;
    }

    /**
     * @param ctn ctn of the product.
     * @param serviceID PRX ServiceId.
     * @param sector sector.
     * @param catalog catalog.
     */
    public PrxRequest(String ctn, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        this.mCtn = ctn;
        this.mServiceId = serviceID;
        this.mSector = sector;
        this.mCatalog = catalog;
    }

    /**
     * @return returns the ctn.
     */
    public String getCtn() {
        return this.mCtn;
    }

    /**
     * @return returns the sector.
     */
    public PrxConstants.Sector getSector() {
        return mSector;
    }

    /**
     * @param mSector
     */
    public void setSector(final PrxConstants.Sector mSector) {
        this.mSector = mSector;
    }

    public PrxConstants.Catalog getCatalog() {
        return mCatalog;
    }

    public void setCatalog(PrxConstants.Catalog catalog) {
        this.mCatalog = catalog;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);


    /**
     * Returns the base prx url from service discovery.
     * @param appInfra appinfra instance.
     * @param listener callback urlreceived
     */
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
                        appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, "prx SUCCESS Url "+url);
                        listener.onSuccess(url.toString());
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, "prx ERRORVALUES "+ message);
                        listener.onError(error, message);
                    }
                }, replaceUrl);
    }


    /**
     * Interface which gives callback onUrlReceieved.
     */
    public interface OnUrlReceived extends ServiceDiscoveryInterface.OnErrorListener {
        void onSuccess(String url);
    }

    /**
     * returns request type
     * @return request type for ex . GET/POST/PUT.
     */
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    /**
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return null;
    }

    /**
     *
     * @return params
     */
    public Map<String, String> getParams() {
        return null;
    }

    /**
     * Get Max num of retries.
     *
     * @return Max num of retries
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * @param maxRetries - Set maximum number of retries when request failed
     */
    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * get request time out in milli seconds
     *
     * @return timeout.
     */
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
