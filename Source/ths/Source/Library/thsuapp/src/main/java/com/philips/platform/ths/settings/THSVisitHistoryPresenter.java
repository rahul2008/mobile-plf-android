/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_VISIT_HISTORY;

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
                mThsVisitHistoryFragment.showError( THSSDKErrorFactory.getErrorType(mThsVisitHistoryFragment.getContext(), ANALYTIC_FETCH_VISIT_HISTORY,sdkError), true, false);
                return;
            }
            mThsVisitHistoryFragment.updateVisitHistoryView(visitReports);
            mThsVisitHistoryFragment.stopRefreshing();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(null!= mThsVisitHistoryFragment && mThsVisitHistoryFragment.isFragmentAttached()) {
            mThsVisitHistoryFragment.doTagging(ANALYTIC_FETCH_VISIT_HISTORY,throwable.getMessage(),true);
            mThsVisitHistoryFragment.showError(mThsVisitHistoryFragment.getString(R.string.ths_se_server_error_toast_message), true, false);
        }
    }
}

