package com.philips.amwelluapp.intake;

import android.text.Html;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.List;

/**
 * Created by philips on 7/6/17.
 */

public class THSNoppPresenter implements PTHBasePresenter, THSNoppCallBack {
    PTHBaseView uiBaseView;
    PTHVisitContext mPTHVisitContext;
    List<LegalText> legalTextList;
    StringBuilder mStringBuilder;

    public THSNoppPresenter(PTHBaseView uiBaseView) {

        this.uiBaseView = uiBaseView;
        mPTHVisitContext = PTHManager.getInstance().getPthVisitContext();
        mStringBuilder = new StringBuilder();
    }


    public void showLegalTextForNOPP() {
        ((THSNoppFragment) uiBaseView).showProgressBar();
        legalTextList = mPTHVisitContext.getLegalTexts();
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
