package com.philips.cdp.productselection.prx;

import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.List;

/**
 * Created by naveen@philips.com on 25-Feb-16.
 */
public interface SummaryDataListener {

    public void onSuccess(List<SummaryModel> summaryModels);

}
