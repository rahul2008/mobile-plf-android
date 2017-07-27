package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class THSConditionsPresenter implements THSBasePresenter, THSConditionsCallBack<THSConditionsList,THSSDKError>, THSUpdateConditionsCallback<Void,THSSDKError> {
    THSBaseFragment mTHSBaseFragment;

    public THSConditionsPresenter(THSBaseFragment THSBaseFragment) {
        this.mTHSBaseFragment = THSBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            try {
                THSManager.getInstance().updateConditions(mTHSBaseFragment.getContext(), ((THSConditionsFragment) mTHSBaseFragment).getTHSConditions(),this);
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
        THSManager.getInstance().getConditions(mTHSBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSConditionsList thsConditions, THSSDKError THSSDKError) {
        final List<Condition> conditions = thsConditions.getConditions();

        List<THSConditions> THSConditionsList = new ArrayList<>();
        for (Condition condition : conditions) {
            THSConditions THSConditions = new THSConditions();
            THSConditions.setCondition(condition);
            THSConditionsList.add(THSConditions);
        }


        ((THSConditionsFragment) mTHSBaseFragment).setConditions(THSConditionsList);
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
