package com.philips.multiproduct.listeners;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public interface ProductModelSelectionListener {

    void onProductModelSelected(final SummaryModel produSummaryModel);
}
