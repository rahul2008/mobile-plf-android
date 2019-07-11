package com.philips.cdp.di.ecs.prx.request;


import com.philips.cdp.di.ecs.prx.Disclaimer.DisclaimerModel;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.response.ResponseData;

import org.json.JSONObject;

/**
 * The type Product disclaimer request.
 */
public class ProductDisclaimerRequest extends PrxRequest {

    private static final String PRXDisclaimerDataServiceID = "prxclient.disclaimers";
    private String mRequestTag = null;


    /**
     * Instantiates a new Product disclaimer request.
     * @since 1805
     * @param ctn         product ctn
     * @param requestTag  requestTag
     */
    public ProductDisclaimerRequest(String ctn, String requestTag) {
        super(ctn, PRXDisclaimerDataServiceID);
        this.mRequestTag = requestTag;
    }


    /**
     * Instantiates a new Product disclaimer request.
     * @since 1805
     * @param ctn         product ctn
     * @param sector      sector
     * @param catalog     catalog
     * @param requestTag  request tag
     */
    public ProductDisclaimerRequest(String ctn, PrxConstants.Sector sector,
                                    PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXDisclaimerDataServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new DisclaimerModel().parseJsonResponseData(jsonObject);
    }
}
