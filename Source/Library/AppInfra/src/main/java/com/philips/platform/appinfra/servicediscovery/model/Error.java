package com.philips.platform.appinfra.servicediscovery.model;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

/**
 * Created by 310238114 on 6/15/2016.
 */
public class Error {
    String message ;
    String payload;
    String httpStatus;
    ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues= null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES getErrorvalues() {
        return errorvalues;
    }

    public void setErrorvalues(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues) {
        this.errorvalues = errorvalues;
    }

}
