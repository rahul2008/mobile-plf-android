package com.philips.cdp.di.ecs.integration;

/**
 * The interface Iapsdk callback.
 */
public interface ECSCallback<R,E> {
    /**
     * On response.
     *
     * @param result the result
     */
    public void onResponse(R result);

    /**
     * On failure.
     *
     * @param error     the error object
     * @param errorCode the error code
     */
    public void onFailure(E error, String detailErrorMessage, int errorCode);
}
