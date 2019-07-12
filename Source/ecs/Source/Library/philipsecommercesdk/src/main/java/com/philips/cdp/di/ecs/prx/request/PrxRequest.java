package com.philips.cdp.di.ecs.prx.request;

import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.response.ResponseData;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the URL Builder base class to build all the PRX relevant URLs.
 * @since 1.0.0
 */
public abstract class PrxRequest {

    private PrxConstants.Sector mSector;
    private PrxConstants.Catalog mCatalog;
    private int maxRetries = 0;
    private int requestTimeOut = 15000;
    private String mCtn;
    private final String mServiceId;

    private List<String> mCtns;


    public PrxRequest(String ctn, String serviceId) {
        this.mCtn = ctn;
        this.mServiceId = serviceId;
    }


    public PrxRequest(String ctn, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        this.mCtn = ctn;
        this.mServiceId = serviceID;
        this.mSector = sector;
        this.mCatalog = catalog;
    }

    public PrxRequest(List<String> ctns, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        this.mCtns = ctns;
        this.mServiceId = serviceID;
        this.mSector = sector;
        this.mCatalog = catalog;
    }


    public String getCtn() {
        return this.mCtn;
    }


    public PrxConstants.Sector getSector() {
        return mSector;
    }

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



    public void getRequestUrlFromAppInfra(final OnUrlReceived listener) {
        Map<String, String> replaceUrl = new HashMap<>();
        replaceUrl.put("ctn", mCtn);
        replaceUrl.put("sector", getSector().toString());
        replaceUrl.put("catalog", getCatalog().toString());
        // replaceUrl.put("locale", locale);

        ArrayList<String> serviceIDList = new ArrayList<>();
        serviceIDList.add(mServiceId);
        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(serviceIDList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                listener.onSuccess(urlMap.get(mServiceId).getConfigUrls());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                listener.onError(error, message);
            }
        },replaceUrl);
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


    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }


    public int getRequestTimeOut() {
        return requestTimeOut;
    }

    public void setRequestTimeOut(final int requestTimeOut) {
        this.requestTimeOut = requestTimeOut;
    }
}
