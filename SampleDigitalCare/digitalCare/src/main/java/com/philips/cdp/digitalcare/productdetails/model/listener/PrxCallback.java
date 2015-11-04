package com.philips.cdp.digitalcare.productdetails.model.listener;

import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 04-Nov-15.
 */
public interface PrxCallback {

    abstract void onSummaryDataReceived(ViewProductDetailsModel object);

    void onAssetDataReceived(ViewProductDetailsModel object);
}
