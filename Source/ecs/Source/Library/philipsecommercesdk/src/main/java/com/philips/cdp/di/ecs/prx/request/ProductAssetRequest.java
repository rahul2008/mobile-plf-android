package com.philips.cdp.di.ecs.prx.request;

import com.philips.cdp.di.ecs.prx.assets.AssetModel;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.response.ResponseData;

import org.json.JSONObject;


/**
 * The type Product asset request.
 */
public class ProductAssetRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    /**
     * Instantiates a new Product asset request.
     * @since  1.0.0
     * @param ctn        product ctn
     * @param requestTag requestTag
     */
    public ProductAssetRequest(String ctn, String requestTag) {
        super(ctn, PRXAssetAssetServiceID);
        this.mRequestTag = requestTag;
    }

    /**
     * Instantiates a new Product asset request.
     * @since 1.0.0
     * @param ctn         product ctn
     * @param sector      sector
     * @param catalog     catalog
     * @param requestTag  request tag
     */
    public ProductAssetRequest(String ctn, PrxConstants.Sector sector, PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXAssetAssetServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

}
