package com.philips.cdp.handler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.backend.PRXDataBuilderFactory;
import com.philips.cdp.backend.PRXRequestType;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProduct {

    public void getRegisteredProducts(final Context context, final ProdRegRequestInfo prodRegRequestInfo, ResponseListener listener) {
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.FETCH_PRODUCTS, prodRegRequestInfo);
        RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(prxRequest, listener);
    }

    @NonNull
    private RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }

    public void registerProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, ResponseListener listener) {
        final PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        final PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.REGISTRATION, prodRegRequestInfo);
        RegistrationRequest registrationRequest = (RegistrationRequest) prxRequest;
        registrationRequest.setRegistrationChannel(prodRegRequestInfo.getRegistrationChannel());
        registrationRequest.setmLocale(prodRegRequestInfo.getLocale());
        registrationRequest.setPurchaseDate(prodRegRequestInfo.getPurchaseDate());
        registrationRequest.setProductSerialNumber(prodRegRequestInfo.getSerialNumber());
        RequestManager mRequestManager = getRequestManager(context);
        mRequestManager.executeRequest(registrationRequest, listener);
    }
}
