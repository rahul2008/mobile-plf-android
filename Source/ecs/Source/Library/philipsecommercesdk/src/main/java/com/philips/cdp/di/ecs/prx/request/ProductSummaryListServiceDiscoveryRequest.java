package com.philips.cdp.di.ecs.prx.request;


import android.text.TextUtils;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.response.ResponseData;
import com.philips.cdp.di.ecs.prx.summary.ECSProductSummary;
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
 * The type Product summary request.
 */
public class ProductSummaryListServiceDiscoveryRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summarylist";
    private String mRequestTag = null;
    private List<String> ctns;

    public ProductSummaryListServiceDiscoveryRequest(List<String> ctns, PrxConstants.Sector sector,
                                                     PrxConstants.Catalog catalog, String requestTag) {
        super(ctns, PRXSummaryDataServiceID, sector, catalog);
        this.ctns = ctns;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new ECSProductSummary().parseJsonResponseData(jsonObject);
    }

    public void getRequestUrlFromAppInfra(final OnUrlReceived listener) {
        Map<String, String> replaceUrl = new HashMap<>();
        replaceUrl.put("ctns", getString(ctns));
        replaceUrl.put("sector", getSector().toString());
        replaceUrl.put("catalog", getCatalog().toString());

        ArrayList<String> serviceIDList = new ArrayList<>();
        serviceIDList.add(PRXSummaryDataServiceID);
        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(serviceIDList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                listener.onSuccess(urlMap.get(PRXSummaryDataServiceID).getConfigUrls());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                listener.onError(error, message);
            }
        }, replaceUrl);
    }

    private String getString(List<String> ctns) {
        return TextUtils.join(",", ctns);
    }
}
