package com.philips.cdp.registration.handlers;

/**
 * Created by 310190722 on 9/15/2015.
 */
public interface LogoutHandler {

    void onLogoutSuccess();

    void onLogoutFailure(int responseCode, String message);
}
