package com.philips.cdp.registration.handlers;

public interface LogoutHandler {

    void onLogoutSuccess();

    void onLogoutFailure(int responseCode, String message);
}
