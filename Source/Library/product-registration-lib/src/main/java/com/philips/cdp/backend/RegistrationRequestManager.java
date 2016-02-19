package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.productbuilder.RegistrationDataBuilder;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationRequestManager {

    private Context mContext = null;

    public RegistrationRequestManager(final Context mContext) {
        this.mContext = mContext;
    }

    public void executeRequest(RegistrationDataBuilder prxDataBuilder, ResponseListener responseListener) {
        final ProcessNetwork processNetwork = new ProcessNetwork(mContext);
        processNetwork.executeRequest(prxDataBuilder, responseListener);
    }

    public void cancelRequest(String requestTag) {
    }
}
