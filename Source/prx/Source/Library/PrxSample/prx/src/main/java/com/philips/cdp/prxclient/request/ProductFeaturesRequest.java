package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * The type Product disclaimer request.
 */
public class ProductFeaturesRequest extends PrxRequest {

    private static final String PRXDisclaimerDataServiceID = "prxclient.features";
    private String mRequestTag = null;


    /**
     * Instantiates a new Product disclaimer request.
     * @since 1805
     * @param ctn         product ctn
     * @param requestTag  requestTag
     */
    public ProductFeaturesRequest(String ctn, String requestTag) {
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
    public ProductFeaturesRequest(String ctn, PrxConstants.Sector sector,
                                  PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXDisclaimerDataServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new FeaturesModel().parseJsonResponseData(jsonObject);
    }
}
