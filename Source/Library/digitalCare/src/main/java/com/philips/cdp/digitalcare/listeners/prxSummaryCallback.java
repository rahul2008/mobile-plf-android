/**
 * Description : Summary Callback listener for the Summary PRX Data.
 * Project : PRX Common Component.
 * @author  naveen@philips.com on 03-Dec-15.
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.listeners;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;


public interface prxSummaryCallback {

    void onResponseReceived(SummaryModel isAvailable);
}
