package com.philips.cdp.registration.listener;

public interface HSDPAuthenticationListener {

    /**
     * Callback when HSDP login success happened
     *
     * @since 1804.0
     */
    void onHSDPLoginSuccess();

    /**
     * Callback when HSDP failure success happened
     *
     * @since 1804.0
     */
    void onHSDPLoginFailure(int errorCode, String msg);
}
