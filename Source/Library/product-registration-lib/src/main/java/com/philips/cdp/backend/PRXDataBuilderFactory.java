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

    public PrxRequest createPRXBuilder(PRXRequestType prxRequestType, ProdRegRequestInfo prodRegRequestInfo, final String accessToken) {
        switch (prxRequestType.getValue()) {
            case 0:
                ProductMetaRequest productMetaRequest = new ProductMetaRequest(prodRegRequestInfo.getCtn());
                productMetaRequest.setSector(prodRegRequestInfo.getSector());
                productMetaRequest.setCatalog(prodRegRequestInfo.getCatalog());
                productMetaRequest.setmLocale(prodRegRequestInfo.getLocale());
                return productMetaRequest;
            case 1:
                RegistrationRequest registrationRequest = new RegistrationRequest(prodRegRequestInfo.getCtn(), prodRegRequestInfo.getSerialNumber(), accessToken);
                registrationRequest.setSector(prodRegRequestInfo.getSector());
                registrationRequest.setCatalog(prodRegRequestInfo.getCatalog());
                registrationRequest.setmLocale(prodRegRequestInfo.getLocale());
                return registrationRequest;

            case 2:
                RegisteredRequest registeredRequest = new RegisteredRequest(accessToken);
                registeredRequest.setSector(prodRegRequestInfo.getSector());
                registeredRequest.setCatalog(prodRegRequestInfo.getCatalog());
                registeredRequest.setmLocale(prodRegRequestInfo.getLocale());
                return registeredRequest;
            default:
                break;
        }
        return null;
    }
}
