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
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;

public class THSFollowUpPresenter implements THSBasePresenter, THSUpdateConsumerCallback<THSConsumer, THSSDKPasswordError> {
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
                thsFollowUpViewInterfaces.startProgressButton();
                acceptLegalText();
                updateConsumer(thsFollowUpViewInterfaces.getConsumerPhoneNumber());
            } else {
                thsFollowUpViewInterfaces.showInvalidPhoneNumberToast(mTHSFollowUpFragment.getString(R.string.ths_invalid_phone_number));
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
    public void onUpdateConsumerResponse(THSConsumer thsConsumer, THSSDKPasswordError sdkPasswordError) {
        thsFollowUpViewInterfaces.hideProgressButton();
        THSManager.getInstance().setPTHConsumer(thsConsumer);
        if (THSManager.getInstance().isMatchMakingVisit()) { // if DOD flow
            thsFollowUpViewInterfaces.showProviderDetailsFragment();
        } else {
            thsFollowUpViewInterfaces.showConditionsFragment();
        }
        //update singleton THSManager THSConsumer member


    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {
        thsFollowUpViewInterfaces.hideProgressButton();
        mTHSFollowUpFragment.showToast(var1.getMessage());
    }

}
