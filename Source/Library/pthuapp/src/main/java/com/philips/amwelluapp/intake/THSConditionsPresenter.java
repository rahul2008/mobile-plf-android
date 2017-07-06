package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.ArrayList;
import java.util.List;

public class THSConditionsPresenter implements PTHBasePresenter, THSConditionsCallBack<THSConditions,PTHSDKError>, THSUpdateConditionsCallback<Void,PTHSDKError> {
    PTHBaseFragment mPthBaseFragment;

    public THSConditionsPresenter(PTHBaseFragment pthBaseFragment) {
        this.mPthBaseFragment = pthBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            try {
                PTHManager.getInstance().updateConditions(mPthBaseFragment.getContext(),PTHManager.getInstance().getPTHConsumer(),((THSConditionsFragment)mPthBaseFragment).getTHSConditions(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            launchFollowUpFragment();
        }else if(componentID == R.id.conditions_skip){
            launchFollowUpFragment();
        }
    }

    private void launchFollowUpFragment() {
        mPthBaseFragment.addFragment(new THSFollowUpFragment(),THSFollowUpFragment.TAG,null);
    }

    public void getConditions() throws AWSDKInstantiationException {
        PTHManager.getInstance().getConditions(mPthBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSConditions thsConditions, PTHSDKError pthsdkError) {
        final List<Condition> conditions = thsConditions.getConditions();

        List<PTHConditions> pthConditionsList = new ArrayList<>();
        for (Condition condition : conditions) {
            PTHConditions pthConditions = new PTHConditions();
            pthConditions.setCondition(condition);
            pthConditionsList.add(pthConditions);
        }


        ((THSConditionsFragment)mPthBaseFragment).setConditions(pthConditionsList);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mPthBaseFragment.showToast("Conditions Failed");
    }


    @Override
    public void onUpdateConditonResponse(Void aVoid, PTHSDKError sdkError) {

    }

    @Override
    public void onUpdateConditionFailure(Throwable throwable) {

    }
}
