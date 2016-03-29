package com.philips.cdp.backend;

import com.philips.cdp.handler.PRXRequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prxrequest.RegisteredRequest;
import com.philips.cdp.prxrequest.RegistrationRequest;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRXDataBuilderFactory {

    public PrxRequest createPRXBuilder(PRXRequestType prxRequestType, ProdRegRequestInfo prodRegRequestInfo, final String accessToken) {
        switch (prxRequestType.getValue()) {
            case 0:
                ProductMetadataRequest productMetadataRequest = new ProductMetadataRequest(prodRegRequestInfo.getCtn());
                productMetadataRequest.setSector(prodRegRequestInfo.getSector());
                productMetadataRequest.setCatalog(prodRegRequestInfo.getCatalog());
                productMetadataRequest.setmLocale(prodRegRequestInfo.getLocale());
                return productMetadataRequest;
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
