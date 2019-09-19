package com.philips.cdp.di.ecs.integration;

import com.philips.cdp.di.ecs.error.ECSError;

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
     * @param error     the error object
     * @param ecsError the error code
     */
    public void onFailure(E error, ECSError ecsError);
}
