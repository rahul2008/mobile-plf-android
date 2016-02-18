package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationRequestManager {

    private Context mContext = null;
    private String mServicetag = null;

    public RegistrationRequestManager(final Context mContext, String mServicetag) {
        this.mContext = mContext;
        this.mServicetag = mServicetag;
    }

    public void executeRequest(PrxDataBuilder prxDataBuilder, ResponseListener responseListener) {
        final ProcessNetwork processNetwork = new ProcessNetwork(mContext);
        processNetwork.setHttpsRequest(true);
        if (mServicetag.equalsIgnoreCase("REGISTRATION")) {
            processNetwork.productRegistrationRequest(prxDataBuilder, responseListener);
        } else if (mServicetag.equalsIgnoreCase("METADATA")) {
            processNetwork.productMetaDataRequest(prxDataBuilder, responseListener);
        } else if (mServicetag.equalsIgnoreCase("REGISTERED")) {
            processNetwork.registredDataRequest(prxDataBuilder, responseListener);
        }
    }

    public void cancelRequest(String requestTag) {
    }
}
