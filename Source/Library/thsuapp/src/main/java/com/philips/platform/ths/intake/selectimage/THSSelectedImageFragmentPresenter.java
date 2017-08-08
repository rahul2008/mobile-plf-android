/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;

import com.americanwell.sdk.entity.SDKError;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;


public class THSSelectedImageFragmentPresenter implements THSBasePresenter, THSDeleteDocumentCallback {
    @Override
    public void onEvent(int componentID) {


        if (componentID == R.id.ths_delete_selected_image_button) {
            deleteDocument();
        }
    }

    private void deleteDocument() {

        //THSManager.getInstance().deletedHealthDocument();
    }

    @Override
    public void onDeleteSuccess(SDKError sdkError) {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
