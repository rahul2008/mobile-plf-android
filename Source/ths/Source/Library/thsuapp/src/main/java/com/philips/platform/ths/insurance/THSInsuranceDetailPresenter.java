/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import android.view.View;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_HEALTH_SUBSCRIPTION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_HEALTH_PLAN;

class THSInsuranceDetailPresenter implements THSBasePresenter, THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError>, THSSDKValidatedCallback<Void, SDKError> {
    protected THSBaseFragment mTHSBaseFragment;

    THSInsuranceDetailPresenter(THSInsuranceDetailFragment tHSInsuranceDetailFraagment) {
        this.mTHSBaseFragment = tHSInsuranceDetailFraagment;

    }


    THSHealthPlan fetchHealthPlanList() {
        THSHealthPlan tHSHealthPlan = new THSHealthPlan();
        try {
            List<HealthPlan> healthPlanList = THSManager.getInstance().getHealthPlans(mTHSBaseFragment.getFragmentActivity());
            tHSHealthPlan.setHealthPlanList(healthPlanList);
        } catch (Exception e) {

        }
        return tHSHealthPlan;
    }

    THSRelationship fetchSubscriberRelationList() {
        THSRelationship tHSRelationship = new THSRelationship();
        try {
            List<Relationship> relationships = THSManager.getInstance().getSubscriberRelationships(mTHSBaseFragment.getFragmentActivity());
            tHSRelationship.setRelationShipList(relationships);
        } catch (Exception e) {

        }
        return tHSRelationship;
    }

    private THSSubscriptionUpdateRequest getSubscriptionUpdateRequestWithoutVistContext() {
        THSSubscriptionUpdateRequest tHSSubscriptionUpdateRequest = null;
        try {
            tHSSubscriptionUpdateRequest = THSManager.getInstance().getNewSubscriptionUpdateRequest(mTHSBaseFragment.getFragmentActivity());
        } catch (Exception e) {
        }
        return tHSSubscriptionUpdateRequest;
    }


    void fetchExistingSubscription() {
        try {
            THSManager.getInstance().getExistingSubscription(mTHSBaseFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }

    void updateTHSInsuranceSubscription(HealthPlan healthPlan, String subscriberId, String suffix, Relationship relationship, String firstName, String lastName, String dob) {
        try {
            ///////validate


            THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest = getSubscriptionUpdateRequestWithoutVistContext();
            final Subscription subscription = thsSubscriptionUpdateRequest.getSubscriptionUpdateRequest().getSubscription();
            subscription.setHealthPlan(healthPlan);
            if (null == healthPlan) {
                ((THSInsuranceDetailFragment) mTHSBaseFragment).doTagging(ANALYTICS_UPDATE_HEALTH_PLAN, "Please select Insurance", false);
                ((THSInsuranceDetailFragment) mTHSBaseFragment).showError(((THSInsuranceDetailFragment) mTHSBaseFragment).getString(R.string.ths_insurance_please_select_insurance));
                return;
            } else {
                subscription.setSubscriberId(subscriberId);
                if (healthPlan.isUsesSuffix()) {
                    subscription.setSubscriberSuffix(suffix);
                }
                subscription.setRelationship(relationship);
                if (!relationship.isPrimarySubscriber()) {
                    subscription.setPrimarySubscriberFirstName(firstName);
                    subscription.setPrimarySubscriberLastName(lastName);
                    subscription.setPrimarySubscriberDateOfBirth(SDKLocalDate.valueOf((dob)));
                }
                Map<String, ValidationReason> errors = new HashMap<>();
                THSManager.getInstance().validateSubscriptionUpdateRequest(mTHSBaseFragment.getFragmentActivity(), thsSubscriptionUpdateRequest, errors);
                if (errors.isEmpty()) {

                    updateInsurance(thsSubscriptionUpdateRequest);
                } else {
                    mTHSBaseFragment.hideProgressBar();
                    AmwellLog.i("updateInsurance", "validateSubscriptionUpdateRequest error " + errors.toString());
                    //showInsuranceNotVerifiedDialog();
                    String missingFields = "";
                    Iterator<Map.Entry<String, ValidationReason>> it = errors.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        String[] array = pair.getKey().toString().split("\\.");
                        missingFields = missingFields + array[array.length - 1] + "     ";
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                    ((THSInsuranceDetailFragment) mTHSBaseFragment).doTagging(ANALYTICS_UPDATE_HEALTH_PLAN, missingFields + "required", false);
                    mTHSBaseFragment.showError(missingFields + ((THSInsuranceDetailFragment) mTHSBaseFragment).getString(R.string.ths_insurance_fields_required));
                    ((THSInsuranceDetailFragment) mTHSBaseFragment).hideProgressBar();
                    return;
                }
            }
        } catch (Exception e) {
            mTHSBaseFragment.hideProgressBar();
        }

    }

    private void updateInsurance(THSSubscriptionUpdateRequest tHSSubscriptionUpdateRequest) {
        try {
            ((THSInsuranceDetailFragment) mTHSBaseFragment).showProgressbar();
            THSManager.getInstance().updateInsuranceSubscription(mTHSBaseFragment.getFragmentActivity(), tHSSubscriptionUpdateRequest, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_insurance_detail_skip_button) {
            Subscription currentSubscription = THSManager.getInstance().getPTHConsumer(mTHSBaseFragment.getContext()).getConsumer().getSubscription();
            if (null == currentSubscription) {
                showCostSummaryFragment();
            } else {
                //remove previous insurance detail
                THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest = getSubscriptionUpdateRequestWithoutVistContext();
                updateInsurance(thsSubscriptionUpdateRequest); // empty SubscriptionUpdateRequest
            }
        } else if (componentID == R.id.ths_confirmation_dialog_primary_button) {  // continue without Insurance verification
            showCostSummaryFragment();
        } else if (componentID == R.id.ths_confirmation_dialog_secondary_button) {
            // stay on same insurance screen
            mTHSBaseFragment.hideProgressBar();
        }

    }


    ////////// start of getExistingSubscription call back
    @Override
    public void onGetInsuranceResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            if (null != tHSSDKError.getSdkError()) {
                mTHSBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_HEALTH_SUBSCRIPTION, tHSSDKError.getSdkError()), true);
            } else {
                ((THSInsuranceDetailFragment) mTHSBaseFragment).thsSubscriptionExisting = tHSSubscription;
                Subscription subscription = tHSSubscription.getSubscription();
                if (null != subscription) {
                    if (subscription.getHealthPlan() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mHealthPlan = subscription.getHealthPlan();
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).insuranceEditBox.setText(subscription.getHealthPlan().getName());
                    }
                    if (subscription.getHealthPlan() != null && subscription.getHealthPlan().isUsesSuffix() && subscription.getSubscriberId() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).subscriberIDEditBox.setText(subscription.getSubscriberId());
                    }
                    if (subscription.getHealthPlan().isUsesSuffix()) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mSuffixLabel.setVisibility(View.VISIBLE);
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mSuffixEditText.setVisibility(View.VISIBLE);
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mSuffixEditText.setText(subscription.getSubscriberSuffix());
                    } else {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mSuffixLabel.setVisibility(View.GONE);
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mSuffixEditText.setVisibility(View.GONE);
                    }
                    if (subscription.getRelationship().isPrimarySubscriber()) {
                        //if user is  a primary subscriber
                        setPrimarySubscriber();
                    } else {
                        //if user is NOT a primary subscriber
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mNotPrimarySubscriberCheckBox.setChecked(true);
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mNotPrimarySubscriberRelativeLayout.setVisibility(View.VISIBLE);
                    }

