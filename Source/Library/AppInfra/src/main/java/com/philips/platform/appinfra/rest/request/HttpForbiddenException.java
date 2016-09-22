/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest.request;

public class HttpForbiddenException extends Exception{

    private final String message = "http calls are depricated use https calls only";
    public HttpForbiddenException() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
