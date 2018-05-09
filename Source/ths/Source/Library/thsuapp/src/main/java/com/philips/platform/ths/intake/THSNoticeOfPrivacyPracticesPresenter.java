/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.americanwell.sdk.entity.legal.LegalText;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.util.List;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_NOPP_FETCH;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;

class THSNoticeOfPrivacyPracticesPresenter implements THSBasePresenter {
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
            if (legalText.isRequired()) {
                mStringBuilder.append(legalText.getLegalText());
                mStringBuilder.append(System.getProperty("line.separator"));
                mStringBuilder.append(System.getProperty("line.separator"));
            }
        }
        final Spanned text = getSpannedText();
        if (null != text) {
            ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).legalTextsLabel.setText(text);
        }else{
            final String errorTag = THSTagUtils.createErrorTag(ANALYTIC_NOPP_FETCH,"Failed to load");
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR, errorTag);
        }
    }

    @Override
    public void onEvent(int componentID) {

    }


    @SuppressWarnings("deprecation")
    private Spanned getSpannedText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(mStringBuilder.toString(), FROM_HTML_MODE_LEGACY); // for 24 api and more
        } else {
            return Html.fromHtml(mStringBuilder.toString());
        }
    }

    //callbacks
    /*@Override
    public void onNoticeOfPrivacyPracticesReceivedSuccess(String string, SDKError sDKError) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
        if (null != string) {
            mStringBuilder.append(System.getProperty("line.separator"));
            mStringBuilder.append(string);
            final Spanned text = getSpannedText();
            ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).legalTextsLabel.setText(text);
        }
    }

    @Override
    public void onNoticeOfPrivacyPracticesReceivedFailure(Throwable throwable) {
        ((THSNoticeOfPrivacyPracticesFragment) uiBaseView).hideProgressBar();
    }*/
}
