package com.philips.amwelluapp.intake;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

public class THSVitalsPresenter implements PTHBasePresenter, THSVitalSDKCallback<THSVitals, PTHSDKError>{
    PTHBaseFragment mPthBaseFragment;
    PTHVisitContext mPthVisitContext;

    public THSVitalsPresenter(THSVitalsFragment thsVitalsFragment, PTHVisitContext pthVisitContext) {
        mPthBaseFragment = thsVitalsFragment;
        mPthVisitContext = pthVisitContext;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            launchMedicationFragment();
        }else if(componentID == R.id.vitals_skip){
            launchMedicationFragment();
        }
    }

    private void launchMedicationFragment() {
        mPthBaseFragment.addFragment(new PTHMedicationFragment(),PTHMedicationFragment.TAG,null);
    }

    public void getVitals() throws AWSDKInstantiationException {
        PTHManager.getInstance().getVitals(mPthBaseFragment.getFragmentActivity(),mPthVisitContext,this);
    }

    @Override
    public void onResponse(THSVitals thsVitals, PTHSDKError var2) {
        ((THSVitalsFragment)mPthBaseFragment).updateUI(thsVitals);
    }

    @Override
    public void onFailure(Throwable var1) {
    }
}
