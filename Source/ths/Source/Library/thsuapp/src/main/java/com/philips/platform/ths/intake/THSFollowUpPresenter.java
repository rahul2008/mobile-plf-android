/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_UPDATE_CONSUMER_PHONE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

public class THSFollowUpPresenter implements THSBasePresenter, THSUpdateConsumerCallback<THSConsumerWrapper, THSSDKPasswordError> {
    private THSFollowUpFragment mTHSFollowUpFragment;
    private THSFollowUpViewInterface thsFollowUpViewInterfaces;

    public THSFollowUpPresenter(THSFollowUpFragment tHSFollowUpFragment, THSFollowUpViewInterface thsFollowUpViewInterface) {
        this.mTHSFollowUpFragment = tHSFollowUpFragment;
        this.thsFollowUpViewInterfaces = thsFollowUpViewInterface;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_intake_follow_up_continue_button) {
            if (thsFollowUpViewInterfaces.validatePhoneNumber()) {
                thsFollowUpViewInterfaces.hideInlineError();
                thsFollowUpViewInterfaces.startProgressButton();
                acceptLegalText();
                updateConsumer(thsFollowUpViewInterfaces.getConsumerPhoneNumber());
            } else {
                thsFollowUpViewInterfaces.showInlineError();

            }

        } else if (componentID == R.id.pth_intake_follow_up_i_agree_link_text) {

            thsFollowUpViewInterfaces.showNoticeOfPrivacyFragment();
        }
    }


    private void acceptLegalText() {
        List<LegalText> legalTextList = THSManager.getInstance().getPthVisitContext().getLegalTexts();
        for (LegalText legalText : legalTextList) {
            legalText.setAccepted(true);
        }
    }


    protected void updateConsumer(String updatedPhoneNumber) {


        try {
            THSManager.getInstance().updateConsumer(mTHSFollowUpFragment.getFragmentActivity(), updatedPhoneNumber, this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {
        thsFollowUpViewInterfaces.hideProgressButton();
    }

    @Override
    public void onUpdateConsumerResponse(THSConsumerWrapper thsConsumer, THSSDKPasswordError sdkPasswordError) {
        if(null != sdkPasswordError.getSdkPasswordError()) {
                if(null != sdkPasswordError.getSdkPasswordError().getSDKErrorReason()) {
                    thsFollowUpViewInterfaces.showError(THSSDKErrorFactory.getErrorType(thsFollowUpViewInterfaces.getFragmentActivity(), ANALYTIC_UPDATE_CONSUMER_PHONE,sdkPasswordError.getSdkPasswordError()));
                }
        }
        else {
            thsFollowUpViewInterfaces.hideProgressButton();
            //update singleton THSManager THSConsumer member
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "step5PhoneNumberAdded");
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, "TImePrepareYourVisit", THSTagUtils.getVisitPrepareTime(THSSymptomsFragment.visitStartTime));
            THSManager.getInstance().setPTHConsumer(thsConsumer);
            if (THSManager.getInstance().isMatchMakingVisit()) { // if DOD flow
                thsFollowUpViewInterfaces.showProviderDetailsFragment();
            } else {
                thsFollowUpViewInterfaces.showConditionsFragment();
            }
            //update singleton THSManager THSConsumer member
        }


    }



    @Override
    public void onUpdateConsumerFailure(Throwable var1) {
        if(null != mTHSFollowUpFragment && mTHSFollowUpFragment.isFragmentAttached()){
            mTHSFollowUpFragment.showError(mTHSFollowUpFragment.getString(R.string.ths_se_server_error_toast_message));
            thsFollowUpViewInterfaces.hideProgressButton();
        }
    }

}
