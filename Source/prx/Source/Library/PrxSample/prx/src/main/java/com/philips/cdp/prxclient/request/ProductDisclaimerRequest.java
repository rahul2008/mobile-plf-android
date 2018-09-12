package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.Disclaimer.DisclaimerModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

public class ProductDisclaimerRequest extends PrxRequest {

    private static final String PRXDisclaimerDataServiceID = "prxclient.disclaimers";
    private String mRequestTag = null;

    public ProductDisclaimerRequest(String ctn, String serviceId) {
        super(ctn, PRXDisclaimerDataServiceID);
        this.mRequestTag = serviceId;
    }


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
