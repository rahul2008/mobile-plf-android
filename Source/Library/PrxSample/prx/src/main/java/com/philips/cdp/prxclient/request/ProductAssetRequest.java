package com.philips.cdp.prxclient.request;

import android.util.Log;

import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductAssetRequest extends PrxRequest {

    private String mRequestTag = null;
    private static final String PRXAssetAssetServiceID = "prxclient.assets";

    public ProductAssetRequest(String ctn, String requestTag) {
        super.initCtn(ctn, PRXAssetAssetServiceID);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new AssetModel().parseJsonResponseData(jsonObject);
    }

}
