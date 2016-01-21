package com.philips.cdp.prxclient.response;

import android.util.Log;

import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This class provides the URL
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */

public abstract class ResponseData {
    public abstract ResponseData parseJsonResponseData(JSONObject response);
}
