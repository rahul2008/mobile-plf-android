package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by philips on 7/11/17.
 */

public class THSInsuranceDetailPresenter implements THSBasePresenter, THSInsuranceCallback.THSSDKCallBack<THSSubscription, THSSDKError>, THSSDKValidatedCallback<Void , SDKError> {
    THSBaseFragment mTHSBaseFragment;
    boolean hasInsurance;

    public THSInsuranceDetailPresenter(THSInsuranceDetailFragment tHSInsuranceDetailFraagment) {
        this.mTHSBaseFragment = tHSInsuranceDetailFraagment;

    }


    public THSHealthPlan fetchHealthPlanList() {
        THSHealthPlan tHSHealthPlan = new THSHealthPlan();
        try {
            List<HealthPlan> healthPlanList = THSManager.getInstance().getHealthPlans(mTHSBaseFragment.getFragmentActivity());
            tHSHealthPlan.setHealthPlanList(healthPlanList);
        } catch (Exception e) {

        }
        return tHSHealthPlan;
    }

    public THSRelationship fetchSubscriberRelationList() {
        THSRelationship tHSRelationship = new THSRelationship();
        try {
            List<Relationship> relationships = THSManager.getInstance().getSubscriberRelationships(mTHSBaseFragment.getFragmentActivity());
            tHSRelationship.setRelationShipList(relationships);
        } catch (Exception e) {

        }
        return tHSRelationship;
    }

    public THSSubscriptionUpdateRequest getSubscriptionUpdateRequestWithoutVistContext() {
        THSSubscriptionUpdateRequest tHSSubscriptionUpdateRequest=null;
        try {
            tHSSubscriptionUpdateRequest = THSManager.getInstance().getNewSubscriptionUpdateRequest(mTHSBaseFragment.getFragmentActivity());
        } catch (Exception e) {
        }
        return  tHSSubscriptionUpdateRequest;
    }


    void fetchExistingSubscription() {
        try {
            THSManager.getInstance().getExistingSubscription(mTHSBaseFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }

    void updateTHSInsuranceSubscription(){
        try {
            THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest = getSubscriptionUpdateRequestWithoutVistContext();
            final Subscription subscription = thsSubscriptionUpdateRequest.getSubscriptionUpdateRequest().getSubscription();
            subscription.setHealthPlan(((THSInsuranceDetailFragment) mTHSBaseFragment).mHealthPlan);
            subscription.setSubscriberId(((THSInsuranceDetailFragment) mTHSBaseFragment).subscriptionIDEditBox.getText().toString().trim());
            //subscription.setSubscriberSuffix(((THSInsuranceDetailFragment) mTHSBaseFragment).mSubscriberSuffix);
            subscription.setSubscriberSuffix("12");// todo as above
            Relationship relationship = null;
            if(!((THSInsuranceDetailFragment) mTHSBaseFragment).mNotPrimarySubscriberCheckBox.isChecked()){
                relationship = ((THSInsuranceDetailFragment) mTHSBaseFragment).mTHSRelationshipList.getRelationShipList().get(0);// primary subscriber by default

            } else {
                relationship = ((THSInsuranceDetailFragment) mTHSBaseFragment).mInsuranceRelationship;
            }
            subscription.setRelationship(relationship);
            if (relationship != null && !relationship.isPrimarySubscriber()) {
                subscription.setPrimarySubscriberFirstName(((THSInsuranceDetailFragment) mTHSBaseFragment).firstNameEditBox.getText().toString().trim());
                subscription.setPrimarySubscriberLastName(((THSInsuranceDetailFragment) mTHSBaseFragment).lastNameEditBox.getText().toString().trim());
                subscription.setPrimarySubscriberDateOfBirth(SDKLocalDate.valueOf(((THSInsuranceDetailFragment) mTHSBaseFragment).relationDOBEditBox.getText().toString().trim()));
            }


            Map<String, ValidationReason> errors = new HashMap<>();
            THSManager.getInstance().validateSubscriptionUpdateRequest(mTHSBaseFragment.getFragmentActivity(),thsSubscriptionUpdateRequest,errors);
            if (errors.isEmpty()) {
                THSManager.getInstance().updateInsuranceSubscription(mTHSBaseFragment.getFragmentActivity(), thsSubscriptionUpdateRequest, this);
            }else{

                AmwellLog.i("updateInsurance","validateSubscriptionUpdateRequest error "+errors.toString());
            }
        } catch (Exception e) {

        }

    }

    public  THSSubscription getCurrentSubscription() {
        THSSubscription thsSubscription = new THSSubscription();
        final Subscription currentSubscription = THSManager.getInstance().getPTHConsumer().getConsumer().getSubscription();
        thsSubscription.setSubscription(currentSubscription);
        return thsSubscription;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_insurance_detail_skip_button){
            // skip insurance update
            final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment,THSCostSummaryFragment.TAG,null);
        } else if (componentID == R.id.ths_insurance_detail_continue_button){
            updateTHSInsuranceSubscription();
        }

    }



