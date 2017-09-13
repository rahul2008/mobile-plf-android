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
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.visit.THSWaitingRoomFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_ALERT;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;


class THSCostSummaryPresenter implements THSBasePresenter, CreateVisitCallback<THSVisit, THSSDKError>, THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError>, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError>, ApplyCouponCallback<Void, THSSDKError> {

    private THSCostSummaryFragment mTHSCostSummaryFragment;


    THSCostSummaryPresenter(THSCostSummaryFragment thsCostSummaryFragment) {
        mTHSCostSummaryFragment = thsCostSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_cost_summary_continue_button) {
            THSWaitingRoomFragment thsWaitingRoomFragment = new THSWaitingRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY, mTHSCostSummaryFragment.thsVisit.getVisit());
            mTHSCostSummaryFragment.addFragment(thsWaitingRoomFragment, THSWaitingRoomFragment.TAG, bundle);

        } else if (componentID == R.id.ths_cost_summary_payment_detail_framelayout || componentID == R.id.ths_cost_summary_add_payment_method_button) {
            final THSCreditCardDetailFragment fragment = new THSCreditCardDetailFragment();
            fragment.setFragmentLauncher(mTHSCostSummaryFragment.getFragmentLauncher());
            mTHSCostSummaryFragment.addFragment(fragment, THSCreditCardDetailFragment.TAG, null);

        } else if (componentID == R.id.ths_cost_summary_insurance_detail_framelayout) {
            THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            mTHSCostSummaryFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle);
        } else if (componentID == R.id.uid_dialog_positive_button) {
            // matchmaking failed
            mTHSCostSummaryFragment.alertDialogFragment.dismiss();
            //mTHSCostSummaryFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG,0);
        } else if (componentID == R.id.ths_cost_summary_promotion_code_apply_button) {
            applyCouponCode(mTHSCostSummaryFragment.mCouponCodeEdittext.getText().toString().trim());
        }

    }


    private void createVisit() {
        try {
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(false);
            THSManager.getInstance().createVisit(mTHSCostSummaryFragment.getFragmentActivity(), THSManager.getInstance().getPthVisitContext(), this);
        } catch (Exception e) {

        }

    }

    private void applyCouponCode(String couponCode) {
        try {
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(false);
            mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(false);
            if (null == couponCode || couponCode.isEmpty()) {
                String errorMessage = mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_coupon_code_empty);
                showCostError(true, true, errorMessage);
            }
            THSManager.getInstance().applyCouponCode(mTHSCostSummaryFragment.getFragmentActivity(), mTHSCostSummaryFragment.thsVisit, couponCode, this);
        } catch (Exception e) {
            mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
            showCostError(true, true, e.getMessage());
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
            String couponCode = null;
            if (null != mTHSCostSummaryFragment.thsVisit && null != mTHSCostSummaryFragment.thsVisit.getCouponCodeApplied() && !mTHSCostSummaryFragment.thsVisit.getCouponCodeApplied().isEmpty()) {
                couponCode = mTHSCostSummaryFragment.thsVisit.getCouponCodeApplied();
            }
            mTHSCostSummaryFragment.thsVisit = tHSVisit;
            mTHSCostSummaryFragment.thsVisit.setCouponCodeApplied(couponCode);
            mTHSCostSummaryFragment.thsVisit.setInitialVisitCost(tHSVisit.getVisit().getVisitCost().getExpectedConsumerCopayCost());
            if (null != couponCode && !couponCode.isEmpty()) {
                applyCouponCode(mTHSCostSummaryFragment.thsVisit.getCouponCodeApplied());
            } else {
                updateCost(mTHSCostSummaryFragment.thsVisit);
            }
        }
    }

    private void updateCost(THSVisit thsVisit) {
        if (thsVisit.getVisit().getVisitCost().isFree()) {
            mTHSCostSummaryFragment.mActualCostHeader.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_free_visit_header));
            mTHSCostSummaryFragment.costBigLabel.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_free_visit_text));
            mTHSCostSummaryFragment.costSmallLabel.setText(null);
            String initialCostString = String.format(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_initial_Full_cover_cost), "$" + String.valueOf(thsVisit.getInitialVisitCost()));
            mTHSCostSummaryFragment.mInitialVisitCostLabel.setText(initialCostString);
            if(null==mTHSCostSummaryFragment.mTHSPaymentMethod || null==mTHSCostSummaryFragment.mTHSPaymentMethod.getPaymentMethod()) {
                // if payment is not yet added then show Payment not required with continue button
                noPaymentRequired();
            }
        } else {
            double costDouble = thsVisit.getVisit().getVisitCost().getExpectedConsumerCopayCost();
            String costString = String.valueOf(costDouble);
            String[] costStringArray = costString.split("\\.");// seperate the decimal value
            mTHSCostSummaryFragment.costBigLabel.setText(String.valueOf("$" + costStringArray[0]));
            if (!"0".equals(costStringArray[1])) { // if decimal part is zero then dont show
                mTHSCostSummaryFragment.costSmallLabel.setText(String.valueOf("." + costStringArray[1]));
            }
            String initialCostString = String.format(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_initial_Partial_cover_cost), "$" + String.valueOf(thsVisit.getInitialVisitCost()));
            mTHSCostSummaryFragment.mInitialVisitCostLabel.setText(initialCostString);
        }
        mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
        mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
    }

    @Override
    public void onCreateVisitFailure(Throwable var1) {
        mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(false);
        mTHSCostSummaryFragment.hideProgressBar();
        showCostError(true, true, var1.toString());
    }

    @Override
    public void onCreateVisitValidationFailure(Map<String, ValidationReason> var1) {
        mTHSCostSummaryFragment.hideProgressBar();
        showCostError(true, true, var1.toString());
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
            mTHSCostSummaryFragment.mInsuranceMemberId.setText(String.valueOf(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_member_id) + subscription.getSubscriberId()));
            mTHSCostSummaryFragment.mInsuranceSubscriptionType.setText(subscription.getRelationship().getName());
        } else {
            // show no insurance detail
            mTHSCostSummaryFragment.mInsuranceDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mNoInsuranceDetailRelativeLayout.setVisibility(View.VISIBLE);
        }
        createVisit();
    }

    @Override
    public void onGetInsuranceFailure(Throwable throwable) {

    }
    // end of getInsurance callbacks


    // start of getPayment callbacks
    @Override
    public void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        mTHSCostSummaryFragment.mTHSPaymentMethod=tHSPaymentMethod;
        if (null != tHSPaymentMethod && null != tHSPaymentMethod.getPaymentMethod()) {
            // show payment detail
            mTHSCostSummaryFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mPaymentNotRequired.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);
            PaymentMethod paymentMethod = tHSPaymentMethod.getPaymentMethod();
            mTHSCostSummaryFragment.mCardType.setText(paymentMethod.getType());
            mTHSCostSummaryFragment.mMaskedCardNumber.setText(String.valueOf("xxxx xxxx xxxx " + paymentMethod.getLastDigits()));

            //  show  "Ok, continue" button
            mTHSCostSummaryFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.VISIBLE);

            if (paymentMethod.isExpired()) {
                mTHSCostSummaryFragment.mCardExpirationDate.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_not_valid_credit_card));
            } else {
                mTHSCostSummaryFragment.mCardExpirationDate.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_valid_credit_card));
            }
        } else {
            // show no payment detail
            mTHSCostSummaryFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mPaymentNotRequired.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.VISIBLE);

            //  show  "Add payment method" button
            mTHSCostSummaryFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.GONE);
            mTHSCostSummaryFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void noPaymentRequired() {
        // show no payment detail required
        mTHSCostSummaryFragment.mPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
        mTHSCostSummaryFragment.mNoPaymentMethodDetailRelativeLayout.setVisibility(View.GONE);
        mTHSCostSummaryFragment.mPaymentNotRequired.setVisibility(View.VISIBLE);


        mTHSCostSummaryFragment.mCostSummaryContinueButtonRelativeLayout.setVisibility(View.VISIBLE);
        mTHSCostSummaryFragment.mAddPaymentMethodButtonRelativeLayout.setVisibility(View.GONE);

    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {

    }
    // end of getPayment callbacks

    private void showCostError(final boolean showLargeContent, final boolean isWithTitle, final String message) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCostSummaryFragment.getFragmentActivity())
                .setMessage(showLargeContent ? message : message).
                        setPositiveButton(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_ok_button), mTHSCostSummaryFragment);

        if (isWithTitle) {
            builder.setTitle(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_error));

        }
        mTHSCostSummaryFragment.alertDialogFragment = builder.setCancelable(false).create();
        mTHSCostSummaryFragment.alertDialogFragment.show(mTHSCostSummaryFragment.getFragmentManager(), THS_PROVIDER_DETAIL_ALERT);


    }


    // start of PROMO/COUPON code callback
    @Override
    public void onApplyCouponResponse(Void aVoid, THSSDKError thssdkError) {
        mTHSCostSummaryFragment.thsVisit.setCouponCodeApplied(mTHSCostSummaryFragment.mCouponCodeEdittext.getText().toString().trim());
        updateCost(mTHSCostSummaryFragment.thsVisit);
    }

    @Override
    public void onApplyCouponFailure(Throwable throwable) {
        mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
        mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
        String invalidCoupon = mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_coupon_code_invalid);
        showCostError(true, true, invalidCoupon);
    }
    // end of PROMO/COUPON code callback
}
