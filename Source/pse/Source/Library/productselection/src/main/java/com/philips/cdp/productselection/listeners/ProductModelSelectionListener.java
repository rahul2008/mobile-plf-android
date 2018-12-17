package com.philips.cdp.productselection.listeners;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

/**
 * Created by naveen@philips.com on 12-Feb-16.
 */
public interface ProductModelSelectionListener {

    void onProductModelSelected(final SummaryModel produSummaryModel);
}
