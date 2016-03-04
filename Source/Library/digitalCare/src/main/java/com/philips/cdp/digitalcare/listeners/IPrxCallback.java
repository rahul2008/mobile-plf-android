package com.philips.cdp.digitalcare.listeners;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 03-Dec-15.
 */
public interface IPrxCallback {

    void onResponseReceived(SummaryModel isAvailable);
}
