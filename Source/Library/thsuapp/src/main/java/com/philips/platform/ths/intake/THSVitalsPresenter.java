package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.EditText;

import java.util.Map;

public class THSVitalsPresenter implements THSBasePresenter, THSVitalSDKCallback<THSVitals, THSSDKError>,THSUpdateVitalsCallBack{
    THSBaseFragment mPthBaseFragment;

    public THSVitalsPresenter(THSVitalsFragment thsVitalsFragment) {
        mPthBaseFragment = thsVitalsFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            boolean isValidinput = ((THSVitalsFragment)mPthBaseFragment).validate();
            if(!isValidinput){
                return;
            }
            ((THSVitalsFragment)mPthBaseFragment).setVitalsValues();
            try {
                THSManager.getInstance().updateVitals(mPthBaseFragment.getContext(),((THSVitalsFragment) mPthBaseFragment).getTHSVitals(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            launchMedicationFragment();
        }else if(componentID == R.id.vitals_skip){
            launchMedicationFragment();
        }
    }

    private void launchMedicationFragment() {

        final THSMedicationFragment fragment = new THSMedicationFragment();
        fragment.setFragmentLauncher(mPthBaseFragment.getFragmentLauncher());
        mPthBaseFragment.addFragment(fragment,THSMedicationFragment.TAG,null);
    }

    public void getVitals() throws AWSDKInstantiationException {
        THSManager.getInstance().getVitals(mPthBaseFragment.getFragmentActivity(),this);
    }

    @Override
    public void onResponse(THSVitals thsVitals, THSSDKError var2) {
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

    int stringToInteger(String value){
        return Integer.parseInt(value);
    }

    String integerToString(int value){
        return String.valueOf(value);
    }

    Double stringToDouble(String value){
        return Double.parseDouble(value);
    }

    String doubleToString(Double value){
        return String.valueOf(value);
    }

    boolean isTextValid(EditText editText){
        if(editText.getText().toString()==null || editText.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    String getTextFromEditText(EditText editText) {
        return editText.getText().toString();
    }
}
