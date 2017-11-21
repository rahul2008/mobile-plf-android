/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

/**
 * Abstraction for returning results of a request.
 * @publicApi
 */
public interface ResponseHandler {

    /**
     * Called when a request has successfully returned.
     * @param data String The data contained in the response
     */
    void onSuccess(String data);

    /**
     * Called when a Request encountered a problem when performing a request.
     * @param error Error
     * @param errorData String
     */
    void onError(Error error, String errorData);
}
