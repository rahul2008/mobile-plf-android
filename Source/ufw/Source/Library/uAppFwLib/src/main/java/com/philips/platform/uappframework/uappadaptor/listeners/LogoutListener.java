package com.philips.platform.uappframework.uappadaptor.listeners;

/**
 * Created by philips on 1/15/18.
 */

/**
 * Call back class to handle logout
 * @since 2018.1.0
 */
public interface LogoutListener {
    /**
     * {@code onLogoutSuccess} method is invoked on Logout Success
     * @since 2018.1.0
     */
    void onLogoutSuccess();

    /**
     * {@code errorMessage} method is invoked on Logout Failure
     * @param errorMessage error message on failure
     * @since 2018.1.0
     */
    void onLogoutFailure(String errorMessage);

}
