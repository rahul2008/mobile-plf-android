/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

class THSNoticeOfPrivacyPracticesPresenter implements THSBasePresenter, THSNoticeOfPrivacyPracticesCallBack {
    private THSBaseView uiBaseView;
    protected THSVisitContext mTHSVisitContext;
    private StringBuilder mStringBuilder;

    THSNoticeOfPrivacyPracticesPresenter(THSBaseView uiBaseView) {

        this.uiBaseView = uiBaseView;
        mTHSVisitContext = THSManager.getInstance().getPthVisitContext();
        mStringBuilder = new StringBuilder();
    }


    void showLegalTextForNOPP() {
        List<LegalText> legalTextList = mTHSVisitContext.getLegalTexts();
        for (LegalText legalText : legalTextList) {
            try{
            THSManager.getInstance().getLegaltext(uiBaseView.getFragmentActivity(), legalText,this);
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
    public void onNoticeOfPrivacyPracticesReceivedSuccess(String string, SDKError sDKError) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
        if (null != string) {
            mStringBuilder.append(System.getProperty("line.separator"));
            mStringBuilder.append(string);
            final Spanned text = getSpannedText();
            ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).legalTextsLabel.setText(text);
        }
    }

    private Spanned getSpannedText() {
        if (Build.VERSION.SDK_INT >= 24) {
           return Html.fromHtml(mStringBuilder.toString(), FROM_HTML_MODE_LEGACY); // for 24 api and more
        } else {
            return Html.fromHtml(mStringBuilder.toString());
        }
    }

    @Override
    public void onNoticeOfPrivacyPracticesReceivedFailure(Throwable throwable) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
    }
}
