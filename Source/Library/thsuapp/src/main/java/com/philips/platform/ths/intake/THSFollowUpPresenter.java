/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
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
    private Pharmacy pharmacy;

    public THSFollowUpPresenter(THSFollowUpFragment tHSFollowUpFragment) {
        this.mTHSFollowUpFragment = tHSFollowUpFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_intake_follow_up_continue_button) {
            if (null != mTHSFollowUpFragment.mPhoneNumberEditText.getText() && !mTHSFollowUpFragment.mPhoneNumberEditText.getText().toString().isEmpty()) {
                mTHSFollowUpFragment.mFollowUpContinueButton.showProgressIndicator();
                acceptLegalText();
                updateConsumer(mTHSFollowUpFragment.mPhoneNumberEditText.getText().toString().trim());
            } else {
                mTHSFollowUpFragment.showToast("Please Enter a valid Phone Number");
            }

        } else if (componentID == R.id.pth_intake_follow_up_i_agree_link_text) {

            final THSNoticeOfPrivacyPracticesFragment fragment = new THSNoticeOfPrivacyPracticesFragment();
            fragment.setFragmentLauncher(mTHSFollowUpFragment.getFragmentLauncher());
            mTHSFollowUpFragment.addFragment(fragment, THSNoticeOfPrivacyPracticesFragment.TAG, null);
        }
    }

    private boolean checkIfDODFlow() {
        return !THSManager.getInstance().getPthVisitContext().hasProvider();
    }

    private void acceptLegalText() {
        List<LegalText> legalTextList = THSManager.getInstance().getPthVisitContext().getLegalTexts();
        for (LegalText legalText : legalTextList) {
            legalText.setAccepted(true);
        }
    }


    protected void updateConsumer(String updatedPhoner) {


        try {
            THSManager.getInstance().updateConsumer(mTHSFollowUpFragment.getFragmentActivity(), updatedPhoner, this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {
        mTHSFollowUpFragment.mFollowUpContinueButton.hideProgressIndicator();
        mTHSFollowUpFragment.showToast(var1.toString());
    }

    @Override
    public void onUpdateConsumerResponse(THSConsumer thsConsumer, THSSDKPasswordError sdkPasswordError) {
        mTHSFollowUpFragment.mFollowUpContinueButton.hideProgressIndicator();
        if (checkIfDODFlow()) {
            //TODO: Add DOD flow here
        } else {
            mTHSFollowUpFragment.addFragment(new THSCheckPharmacyConditionsFragmentNew(), THSCheckPharmacyConditionsFragmentNew.TAG, null);
        }
        //update singleton THSManager THSConsumer member
        THSManager.getInstance().setPTHConsumer(thsConsumer);

    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {
        mTHSFollowUpFragment.mFollowUpContinueButton.hideProgressIndicator();
        mTHSFollowUpFragment.showToast(var1.getMessage());
    }

}
