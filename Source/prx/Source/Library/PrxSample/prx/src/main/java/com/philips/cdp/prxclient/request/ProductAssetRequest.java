package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;


public class ProductAssetRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    public ProductAssetRequest(String ctn, String requestTag) {
        super(ctn, PRXAssetAssetServiceID);
        this.mRequestTag = requestTag;
    }

    public ProductAssetRequest(String ctn, PrxConstants.Sector sector, PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXAssetAssetServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

}
