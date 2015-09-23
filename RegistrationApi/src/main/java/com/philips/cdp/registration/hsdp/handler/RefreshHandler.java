package com.philips.cdp.registration.hsdp.handler;

/**
 * Created by 310190722 on 9/15/2015.
 */
public interface RefreshHandler {

    void onHsdpRefreshSuccess();

    void onHsdpRefreshFailure(int responseCode, String message);

}
