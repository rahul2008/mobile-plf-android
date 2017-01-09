package com.philips.cdp.prxclient.request;


import android.util.Log;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
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
public class ProductSummaryRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summary";
    private String mRequestTag = null;


    public ProductSummaryRequest(String ctn, String requestTag) {
        super.initCtn(ctn,PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {

        return new SummaryModel().parseJsonResponseData(jsonObject);
    }

}
