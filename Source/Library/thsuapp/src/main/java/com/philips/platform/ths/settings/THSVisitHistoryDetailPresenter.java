/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public class THSVisitHistoryDetailPresenter implements THSBasePresenter, THSVisitReportAttachmentCallback<FileAttachment,SDKError> {

    THSVisitHistoryDetailFragment mThsVisitHistoryDetailFragment;

    public THSVisitHistoryDetailPresenter(THSVisitHistoryDetailFragment thsVisitHistoryDetailFragment) {
        mThsVisitHistoryDetailFragment = thsVisitHistoryDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    public void downloadReport() {
        try {
            THSManager.getInstance().getVisitReportAttachment(mThsVisitHistoryDetailFragment.getConsumer(), mThsVisitHistoryDetailFragment.getVisitReport(), this, mThsVisitHistoryDetailFragment.getContext()
            );
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(FileAttachment fileAttachment, SDKError sdkError) {
        if (sdkError != null) {
            mThsVisitHistoryDetailFragment.showToast(sdkError.getSDKErrorReason().name());
            return;
        }
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, "specialEvents","reportDownloaded");
        THSFileUtils fileUtils = new THSFileUtils();

        fileUtils.openAttachment(mThsVisitHistoryDetailFragment.getContext(), fileAttachment);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsVisitHistoryDetailFragment.showToast(throwable.getMessage());
    }

    public void getVisitReportDetail(VisitReport visitReport){

        try {
            THSManager.getInstance().getVisitReportDetail(mThsVisitHistoryDetailFragment.getConsumer(), visitReport, new THSVisitReportDetailCallback<VisitReportDetail, SDKError>() {
                @Override
                public void onResponse(VisitReportDetail visitReportDetail, SDKError sdkError) {
                    mThsVisitHistoryDetailFragment.hideProgressBar();
                    mThsVisitHistoryDetailFragment.updateView(visitReportDetail);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    mThsVisitHistoryDetailFragment.hideProgressBar();
                    mThsVisitHistoryDetailFragment.showToast("Failed to get the details");
                }
            }, mThsVisitHistoryDetailFragment.getContext());
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }


    }
}
