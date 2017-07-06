package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.Map;

public class THSVitalsPresenter implements PTHBasePresenter, THSVitalSDKCallback<THSVitals, PTHSDKError>,THSUpdateVitalsCallBack{
    PTHBaseFragment mPthBaseFragment;

    public THSVitalsPresenter(THSVitalsFragment thsVitalsFragment) {
        mPthBaseFragment = thsVitalsFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            ((THSVitalsFragment)mPthBaseFragment).setVitalsValues();
            try {
                PTHManager.getInstance().updateVitals(mPthBaseFragment.getContext(),((THSVitalsFragment) mPthBaseFragment).getTHSVitals(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            launchMedicationFragment();
        }else if(componentID == R.id.vitals_skip){
            launchMedicationFragment();
        }
    }

    private void launchMedicationFragment() {
        mPthBaseFragment.addFragment(new PTHMedicationFragment(),PTHMedicationFragment.TAG,null);
    }

    public void getVitals() throws AWSDKInstantiationException {
        PTHManager.getInstance().getVitals(mPthBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSVitals thsVitals, PTHSDKError var2) {
        ((THSVitalsFragment)mPthBaseFragment).updateUI(thsVitals);
    }

    @Override
    public void onFailure(Throwable var1) {

    }

    @Override
    public void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map) {
        mPthBaseFragment.showToast("VitalsValidationFailure");
    }

    @Override
    public void onUpdateVitalsResponse(SDKError sdkError) {
        if (sdkError == null)
            mPthBaseFragment.showToast("UPDATE SUCCESS");
        else
            mPthBaseFragment.showToast("UPDATE FAILED");
    }

    @Override
    public void onUpdateVitalsFailure(Throwable throwable) {
        mPthBaseFragment.showToast("onUpdateVitalsFailure throwable");
    }
}
