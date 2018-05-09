package com.philips.platform.mya.mock;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;

public class RestInterfaceMock implements RestInterface {

    public boolean isInternetAvailable;

    @Override
    public RequestQueue getRequestQueue() {
        return null;
    }

    @Override
    public NetworkTypes getNetworkReachabilityStatus() {
        return null;
    }

    @Override
    public boolean isInternetReachable() {
        return isInternetAvailable;
    }
}
