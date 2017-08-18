/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

/**
 * @publicApi
 */
public interface ResponseHandler {

    void onSuccess(String data);

    void onError(Error error, String errorData);
}
