package com.philips.cdp.digitalcare.listeners;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 03-Dec-15.
 */
public interface prxSummaryCallback {

    void onResponseReceived(SummaryModel isAvailable);
}
