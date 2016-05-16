/**
 * @author  naveen@philips.com on 12-Apr-16.
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.listeners;


import com.philips.cdp.prxclient.datamodels.support.SupportModel;


public interface PrxFaqCallback {

    void onResponseReceived(SupportModel isAvailable);
}
