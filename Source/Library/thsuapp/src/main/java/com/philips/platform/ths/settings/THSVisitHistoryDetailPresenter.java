/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class THSVisitHistoryDetailPresenter implements THSBasePresenter, THSVisitReportAttachmentCallback<FileAttachment,SDKError> {

    THSVisitHistoryDetailFragment mThsVisitHistoryDetailFragment;

    public THSVisitHistoryDetailPresenter(THSVisitHistoryDetailFragment thsVisitHistoryDetailFragment) {
        mThsVisitHistoryDetailFragment = thsVisitHistoryDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_pdf_container){
            try {
                THSManager.getInstance().getVisitReportAttachment(mThsVisitHistoryDetailFragment.getContext(),
                        mThsVisitHistoryDetailFragment.getVisitReport(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(FileAttachment fileAttachment, SDKError sdkError) {
        if (sdkError != null) {
            mThsVisitHistoryDetailFragment.showToast(sdkError.getSDKErrorReason().name());
            return;
        }

        THSFileUtils fileUtils = new THSFileUtils();

        fileUtils.openAttachment(mThsVisitHistoryDetailFragment.getContext(), fileAttachment);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsVisitHistoryDetailFragment.showToast(throwable.getMessage());
    }
}
