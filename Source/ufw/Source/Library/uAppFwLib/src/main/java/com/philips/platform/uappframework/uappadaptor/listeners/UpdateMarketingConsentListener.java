package com.philips.platform.uappframework.uappadaptor.listeners;

/**
 * Created by philips on 1/15/18.
 */

/**
 * Callback class to handle update marketing consent
 * @since 2018.1.0
 */
public interface UpdateMarketingConsentListener {
    /**
     * {@code onUpdateSuccess} method is invoked on Logout Success
     * @since 2018.1.0
     */
    void onUpdateSuccess();
    /**
     * {@code errorMessage} method is invoked on update Failure
     * @param errorMessage error message on failure
     * @since 2018.1.0
     */
    void onUpdateFailure(String errorMessage);
}
