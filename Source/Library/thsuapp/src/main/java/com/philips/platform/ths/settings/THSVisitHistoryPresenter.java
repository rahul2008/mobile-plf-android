/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

public class THSVisitHistoryPresenter implements THSBasePresenter, THSVisitReportListCallback<List<VisitReport>,SDKError>{

    THSVisitHistoryFragment mThsVisitHistoryFragment;

    public THSVisitHistoryPresenter(THSVisitHistoryFragment thsVisitHistoryFragmnent) {
        mThsVisitHistoryFragment = thsVisitHistoryFragmnent;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void getVisitHistory(){
        try {
            THSManager.getInstance().getVisitHistory(mThsVisitHistoryFragment.getContext(),null, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(List<VisitReport> visitReports, SDKError sdkError) {
        if(null!= mThsVisitHistoryFragment && mThsVisitHistoryFragment.isFragmentAttached()) {
            if (sdkError != null) {
                mThsVisitHistoryFragment.showToast(sdkError.getSDKErrorReason().name());
                return;
            }
            mThsVisitHistoryFragment.updateVisitHistoryView(visitReports);
            mThsVisitHistoryFragment.hideProgressBar();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}

