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
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.net.URL;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITIONS;

public class THSVisitHistoryDetailPresenter implements THSBasePresenter, THSVisitReportAttachmentCallback<FileAttachment,SDKError> {

    THSVisitHistoryDetailFragment mThsVisitHistoryDetailFragment;

    public THSVisitHistoryDetailPresenter(THSVisitHistoryDetailFragment thsVisitHistoryDetailFragment) {
        mThsVisitHistoryDetailFragment = thsVisitHistoryDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_download_report_hippa_notice_link){
            getHIPPANotice();
        }
    }

    private void getHIPPANotice() {
        THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THSConstants.THS_HIPPA_NOTICE, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                mThsVisitHistoryDetailFragment.showError("Service discovery failed - >" + s);
                mThsVisitHistoryDetailFragment.hideProgressBar();
            }

            @Override
            public void onSuccess(URL url) {
                mThsVisitHistoryDetailFragment.showHippsNotice(url.toString());
            }
        });
    }

    public void downloadReport() {
        try {
            THSManager.getInstance().getVisitReportAttachment(mThsVisitHistoryDetailFragment.getContext(),
                    mThsVisitHistoryDetailFragment.getVisitReport(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(FileAttachment fileAttachment, SDKError sdkError) {
        if(null!=mThsVisitHistoryDetailFragment && mThsVisitHistoryDetailFragment.isFragmentAttached()) {
            if (sdkError != null) {
                mThsVisitHistoryDetailFragment.showError(sdkError.getSDKErrorReason().name());
                return;
            }
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "reportDownloaded");
            THSFileUtils fileUtils = new THSFileUtils();

            fileUtils.openAttachment(mThsVisitHistoryDetailFragment.getContext(), fileAttachment);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(null!=mThsVisitHistoryDetailFragment && mThsVisitHistoryDetailFragment.isFragmentAttached()) {
            mThsVisitHistoryDetailFragment.showError(mThsVisitHistoryDetailFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    public void getVisitReportDetail(VisitReport visitReport){

        try {
            THSManager.getInstance().getVisitReportDetail(mThsVisitHistoryDetailFragment.getContext(), visitReport, new THSVisitReportDetailCallback<VisitReportDetail, SDKError>() {
                @Override
                public void onResponse(VisitReportDetail visitReportDetail, SDKError sdkError) {
                    if(null!=mThsVisitHistoryDetailFragment && mThsVisitHistoryDetailFragment.isFragmentAttached()) {
                        mThsVisitHistoryDetailFragment.hideProgressBar();
                        mThsVisitHistoryDetailFragment.updateView(visitReportDetail);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    if(null!=mThsVisitHistoryDetailFragment && mThsVisitHistoryDetailFragment.isFragmentAttached()) {
                        mThsVisitHistoryDetailFragment.hideProgressBar();
                        mThsVisitHistoryDetailFragment.showError(mThsVisitHistoryDetailFragment.getString(R.string.ths_se_server_error_toast_message));
                    }
                }
            });
        } catch (AWSDKInstantiationException e) {
            mThsVisitHistoryDetailFragment.hideProgressBar();
            mThsVisitHistoryDetailFragment.showError(mThsVisitHistoryDetailFragment.getString(R.string.ths_se_server_error_toast_message));
        }


    }
}
