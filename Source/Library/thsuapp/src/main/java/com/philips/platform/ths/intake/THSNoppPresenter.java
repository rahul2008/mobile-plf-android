package com.philips.platform.ths.intake;

import android.text.Html;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.PTHManager;

import java.util.List;

/**
 * Created by philips on 7/6/17.
 */

public class THSNoppPresenter implements THSBasePresenter, THSNoppCallBack {
    THSBaseView uiBaseView;
    THSVisitContext mTHSVisitContext;
    List<LegalText> legalTextList;
    StringBuilder mStringBuilder;

    public THSNoppPresenter(THSBaseView uiBaseView) {

        this.uiBaseView = uiBaseView;
        mTHSVisitContext = PTHManager.getInstance().getPthVisitContext();
        mStringBuilder = new StringBuilder();
    }


    public void showLegalTextForNOPP() {
        ((THSNoppFragment) uiBaseView).showProgressBar();
        legalTextList = mTHSVisitContext.getLegalTexts();
        for (LegalText legalText : legalTextList) {
            try{
            PTHManager.getInstance().getLegaltext(uiBaseView.getFragmentActivity(), legalText,this);
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
            ((THSNoppFragment) uiBaseView).legalTextsLabel.setText(Html.fromHtml(mStringBuilder.toString()));
        }
    }

    @Override
    public void onNoppReceivedFailure(Throwable throwable) {
        ((THSNoppFragment) uiBaseView).hideProgressBar();
    }
}
