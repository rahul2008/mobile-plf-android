package com.philips.cdp.prxclient.request;


import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.summary.PRXSummaryListResponse;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Product summary request.
 */
public class ProductSummaryListRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summarylist";
    private String mRequestTag = null;
    private List<String> ctns;


    /**
     * Instantiates a new Product summary request.
     *
     * @param ctn        product ctn
     * @param requestTag requestTag
     * @since 1.0.0
     */
    public ProductSummaryListRequest(String ctn, String requestTag) {
        super(ctn, PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    /**
     * Instantiates a new Product summary request.
     *
     * @param ctns       product ctns
     * @param sector     sector
     * @param catalog    catalog
     * @param requestTag request tag
     * @since 1.0.0
     */
    public ProductSummaryListRequest(List<String> ctns, PrxConstants.Sector sector,
                                     PrxConstants.Catalog catalog, String requestTag) {
        super(ctns, PRXSummaryDataServiceID, sector, catalog);
        this.ctns = ctns;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new PRXSummaryListResponse().parseJsonResponseData(jsonObject);
    }

    /**
     * Returns the base prx url from service discovery.
     *
     * @param appInfra AppInfra instance.
     * @param listener callback url received
     * @since 1.0.0
     */
    public void getRequestUrlFromAppInfra(final AppInfraInterface appInfra, final OnUrlReceived listener) {
        Map<String, String> replaceUrl = new HashMap<>();
        replaceUrl.put("ctns", getString(ctns));
        replaceUrl.put("sector", getSector().toString());
        replaceUrl.put("catalog", getCatalog().toString());
        // replaceUrl.put("locale", locale);
        appInfra.getServiceDiscovery().getServiceUrlWithCountryPreference(PRXSummaryDataServiceID,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {

                        appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, "prx SUCCESS Url " + url);
                        String urlHardCoded = "https://stg.philips.com/prx/product/B2C/en_US/CONSUMER/listproducts?ctnlist=S9721/84,FS9185/49";
                        listener.onSuccess(urlHardCoded);
                    }

                    @Override
                    public void onError(ERRORVALUES error, String message) {
                        appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG, PrxConstants.PRX_REQUEST_MANAGER, "prx ERRORVALUES " + message);
                        listener.onError(error, message);
                    }
                }, replaceUrl);
    }

    private String getString(List<String> ctns) {

        String ctnString = "";

        for (String ctn : ctns) {
            ctnString = ctnString + "," + ctn;
        }
        return ctnString;
    }
}
