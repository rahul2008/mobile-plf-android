package com.philips.cdp.prxclient.request;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductAssetRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    public ProductAssetRequest(String ctn, Sector sector, Catalog catalog, String requestTag) {
        super.init(ctn, PRXAssetAssetServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

}
