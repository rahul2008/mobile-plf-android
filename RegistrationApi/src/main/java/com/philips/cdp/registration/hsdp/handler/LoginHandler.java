package com.philips.cdp.registration.hsdp.handler;

/**
 * Created by 310190722 on 9/15/2015.
 */
public interface LoginHandler {

    void onHsdpLoginSuccess();

    void onHsdpLoginFailure(int responseCode, String message);
}
