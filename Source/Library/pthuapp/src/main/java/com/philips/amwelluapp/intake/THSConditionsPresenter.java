package com.philips.amwelluapp.intake;

import android.widget.Toast;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.List;

public class THSConditionsPresenter implements PTHBasePresenter, THSConditionsCallBack<THSConditions,PTHSDKError> {
    PTHBaseFragment mPthBaseFragment;

    public THSConditionsPresenter(PTHBaseFragment pthBaseFragment) {
        this.mPthBaseFragment = pthBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
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
        ((THSConditionsFragment)mPthBaseFragment).setConditions(conditions);
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(mPthBaseFragment.getContext(),"Conditions Failed", Toast.LENGTH_SHORT).show();
    }
}
