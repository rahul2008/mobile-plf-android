/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.insurance.THSInsuranceCallback;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.insurance.THSInsuranceDetailFragment;
import com.philips.platform.ths.insurance.THSSubscription;
import com.philips.platform.ths.payment.THSCreditCardDetailFragment;
import com.philips.platform.ths.payment.THSPaymentCallback;
import com.philips.platform.ths.payment.THSPaymentMethod;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.visit.THSWaitingRoomFragment;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;


public class THSCostSummaryPresenter implements THSBasePresenter, CreateVisitCallback<THSVisit, THSSDKError>, THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError>, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {

    private THSCostSummaryFragment mTHSBaseFragment;

    public THSCostSummaryPresenter(THSCostSummaryFragment thsCostSummaryFragment) {
        mTHSBaseFragment = thsCostSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_cost_summary_continue_button) {
            mTHSBaseFragment.addFragment(new THSWaitingRoomFragment(), THSWaitingRoomFragment.TAG, null);

        } else if (componentID == R.id.ths_cost_summary_payment_detail_framelayout) {
            final THSCreditCardDetailFragment fragment = new THSCreditCardDetailFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment, THSCreditCardDetailFragment.TAG, null);

        } else if (componentID == R.id.ths_cost_summary_insurance_detail_framelayout) {
            THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            mTHSBaseFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle);
        }

    }


    void createVisit() {
        try {
            THSManager.getInstance().createVisit(mTHSBaseFragment.getFragmentActivity(), THSManager.getInstance().getPthVisitContext(), this);
        } catch (Exception e) {

        }

    }


    //fetch Insurance
    void fetchExistingSubscription() {
        try {
            THSManager.getInstance().getExistingSubscription(mTHSBaseFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }


    // fetch card detail
    void getPaymentMethod() {
        try {
            THSManager.getInstance().getPaymentMethod(mTHSBaseFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }

    // start of createVisit callbacks
    @Override
    public void onCreateVisitResponse(THSVisit tHSVisit, THSSDKError tHSSDKError) {
        mTHSBaseFragment.hideProgressBar();
        if (null != tHSVisit) {
            THSManager.getInstance().setTHSVisit(tHSVisit);
            double costDouble = tHSVisit.getVisit().getVisitCost().getExpectedConsumerCopayCost();
            String costString = String.valueOf(costDouble);
            String[] costStringArray = costString.split("\\.");// seperate the decimal value
            mTHSBaseFragment.costBigLabel.setText("$" + costStringArray[0]);
            if (!"0".equals(costStringArray[1])) { // if decimal part is zero then dont show
                mTHSBaseFragment.costSmallLabel.setText("." + costStringArray[1]);
            }
        }
    }

    @Override
    public void onCreateVisitFailure(Throwable var1) {

    }

    @Override
    public void onCreateVisitValidationFailure(Map<String, ValidationReason> var1) {

    }
    // end of createVisit callbacks


    // start of getInsurance callbacks
    @Override
    public void onGetInsuranceResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError) {
        if (null != tHSSubscription && null != tHSSubscription.getSubscription()) {
            // show insurance detail
            mTHSBaseFragment.mNoInsuranceDetailRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mInsuranceDetailRelativeLayout.setVisibility(View.VISIBLE);
            Subscription subscription = tHSSubscription.getSubscription();
            mTHSBaseFragment.mInsuranceName.setText(subscription.getHealthPlan().getName());
            mTHSBaseFragment.mInsuranceMemberId.setText("Member ID: " + subscription.getSubscriberId());
            mTHSBaseFragment.mInsuranceSubscriptionType.setText(subscription.getRelationship().getName());
        } else {
            // show no insurance detail
            mTHSBaseFragment.mInsuranceDetailRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mNoInsuranceDetailRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetInsuranceFailure(Throwable throwable) {

    }
    // end of getInsurance callbacks


    // start of getPayment callbacks
    @Override
    public void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != tHSPaymentMethod && null != tHSPaymentMethod.getPaymentMethod()) {
            // show payment detail
            mTHSBaseFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);
            PaymentMethod paymentMethod = tHSPaymentMethod.getPaymentMethod();
            mTHSBaseFragment.mCardType.setText(paymentMethod.getType());
            mTHSBaseFragment.mMaskedCardNumber.setText("xxxx xxxx xxxx " + paymentMethod.getLastDigits());

            //  show  "Ok, continue" button
            mTHSBaseFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.VISIBLE);

            //mTHSBaseFragment.mCardExpirationDate.setText(paymentMethod.);
        } else {
            // show no payment detail
            mTHSBaseFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);
            mTHSBaseFragment.mCostSummaryContinueButton.setText("Add payment method");


            //  show  "Add payment method" button
            mTHSBaseFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.GONE);
            mTHSBaseFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {

    }
    // end of getPayment callbacks
}
