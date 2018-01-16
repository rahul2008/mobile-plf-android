package com.philips.platform.uappframework.uappadaptor.listeners;

/**
 * Created by philips on 1/15/18.
 */

/**
 * callback class to handle user refresh
 * @since 2018.1.0
 */
public interface UserRefreshListener {
    /**
     * {@code onRefreshSuccess} method is invoked on successful user refresh
     * @since 2018.1.0
     */
    void onRefreshSuccess();

    /**
     * {@code errorMessage} method is invoked on user refresh Failure
     * @param errorMessage error message on failure
     * @since 2018.1.0
     */
    void onRefreshFailure(String errorMessage);
}