    ////////// start of getExistingSubscription call back
    @Override
    public void onResponse(THSSubscription tHSSubscription, THSSDKError tHSSDKError) {
        ((THSInsuranceDetailFragment) mTHSBaseFragment).hideProgressBar();
        ((THSInsuranceDetailFragment) mTHSBaseFragment).thsSubscriptionExisting = tHSSubscription;
        Subscription subscription = tHSSubscription.getSubscription();
        if(null!=subscription) {
            if(subscription.getHealthPlan()!=null){
                ((THSInsuranceDetailFragment) mTHSBaseFragment).mHealthPlan=subscription.getHealthPlan();
                ((THSInsuranceDetailFragment) mTHSBaseFragment).insuranceEditBox.setText(subscription.getHealthPlan().getName());
            }
            if(subscription.getSubscriberId()!=null){
                ((THSInsuranceDetailFragment) mTHSBaseFragment).subscriptionIDEditBox.setText(subscription.getSubscriberId());
            }
            if(subscription.getRelationship()!=null){
               ((THSInsuranceDetailFragment) mTHSBaseFragment).mInsuranceRelationship=subscription.getRelationship();
                ((THSInsuranceDetailFragment) mTHSBaseFragment).relationshipEditBox.setText(subscription.getRelationship().getName());
            }
            if(subscription.getPrimarySubscriberFirstName()!=null){
                ((THSInsuranceDetailFragment) mTHSBaseFragment).firstNameEditBox.setText(subscription.getPrimarySubscriberFirstName());
            }
            if(subscription.getPrimarySubscriberLastName()!=null){
                ((THSInsuranceDetailFragment) mTHSBaseFragment).lastNameEditBox.setText(subscription.getPrimarySubscriberLastName());
            }
            if(subscription.getPrimarySubscriberDateOfBirth()!=null){
                ((THSInsuranceDetailFragment) mTHSBaseFragment).relationDOBEditBox.setText(subscription.getPrimarySubscriberDateOfBirth().toString());
            }

        }
    }



    @Override
    public void onFailure(Throwable throwable) {
        ((THSInsuranceDetailFragment) mTHSBaseFragment).hideProgressBar();

    }
    ////////// end of getExistingSubscription call back




    ///////// start update suscription call back
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        ((THSInsuranceDetailFragment) mTHSBaseFragment).hideProgressBar();
        AmwellLog.i("updateInsurance","fail");
    }

    @Override
    public void onResponse(Void aVoid, SDKError sdkError) {
        ((THSInsuranceDetailFragment) mTHSBaseFragment).hideProgressBar();
        AmwellLog.i("updateInsurance","success");
        final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
        fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
        mTHSBaseFragment.addFragment(fragment,THSCostSummaryFragment.TAG,null);
    }
    ///////// start update suscription call back

}
