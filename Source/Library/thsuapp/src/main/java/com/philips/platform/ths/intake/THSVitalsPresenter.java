package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.PTHManager;

import java.util.Map;

public class THSVitalsPresenter implements THSBasePresenter, THSVitalSDKCallback<THSVitals, THSSDKError>,THSUpdateVitalsCallBack{
    THSBaseFragment mTHSBaseFragment;

    public THSVitalsPresenter(THSVitalsFragment thsVitalsFragment) {
        mTHSBaseFragment = thsVitalsFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            ((THSVitalsFragment) mTHSBaseFragment).setVitalsValues();
            try {
                PTHManager.getInstance().updateVitals(mTHSBaseFragment.getContext(),((THSVitalsFragment) mTHSBaseFragment).getTHSVitals(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            launchMedicationFragment();
        }else if(componentID == R.id.vitals_skip){
            launchMedicationFragment();
        }
    }

    private void launchMedicationFragment() {
        mTHSBaseFragment.addFragment(new THSMedicationFragment(), THSMedicationFragment.TAG,null);
    }

    public void getVitals() throws AWSDKInstantiationException {
        PTHManager.getInstance().getVitals(mTHSBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSVitals thsVitals, THSSDKError var2) {
        ((THSVitalsFragment) mTHSBaseFragment).updateUI(thsVitals);
    }

    @Override
    public void onFailure(Throwable var1) {

    }

    @Override
    public void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map) {
        mTHSBaseFragment.showToast("VitalsValidationFailure");
    }

    @Override
    public void onUpdateVitalsResponse(SDKError sdkError) {
        if (sdkError == null)
            mTHSBaseFragment.showToast("UPDATE SUCCESS");
        else
            mTHSBaseFragment.showToast("UPDATE FAILED");
    }

    @Override
    public void onUpdateVitalsFailure(Throwable throwable) {
        mTHSBaseFragment.showToast("onUpdateVitalsFailure throwable");
    }
}
