/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;


import android.content.Context;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.providerslist.THSProviderInfo;

public interface THSProviderDetailsViewInterface {

    Context getContext();
    THSProviderInfo getTHSProviderInfo();
    Practice getPractice();
    PracticeInfo getPracticeInfo();
    Consumer getConsumerInfo();
    void updateView(Provider provider);
    void dismissRefreshLayout();
    String getFragmentTag();
    Provider getProvider();
    void updateEstimatedCost(EstimatedVisitCost estimatedVisitCost);
    void onCalenderItemClick(int position);
    String getReminderTime();
}
