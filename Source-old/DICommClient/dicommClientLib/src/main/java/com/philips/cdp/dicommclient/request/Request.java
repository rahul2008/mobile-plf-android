/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import java.util.Map;

public abstract class Request {

    protected final Map<String, Object> mDataMap;
    protected final ResponseHandler mResponseHandler;

    public Request(Map<String, Object> dataMap, ResponseHandler responseHandler) {
        this.mDataMap = dataMap;
        this.mResponseHandler = responseHandler;
    }

    public abstract Response execute();
}
