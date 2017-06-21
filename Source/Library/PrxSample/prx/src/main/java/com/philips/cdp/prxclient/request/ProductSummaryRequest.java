package com.philips.cdp.prxclient.request;


import android.util.Log;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ProductSummaryRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summary";
    private String mRequestTag = null;


    public ProductSummaryRequest(String ctn, String requestTag) {
        super(ctn, PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    public ProductSummaryRequest(String ctn, PrxConstants.Sector sector,
                                 PrxConstants.Catalog catalog, String requestTag) {
        super(ctn, PRXSummaryDataServiceID, sector, catalog);
        Log.i(PrxConstants.PRX_REQUEST_MANAGER, "Product Summary Request");
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        Log.i(PrxConstants.PRX_REQUEST_MANAGER, "Product Summary get Response Data ");
        return new SummaryModel().parseJsonResponseData(jsonObject);
    }
}
