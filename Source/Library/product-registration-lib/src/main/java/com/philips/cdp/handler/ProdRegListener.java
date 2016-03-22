package com.philips.cdp.handler;

import com.philips.cdp.error.ErrorType;
import com.philips.cdp.prxclient.response.ResponseData;

/**
 * Description : The Listner interface used by the vertical applications for getting the product,locale,category specific data from
 * the Philips IT system.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public interface ProdRegListener {

    void onProdRegSuccess(ResponseData responseData);

    void onProdRegFailed(ErrorType errorType);
}
