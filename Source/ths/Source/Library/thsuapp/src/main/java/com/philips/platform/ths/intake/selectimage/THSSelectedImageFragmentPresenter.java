/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;


public class THSSelectedImageFragmentPresenter implements THSBasePresenter, THSDeleteDocumentCallback {
    THSSelectedImageFragmentViewCallback thsSelectedImageFragmentViewCallback;

    public THSSelectedImageFragmentPresenter(THSSelectedImageFragmentViewCallback thsSelectedImageFragmentViewCallback){
        this.thsSelectedImageFragmentViewCallback = thsSelectedImageFragmentViewCallback;
    }
    @Override
    public void onEvent(int componentID) {


        if (componentID == R.id.ths_delete_selected_image_button) {
        }
    }

    public void deleteDocument(DocumentRecord documentRecord) {
        thsSelectedImageFragmentViewCallback.updateProgreeDialog(true);
        try {
            THSManager.getInstance().deletedHealthDocument(thsSelectedImageFragmentViewCallback.getFragmentActivity(),documentRecord,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteSuccess(Void voidResponse, SDKError sdkError) {
        thsSelectedImageFragmentViewCallback.updateProgreeDialog(false);
        if(null != sdkError){
            if(null != sdkError.getMessage()){
                thsSelectedImageFragmentViewCallback.showToast(sdkError.getMessage().toString());
            }
        }
        else {
            thsSelectedImageFragmentViewCallback.showToast(thsSelectedImageFragmentViewCallback.getFragmentActivity().getString(R.string.ths_delete_success_string));
        }

    }

    @Override
    public void onError(Throwable throwable) {
        thsSelectedImageFragmentViewCallback.updateProgreeDialog(false);
        thsSelectedImageFragmentViewCallback.showToast(thsSelectedImageFragmentViewCallback.getFragmentActivity().getString(R.string.ths_se_server_error_toast_message));
    }
}
