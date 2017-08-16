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
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
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
        if (sdkError!=null){
            mThsVisitHistoryDetailFragment.showToast(sdkError.getSDKErrorReason().name());
            return;
        }
        copyInputStreamToFile(fileAttachment.getInputStream(),fileAttachment.getType());
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsVisitHistoryDetailFragment.showToast(throwable.getMessage());
    }

    private void copyInputStreamToFile(InputStream inputStream, String extension) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            String fileName = "targetFile" + new Date() + "." + extension;
            File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            gotoFileLocation(targetFile);
            mThsVisitHistoryDetailFragment.showToast("Stored the file successfully and file -> " + fileName);
        } catch (IOException e) {
            mThsVisitHistoryDetailFragment.showToast("File could not be stored");
        }
    }

    void gotoFileLocation(File file){

        Uri selectedUri= Uri.fromFile(file.getParentFile());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(mThsVisitHistoryDetailFragment.getActivity().getPackageManager(), 0) != null)
        {
            mThsVisitHistoryDetailFragment.getActivity().startActivity(intent);
        }
        else
        {
            // if you reach this place, it means there is no any file
            // explorer app installed on your device
        }
    }

}