                    if (subscription.getRelationship() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).mInsuranceRelationship = subscription.getRelationship();
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).relationshipEditBox.setText(subscription.getRelationship().getName());
                    }
                    if (subscription.getPrimarySubscriberFirstName() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).firstNameEditBox.setText(subscription.getPrimarySubscriberFirstName());
                    }
                    if (subscription.getPrimarySubscriberLastName() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).lastNameEditBox.setText(subscription.getPrimarySubscriberLastName());
                    }
                    if (subscription.getPrimarySubscriberDateOfBirth() != null) {
                        ((THSInsuranceDetailFragment) mTHSBaseFragment).relationDOBEditBox.setText(subscription.getPrimarySubscriberDateOfBirth().toString());
                    }
                } else {
                    setPrimarySubscriber();// in first run
                }

            }
        }
    }

    private void setPrimarySubscriber() {
        ((THSInsuranceDetailFragment) mTHSBaseFragment).mNotPrimarySubscriberCheckBox.setChecked(false);
        ((THSInsuranceDetailFragment) mTHSBaseFragment).mNotPrimarySubscriberRelativeLayout.setVisibility(View.GONE);
    }


    @Override
    public void onGetInsuranceFailure(Throwable throwable) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            mTHSBaseFragment.showError(throwable.getMessage());
        }
    }
    ////////// end of getExistingSubscription call back


    ///////// start update suscription call back
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();

            String errorMessage= (null!=var1)?var1.toString():"null";
            AmwellLog.i("updateInsurance", "onValidationFailure: "+errorMessage);
            ((THSInsuranceDetailFragment) mTHSBaseFragment).doTagging(ANALYTICS_UPDATE_HEALTH_PLAN, errorMessage, false);
            showInsuranceNotVerifiedDialog();
        }
    }

    @Override
    public void onResponse(Void aVoid, SDKError sdkError) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            if (null != sdkError) {
                if (null != sdkError.getSDKErrorReason()) {
                    THSSDKErrorFactory.getErrorType(ANALYTICS_UPDATE_HEALTH_PLAN,sdkError);
                    AmwellLog.e("updateInsurance", "onResponse: "+ sdkError.getSDKErrorReason().toString());
                    showInsuranceNotVerifiedDialog();
                }
            } else {
                showCostSummaryFragment();
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            String errorMessage= (null!=throwable)?throwable.getMessage():"null";
            AmwellLog.i("updateInsurance", "onFailure: "+errorMessage);
            ((THSInsuranceDetailFragment) mTHSBaseFragment).doTagging(ANALYTICS_UPDATE_HEALTH_PLAN, errorMessage, false);
            showInsuranceNotVerifiedDialog();
        }

    }
    ///////// start update suscription call back


    private void showCostSummaryFragment() {
        AmwellLog.i("updateInsurance", "success");
        if (((THSInsuranceDetailFragment) mTHSBaseFragment).isLaunchedFromCostSummary) {
            mTHSBaseFragment.getActivity().getSupportFragmentManager().popBackStack(THSCostSummaryFragment.TAG, 0);
        } else {
            THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            //fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment, THSCostSummaryFragment.TAG, null, true);
        }
    }

    protected void showInsuranceNotVerifiedDialog() {
        THSInsuranceNotVerifiedDialogFragment thsInsuranceNotVerifiedDialogFragment = new THSInsuranceNotVerifiedDialogFragment();
        thsInsuranceNotVerifiedDialogFragment.setPresenter(this);
        thsInsuranceNotVerifiedDialogFragment.show(mTHSBaseFragment.getFragmentManager(), THSInsuranceNotVerifiedDialogFragment.TAG);
    }

}
