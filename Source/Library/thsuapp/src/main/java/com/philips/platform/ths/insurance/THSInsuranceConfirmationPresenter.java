/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;

public class THSInsuranceConfirmationPresenter implements THSBasePresenter, THSSDKValidatedCallback<Void, SDKError> {
    private THSInsuranceConfirmationFragment mTHSInsuranceConfirmationFragment;


    public THSInsuranceConfirmationPresenter(THSInsuranceConfirmationFragment tHSInsuranceConfirmationFragment) {
        this.mTHSInsuranceConfirmationFragment = tHSInsuranceConfirmationFragment;
    }


    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_insurance_confirmation_radio_option_yes) {
            THSInsuranceDetailFragment fragment = new THSInsuranceDetailFragment();
            Bundle bundle = null;
            if (mTHSInsuranceConfirmationFragment.isLaunchedFromCostSummary) {
                bundle = new Bundle();
                bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            }
            mTHSInsuranceConfirmationFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle);

        } else if (componentID == R.id.pth_insurance_confirmation_radio_option_no) {
            Subscription currentSubscription = THSManager.getInstance().getPTHConsumer().getConsumer().getSubscription();
            if(null==currentSubscription) {
                showCostSummary();
            }else{
                //remove previous insurance detail
                THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest = getSubscriptionUpdateRequestWithoutVistContext();
                updateInsurance(thsSubscriptionUpdateRequest); // empty SubscriptionUpdateRequest
            }

        }

    }

    public THSSubscriptionUpdateRequest getSubscriptionUpdateRequestWithoutVistContext() {
        THSSubscriptionUpdateRequest tHSSubscriptionUpdateRequest = null;
        try {
            tHSSubscriptionUpdateRequest = THSManager.getInstance().getNewSubscriptionUpdateRequest(mTHSInsuranceConfirmationFragment.getFragmentActivity());
        } catch (Exception e) {
        }
        return tHSSubscriptionUpdateRequest;
    }

    private void showCostSummary(){
        if (mTHSInsuranceConfirmationFragment.isLaunchedFromCostSummary) {
            mTHSInsuranceConfirmationFragment.getActivity().getSupportFragmentManager().popBackStack(THSCostSummaryFragment.TAG, 0);
        } else {

            final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            fragment.setFragmentLauncher(mTHSInsuranceConfirmationFragment.getFragmentLauncher());
            mTHSInsuranceConfirmationFragment.addFragment(fragment, THSCostSummaryFragment.TAG, null);
        }
    }

    private void updateInsurance( THSSubscriptionUpdateRequest tHSSubscriptionUpdateRequest){
        try {
            mTHSInsuranceConfirmationFragment.showProgressbar();
            THSManager.getInstance().updateInsuranceSubscription(mTHSInsuranceConfirmationFragment.getFragmentActivity(), tHSSubscriptionUpdateRequest, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        mTHSInsuranceConfirmationFragment.hideProgressBar();
    }

    @Override
    public void onResponse(Void aVoid, SDKError sdkError) {
        mTHSInsuranceConfirmationFragment.hideProgressBar();
        showCostSummary();
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTHSInsuranceConfirmationFragment.hideProgressBar();

    }
}
