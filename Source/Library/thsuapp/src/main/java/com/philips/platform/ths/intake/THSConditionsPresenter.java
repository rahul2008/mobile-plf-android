package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.PTHManager;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class THSConditionsPresenter implements THSBasePresenter, THSConditionsCallBack<THSConditions,THSSDKError>, THSUpdateConditionsCallback<Void,THSSDKError> {
    THSBaseFragment mTHSBaseFragment;

    public THSConditionsPresenter(THSBaseFragment THSBaseFragment) {
        this.mTHSBaseFragment = THSBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            try {
                PTHManager.getInstance().updateConditions(mTHSBaseFragment.getContext(),PTHManager.getInstance().getPTHConsumer(),((THSConditionsFragment) mTHSBaseFragment).getTHSConditions(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            launchFollowUpFragment();
        }else if(componentID == R.id.conditions_skip){
            launchFollowUpFragment();
        }
    }

    private void launchFollowUpFragment() {
        mTHSBaseFragment.addFragment(new THSFollowUpFragment(),THSFollowUpFragment.TAG,null);
    }

    public void getConditions() throws AWSDKInstantiationException {
        PTHManager.getInstance().getConditions(mTHSBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSConditions thsConditions, THSSDKError THSSDKError) {
        final List<Condition> conditions = thsConditions.getConditions();

        List<PTHConditions> pthConditionsList = new ArrayList<>();
        for (Condition condition : conditions) {
            PTHConditions pthConditions = new PTHConditions();
            pthConditions.setCondition(condition);
            pthConditionsList.add(pthConditions);
        }


        ((THSConditionsFragment) mTHSBaseFragment).setConditions(pthConditionsList);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTHSBaseFragment.showToast("Conditions Failed");
    }


    @Override
    public void onUpdateConditonResponse(Void aVoid, THSSDKError sdkError) {

    }

    @Override
    public void onUpdateConditionFailure(Throwable throwable) {

    }
}
