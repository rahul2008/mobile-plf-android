/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.entity.consumer.Consumer;
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
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.visit.THSWaitingRoomFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_APPLY_PROMOCODE;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_CREATE_VISIT;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_ESTIMATED_VISIT_COST;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_HEALTH_SUBSCRIPTION;
import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY_COUPON_CODE_ERROR;
import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY_CREATE_VISIT_ERROR;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;


class THSCostSummaryPresenter implements THSBasePresenter, CreateVisitCallback<THSVisit, THSSDKError>, THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError>, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError>, ApplyCouponCallback<Void, THSSDKError> {

    private THSCostSummaryFragment mTHSCostSummaryFragment;


    THSCostSummaryPresenter(THSCostSummaryFragment thsCostSummaryFragment) {
        mTHSCostSummaryFragment = thsCostSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_cost_summary_continue_button) {
            THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "costSummaryViewed");
            THSWaitingRoomFragment thsWaitingRoomFragment = new THSWaitingRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY, mTHSCostSummaryFragment.thsVisit.getVisit());
            mTHSCostSummaryFragment.addFragment(thsWaitingRoomFragment, THSWaitingRoomFragment.TAG, bundle, true);

        } else if (componentID == R.id.ths_cost_summary_payment_detail_framelayout || componentID == R.id.ths_cost_summary_add_payment_method_button) {
            final THSCreditCardDetailFragment fragment = new THSCreditCardDetailFragment();
            fragment.setFragmentLauncher(mTHSCostSummaryFragment.getFragmentLauncher());
            mTHSCostSummaryFragment.addFragment(fragment, THSCreditCardDetailFragment.TAG, null, true);

        } else if (componentID == R.id.ths_cost_summary_insurance_detail_framelayout) {
            THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            mTHSCostSummaryFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle, true);
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
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.hideProgressBar();
            if (null != tHSSDKError.getSdkError()) {
                if (null!=tHSSDKError.getSdkError().getSDKErrorReason() && tHSSDKError.getSdkError().getSDKErrorReason() == SDKErrorReason.PROVIDER_OFFLINE) {
                    mTHSCostSummaryFragment.doTagging(ANALYTICS_ESTIMATED_VISIT_COST, mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_provider_offline), false);
                    showCreateVisitError(true, true, mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_provider_offline));
                } else {
                    mTHSCostSummaryFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_CREATE_VISIT, tHSSDKError.getSdkError()), true);
                }
            } else if (null != tHSVisit) {
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

    }

    private void updateCost(THSVisit thsVisit) {
        if (thsVisit.getVisit().getVisitCost().isFree()) {
            mTHSCostSummaryFragment.mActualCostHeader.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_free_visit_header));
            mTHSCostSummaryFragment.costBigLabel.setText(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_free_visit_text));
            mTHSCostSummaryFragment.costSmallLabel.setText(null);
            String initialCostString = String.format(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_initial_Full_cover_cost), "$" + String.valueOf(thsVisit.getInitialVisitCost()));
            mTHSCostSummaryFragment.mInitialVisitCostLabel.setText(initialCostString);
            if (null == mTHSCostSummaryFragment.mTHSPaymentMethod || null == mTHSCostSummaryFragment.mTHSPaymentMethod.getPaymentMethod()) {
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
            String couponCode = mTHSCostSummaryFragment.thsVisit.getCouponCodeApplied();
            Consumer consumer = THSManager.getInstance().getPTHConsumer(mTHSCostSummaryFragment.getContext()).getConsumer();
            if ((consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) || (null != couponCode && !couponCode.isEmpty())) {
                mTHSCostSummaryFragment.mInitialVisitCostLabel.setVisibility(View.VISIBLE);
                String initialCostString = String.format(mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_initial_Partial_cover_cost), "$" + String.valueOf(thsVisit.getInitialVisitCost()));
                mTHSCostSummaryFragment.mInitialVisitCostLabel.setText(initialCostString);
            } else {
                // if No insurance and No Coupon
                mTHSCostSummaryFragment.mInitialVisitCostLabel.setVisibility(View.GONE);
            }

        }
        mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
        mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
    }

    @Override
    public void onCreateVisitFailure(Throwable var1) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(false);
            mTHSCostSummaryFragment.hideProgressBar();
            showCreateVisitError(true, true, mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_provider_offline));
        }
    }

    @Override
    public void onCreateVisitValidationFailure(Map<String, ValidationReason> var1) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.hideProgressBar();
            showCreateVisitError(true, true, var1.toString());
        }
    }
    // end of createVisit callbacks


    // start of getInsurance callbacks
    @Override
    public void onGetInsuranceResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            if (null != tHSSDKError.getSdkError()) {
                mTHSCostSummaryFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_HEALTH_SUBSCRIPTION, tHSSDKError.getSdkError()));
            } else {
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
        }
    }

    @Override
    public void onGetInsuranceFailure(Throwable throwable) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.showToast(R.string.ths_se_server_error_toast_message);
        }

    }
    // end of getInsurance callbacks


    // start of getPayment callbacks
    @Override
    public void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.mTHSPaymentMethod = tHSPaymentMethod;
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
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }
    // end of getPayment callbacks

    private void showCostError(final boolean showLargeContent, final boolean isWithTitle, final String message) {
        if (null != mTHSCostSummaryFragment.alertDialogFragmentCouponCode) {
            mTHSCostSummaryFragment.alertDialogFragmentCouponCode.dismiss();
        }
        View.OnClickListener alertDialogFragmentCouponListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTHSCostSummaryFragment.alertDialogFragmentCouponCode.dismiss();
                mTHSCostSummaryFragment.doTagging(ANALYTICS_ESTIMATED_VISIT_COST,message,false);
            }
        };
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCostSummaryFragment.getFragmentActivity())
                .setMessage(showLargeContent ? message : message).
                        setPositiveButton(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_ok_button), alertDialogFragmentCouponListener);

        if (isWithTitle) {
            builder.setTitle(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_error));

        }
        mTHSCostSummaryFragment.alertDialogFragmentCouponCode = builder.setCancelable(false).create();
        mTHSCostSummaryFragment.alertDialogFragmentCouponCode.setPositiveButtonListener(alertDialogFragmentCouponListener);
        mTHSCostSummaryFragment.alertDialogFragmentCouponCode.show(mTHSCostSummaryFragment.getFragmentManager(), THS_COST_SUMMARY_COUPON_CODE_ERROR);

    }

    private void showCreateVisitError(final boolean showLargeContent, final boolean isWithTitle, final String message) {
        if (null != mTHSCostSummaryFragment.alertDialogFragmentCreateVisit) {
            mTHSCostSummaryFragment.alertDialogFragmentCreateVisit.dismiss();
        }
        View.OnClickListener alertDialogFragmentCreateVisitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTHSCostSummaryFragment.alertDialogFragmentCreateVisit.dismiss();
                mTHSCostSummaryFragment.doTagging(ANALYTICS_ESTIMATED_VISIT_COST,message,false);
                mTHSCostSummaryFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG, 0);
            }
        };
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCostSummaryFragment.getFragmentActivity())
                .setMessage(showLargeContent ? message : message).
                        setPositiveButton(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_ok_button), alertDialogFragmentCreateVisitListener);

        if (isWithTitle) {
            builder.setTitle(mTHSCostSummaryFragment.getResources().getString(R.string.ths_matchmaking_error));

        }
        mTHSCostSummaryFragment.alertDialogFragmentCreateVisit = builder.setCancelable(false).create();
        mTHSCostSummaryFragment.alertDialogFragmentCreateVisit.setPositiveButtonListener(alertDialogFragmentCreateVisitListener);
        mTHSCostSummaryFragment.alertDialogFragmentCreateVisit.show(mTHSCostSummaryFragment.getFragmentManager(), THS_COST_SUMMARY_CREATE_VISIT_ERROR);
    }


    // start of PROMO/COUPON code callback
    @Override
    public void onApplyCouponResponse(Void aVoid, THSSDKError thssdkError) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            if (null != thssdkError.getSdkError()) {
                mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
                mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
                mTHSCostSummaryFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_APPLY_PROMOCODE, thssdkError.getSdkError()));
            } else {
                if (mTHSCostSummaryFragment.isPromoCodeAlreadyApplied.compareAndSet(false, true)) {
                    THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "promoCodeAppliedSuccessfully");
                }
                mTHSCostSummaryFragment.thsVisit.setCouponCodeApplied(mTHSCostSummaryFragment.mCouponCodeEdittext.getText().toString().trim());
                updateCost(mTHSCostSummaryFragment.thsVisit);

            }
        }
    }

    @Override
    public void onApplyCouponFailure(Throwable throwable) {
        if (null != mTHSCostSummaryFragment && mTHSCostSummaryFragment.isFragmentAttached()) {
            mTHSCostSummaryFragment.mCouponCodeButton.setEnabled(true);
            mTHSCostSummaryFragment.mCostSummaryContinueButton.setEnabled(true);
            String invalidCoupon = mTHSCostSummaryFragment.getResources().getString(R.string.ths_cost_summary_coupon_code_invalid);
            showCostError(true, true, invalidCoupon);
        }
    }
    // end of PROMO/COUPON code callback
}
