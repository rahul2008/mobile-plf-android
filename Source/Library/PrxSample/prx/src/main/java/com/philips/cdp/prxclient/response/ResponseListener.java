package com.philips.cdp.prxclient.response;

import com.philips.cdp.prxclient.error.PrxError;

/**
 * Description : The Listner interface used by the vertical applications for getting the product,locale,category specific data from
 * the Philips IT system.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public interface ResponseListener {

    void onResponseSuccess(ResponseData responseData);

    void onResponseError(PrxError prxError);
}
