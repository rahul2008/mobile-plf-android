package com.philips.cdp.prxclient;

import android.content.Context;

import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;

import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This is the entry class to start the PRX Request.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class RequestManager {

    private final String VERSION = "1.0.0";

    private Context mContext = null;
    //private ResponseHandler mResponseHandler = null;

    public void init(Context applicationContext) {
        mContext = applicationContext;
    }

    public void executeRequest(PrxDataBuilder prxDataBuilder, ResponseListener responseListener) {
        new NetworkWrapper(mContext).executeJsonObjectRequest(prxDataBuilder, responseListener);
    }

    public void cancelRequest(String requestTag) {
    }


    public String getLibVersion() {
        return VERSION;
    }
}
