package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Product Support Request class
 *
 * @since 1.0.0
 */
public class ProductSupportRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXFAQServiceID = "prxclient.support";

    /**
     * Instantiates a new Product support request.
     * @since 1.0.0
     * @param ctn        the ctn
     * @param requestTag requestTag
     */
    public ProductSupportRequest(String ctn, String requestTag) {
        super(ctn, PRXFAQServiceID);
        this.mRequestTag = requestTag;
    }

    /**
     * Instantiates a new Product support request.
     * @since 1.0.0
     * @param ctn         product ctn
     * @param sector      sector
     * @param catalog     catalog
     * @param requestTag  request tag
     */
    public ProductSupportRequest(String ctn, PrxConstants.Sector sector,
                                 PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXFAQServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new SupportModel().parseJsonResponseData(jsonObject);
    }
}
