package com.philips.cdp.registration.hsdp.handler;

/**
 * Created by 310190722 on 9/15/2015.
 */
public interface LogoutHandler {

    void onHsdpLogoutSuccess();

    void onHsdpLogoutFailure(int responseCode, String message);
}
