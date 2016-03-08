package com.philips.cdp.backend;

import com.philips.cdp.productrequest.ProductMetaRequest;
import com.philips.cdp.productrequest.RegisteredRequest;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRXDataBuilderFactory {

    public PrxRequest createPRXBuilder(PRXRequestType prxRequestType, ProdRegRequestInfo prodRegRequestInfo) {
        switch (prxRequestType.getValue()) {
            case 0:
                ProductMetaRequest productMetaRequest = new ProductMetaRequest(prodRegRequestInfo.getCtn(), prodRegRequestInfo.getAccessToken());
                productMetaRequest.setSector(prodRegRequestInfo.getSector());
                productMetaRequest.setCatalog(prodRegRequestInfo.getCatalog());
                productMetaRequest.setmLocale(prodRegRequestInfo.getLocale());
                return productMetaRequest;
            case 1:
                RegistrationRequest registrationRequest = new RegistrationRequest(prodRegRequestInfo.getCtn(), prodRegRequestInfo.getAccessToken(), prodRegRequestInfo.getSerialNumber());
                registrationRequest.setSector(prodRegRequestInfo.getSector());
                registrationRequest.setCatalog(prodRegRequestInfo.getCatalog());
                registrationRequest.setmLocale(prodRegRequestInfo.getLocale());
                return registrationRequest;

            case 2:
                RegisteredRequest registeredRequest = new RegisteredRequest(prodRegRequestInfo.getAccessToken());
                registeredRequest.setSector(prodRegRequestInfo.getSector());
                registeredRequest.setCatalog(prodRegRequestInfo.getCatalog());
                registeredRequest.setmLocale("en_GB");
                return registeredRequest;
            default:
                break;
        }
        return null;
    }
}
