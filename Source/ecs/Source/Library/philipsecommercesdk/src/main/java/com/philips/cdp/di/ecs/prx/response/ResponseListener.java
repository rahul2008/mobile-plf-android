package com.philips.cdp.di.ecs.prx.response;


import com.philips.cdp.di.ecs.prx.error.PrxError;

/**
 * The Listener interface used by the vertical applications for getting the product,locale,category specific data from the Philips IT system.
 * It is an interface which has two methods. On successful response we return onResponseSuccess (ResponseData data) and on any error we return onResponseError (Error error)
 * @since 1.0.0
 */
public interface ResponseListener {

    /**
     * Gets returned on success.
     * @param responseData The Response data
     * @since 1.0.0
     */
    void onResponseSuccess(ResponseData responseData);

    /**
     * Gets returned on error.
     * @param prxError PRX error
     * @since 1.0.0
     */
    void onResponseError(PrxError prxError);
}
