package com.philips.cdp.digitalcare.listeners;


import com.philips.cdp.prxclient.datamodels.support.SupportModel;

/**
 * Created by naveen@philips.com on 12-Apr-16.
 */
public interface PrxFaqCallback {

    void onResponseReceived(SupportModel isAvailable);
}
