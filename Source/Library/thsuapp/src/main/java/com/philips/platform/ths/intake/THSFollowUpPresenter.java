/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;

public class THSFollowUpPresenter implements THSBasePresenter, THSUpdateConsumerCallback<THSConsumer, THSSDKPasswordError>
        , THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {
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
            // mTHSFollowUpFragment.addFragment(new THSInsuranceConfirmationFragment(), THSInsuranceConfirmationFragment.TAG, null);
        } else if (componentID == R.id.pth_intake_follow_up_i_agree_link_text) {

            final THSNoticeOfPrivacyPracticesFragment fragment = new THSNoticeOfPrivacyPracticesFragment();
            fragment.setFragmentLauncher(mTHSFollowUpFragment.getFragmentLauncher());
            mTHSFollowUpFragment.addFragment(fragment, THSNoticeOfPrivacyPracticesFragment.TAG, null);
        }
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

    public void fetchConsumerPreferredPharmacy(THSConsumer thsConsumer) {
        try {
            THSManager.getInstance().getConsumerPreferredPharmacy(mTHSFollowUpFragment.getFragmentActivity(), thsConsumer, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void getConsumerShippingAddress(THSConsumer thsConsumer) {
        try {
            THSManager.getInstance().getConsumerShippingAddress(mTHSFollowUpFragment.getFragmentActivity(), thsConsumer, this);
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
        //update signleton THSManager THSConsumer member
        THSManager.getInstance().setPTHConsumer(thsConsumer);
        boolean isPharmacyDetailRequired = THSManager.getInstance().getPthVisitContext().getVisitContext().isCanPrescribe();
        isPharmacyDetailRequired=true;// todo this line is to removed when isCanPrescribe() returns correct value
        if (isPharmacyDetailRequired) { // go to pharmacy detail
            fetchConsumerPreferredPharmacy(thsConsumer);
        } else {  // go to insurance or cost detail
            Consumer consumer = THSManager.getInstance().getPTHConsumer().getConsumer();
            if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
                final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
                mTHSFollowUpFragment.addFragment(fragment, THSCostSummaryFragment.TAG, null);
            } else {
                final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
                mTHSFollowUpFragment.addFragment(fragment, THSInsuranceConfirmationFragment.TAG, null);
            }
        }
    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {
        mTHSFollowUpFragment.mFollowUpContinueButton.hideProgressIndicator();
        mTHSFollowUpFragment.showToast(var1.getMessage());
    }

    @Override
    public void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError) {
        if (null != pharmacy) {
            this.pharmacy = pharmacy;
            getConsumerShippingAddress(THSManager.getInstance().getPTHConsumer());
            //mTHSFollowUpFragment.displaySearchPharmacy();
        } else {
            mTHSFollowUpFragment.displaySearchPharmacy();
        }
    }

    @Override
    public void onSuccessfulFetch(Address address, SDKError sdkError) {
        if (null != address) {
            mTHSFollowUpFragment.displayPharmacyAndShippingPreferenceFragment(pharmacy, address);
        } else {
            mTHSFollowUpFragment.displaySearchPharmacy();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
