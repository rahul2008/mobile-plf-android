/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.text.Html;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;


public class THSNoppPresenter implements THSBasePresenter, THSNoppCallBack {
    private THSBaseView uiBaseView;
    private THSVisitContext mTHSVisitContext;
    private List<LegalText> legalTextList;
    private StringBuilder mStringBuilder;

    public THSNoppPresenter(THSBaseView uiBaseView) {

        this.uiBaseView = uiBaseView;
        mTHSVisitContext = THSManager.getInstance().getPthVisitContext();
        mStringBuilder = new StringBuilder();
    }


    public void showLegalTextForNOPP() {
        legalTextList = mTHSVisitContext.getLegalTexts();
        for (LegalText legalText : legalTextList) {
            try {
                THSManager.getInstance().getLegaltext(uiBaseView.getFragmentActivity(), legalText, this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onEvent(int componentID) {

    }


    //callbacks
    @Override
    public void onNoppReceivedSuccess(String string, SDKError sDKError) {
        ((THSNoppFragment) uiBaseView).hideProgressBar();
        if (null != string) {
            mStringBuilder.append(System.getProperty("line.separator"));
            mStringBuilder.append(string);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ((THSNoppFragment) uiBaseView).legalTextsLabel.setText(Html.fromHtml(mStringBuilder.toString(),Html.FROM_HTML_MODE_LEGACY));
            } else {
                ((THSNoppFragment) uiBaseView).legalTextsLabel.setText(Html.fromHtml(mStringBuilder.toString()));
            }

        }
    }

    @Override
    public void onNoppReceivedFailure(Throwable throwable) {
        ((THSNoppFragment) uiBaseView).hideProgressBar();
    }
}
