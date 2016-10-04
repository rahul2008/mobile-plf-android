/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery.model;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class ServiceDiscovery {


    boolean success = false;
    String httpStatus;
    String country;
    MatchByCountryOrLanguage matchByCountry;
    MatchByCountryOrLanguage matchByLanguage;


    Error error = null;

    public static class Error {
        private String message ;
        private ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalue = null;

        public Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES e, String m) {
            errorvalue = e;
            message = m;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES getErrorvalue() {
            return errorvalue;
        }
        public void setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues) {
            this.errorvalue = errorvalues;
        }
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }


    public MatchByCountryOrLanguage getMatchByCountry() {
        return matchByCountry;
    }

    public void setMatchByCountry(MatchByCountryOrLanguage matchByCountry) {
        this.matchByCountry = matchByCountry;
    }


    public MatchByCountryOrLanguage getMatchByLanguage() {
        return matchByLanguage;
    }

    public void setMatchByLanguage(MatchByCountryOrLanguage matchByLanguage) {
        this.matchByLanguage = matchByLanguage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
