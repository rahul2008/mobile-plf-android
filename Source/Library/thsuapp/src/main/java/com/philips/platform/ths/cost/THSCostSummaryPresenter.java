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
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.insurance.THSInsuranceCallback;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.insurance.THSInsuranceDetailFragment;
import com.philips.platform.ths.insurance.THSSubscription;
import com.philips.platform.ths.payment.THSCreditCardDetailFragment;
import com.philips.platform.ths.payment.THSPaymentCallback;
import com.philips.platform.ths.payment.THSPaymentMethod;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.visit.THSWaitingRoomFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_ALERT;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;


public class THSCostSummaryPresenter implements THSBasePresenter, CreateVisitCallback<THSVisit, THSSDKError>, THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError>, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {

    private THSCostSummaryFragment mTHSCostSummaryFragment;

    public THSCostSummaryPresenter(THSCostSummaryFragment thsCostSummaryFragment) {
        mTHSCostSummaryFragment = thsCostSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_cost_summary_continue_button) {
            THSWaitingRoomFragment thsWaitingRoomFragment = new THSWaitingRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY,mTHSCostSummaryFragment.thsVisit.getVisit());
            mTHSCostSummaryFragment.addFragment(thsWaitingRoomFragment, THSWaitingRoomFragment.TAG, bundle);

        } else if (componentID == R.id.ths_cost_summary_payment_detail_framelayout) {
            final THSCreditCardDetailFragment fragment = new THSCreditCardDetailFragment();
            fragment.setFragmentLauncher(mTHSCostSummaryFragment.getFragmentLauncher());
            mTHSCostSummaryFragment.addFragment(fragment, THSCreditCardDetailFragment.TAG, null);

        } else if (componentID == R.id.ths_cost_summary_insurance_detail_framelayout) {
            THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            mTHSCostSummaryFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle);
        }else if (componentID == R.id.uid_dialog_positive_button) {
            // matchmaking failed
            mTHSCostSummaryFragment.alertDialogFragment.dismiss();
            mTHSCostSummaryFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG,0);
        }

    }


    void createVisit() {
        try {
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(false);
            THSManager.getInstance().createVisit(mTHSCostSummaryFragment.getFragmentActivity(), THSManager.getInstance().getPthVisitContext(), this);
        } catch (Exception e) {

        }

    }


    //fetch Insurance
    void fetchExistingSubscription() {
        try {
            THSManager.getInstance().getExistingSubscription(mTHSCostSummaryFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }


    // fetch card detail
    void getPaymentMethod() {
        try {
            THSManager.getInstance().getPaymentMethod(mTHSCostSummaryFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }

    // start of createVisit callbacks
    @Override
    public void onCreateVisitResponse(THSVisit tHSVisit, THSSDKError tHSSDKError) {
        mTHSCostSummaryFragment.hideProgressBar();
        if (null != tHSVisit) {
            mTHSCostSummaryFragment.thsVisit=tHSVisit;
            double costDouble = tHSVisit.getVisit().getVisitCost().getExpectedConsumerCopayCost();
            String costString = String.valueOf(costDouble);
            String[] costStringArray = costString.split("\\.");// seperate the decimal value
            mTHSCostSummaryFragment.costBigLabel.setText("$" + costStringArray[0]);
            if (!"0".equals(costStringArray[1])) { // if decimal part is zero then dont show
                mTHSCostSummaryFragment.costSmallLabel.setText("." + costStringArray[1]);
            }
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
        }
    }

    @Override
    public void onCreateVisitFailure(Throwable var1) {
        mTHSCostSummaryFragment.hideProgressBar();
        showCostError(true,true,false,var1.toString());
    }

    @Override
    public void onCreateVisitValidationFailure(Map<String, ValidationReason> var1) {
        mTHSCostSummaryFragment.hideProgressBar();
        showCostError(true,true,false,var1.toString());
    }
    // end of createVisit callbacks


    // start of getInsurance callbacks
    @Override
    public void onGetInsuranceResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError) {
        if (null != tHSSubscription && null != tHSSubscription.getSubscription()) {
            // show insurance detail
            mTHSCostSummaryFragment.mNoInsuranceDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mInsuranceDetailRelativeLayout.setVisibility(View.VISIBLE);
            Subscription subscription = tHSSubscription.getSubscription();
            mTHSCostSummaryFragment.mInsuranceName.setText(subscription.getHealthPlan().getName());
            mTHSCostSummaryFragment.mInsuranceMemberId.setText("Member ID: " + subscription.getSubscriberId());
            mTHSCostSummaryFragment.mInsuranceSubscriptionType.setText(subscription.getRelationship().getName());
        } else {
            // show no insurance detail
            mTHSCostSummaryFragment.mInsuranceDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mNoInsuranceDetailRelativeLayout.setVisibility(View.VISIBLE);
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
            mTHSCostSummaryFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);
            PaymentMethod paymentMethod = tHSPaymentMethod.getPaymentMethod();
            mTHSCostSummaryFragment.mCardType.setText(paymentMethod.getType());
            mTHSCostSummaryFragment.mMaskedCardNumber.setText("xxxx xxxx xxxx " + paymentMethod.getLastDigits());

            //  show  "Ok, continue" button
            mTHSCostSummaryFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.VISIBLE);

            if(paymentMethod.isExpired()){
                mTHSCostSummaryFragment.mCardExpirationDate.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_not_valid_credit_card));
            }else {
                mTHSCostSummaryFragment.mCardExpirationDate.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_valid_credit_card));
            }
        } else {
            // show no payment detail
            mTHSCostSummaryFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setText("Add payment method");


            //  show  "Add payment method" button
            mTHSCostSummaryFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {

    }
    // end of getPayment callbacks

    void showCostError(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon, final String message) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCostSummaryFragment.getFragmentActivity())
                .setMessage(showLargeContent ? message : message).
                        setPositiveButton(" Ok ", mTHSCostSummaryFragment);

        if (isWithTitle) {
            builder.setTitle("Error");

        }
        mTHSCostSummaryFragment.alertDialogFragment = builder.setCancelable(false).create();
        mTHSCostSummaryFragment.alertDialogFragment.show(mTHSCostSummaryFragment.getFragmentManager(), THS_PROVIDER_DETAIL_ALERT);

    }
}
