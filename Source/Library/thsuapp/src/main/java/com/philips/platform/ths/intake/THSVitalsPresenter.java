/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

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

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public class THSVitalsPresenter implements THSBasePresenter, THSVitalSDKCallback<THSVitals, THSSDKError>, THSUpdateVitalsCallBack {
    private THSBaseFragment mPthBaseFragment;
    private THSVItalsUIInterface thsvItalsUIInterface;

    public THSVitalsPresenter(THSVItalsUIInterface thsvItalsUIInterface,THSVitalsFragment thsVitalsFragment) {
        mPthBaseFragment = thsVitalsFragment;
        this.thsvItalsUIInterface = thsvItalsUIInterface;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            if(thsvItalsUIInterface.validate()){
                thsvItalsUIInterface.updateVitalsData();
                try {
                    THSManager.getInstance().updateVitals(((THSVitalsFragment)mPthBaseFragment).getConsumer(), thsvItalsUIInterface.getTHSVitals(), this, mPthBaseFragment.getContext());
                } catch (AWSDKInstantiationException e) {
                    e.printStackTrace();
                }
            }

        } else if (componentID == R.id.vitals_skip) {
            thsvItalsUIInterface.launchMedicationFragment();
        }
    }

    public void getVitals() throws AWSDKInstantiationException {
        THSManager.getInstance().getVitals(((THSVitalsFragment)mPthBaseFragment).getConsumer(), this, mPthBaseFragment.getFragmentActivity());
    }

    @Override
    public void onResponse(THSVitals thsVitals, THSSDKError var2) {
        thsvItalsUIInterface.updateUI(thsVitals);
    }

    @Override
    public void onFailure(Throwable var1) {

    }

    @Override
    public void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map) {
        mPthBaseFragment.showToast("Vitals Validation Failure");
    }

    @Override
    public void onUpdateVitalsResponse(SDKError sdkError) {
        if (sdkError == null) {
            tagSuccess();
            thsvItalsUIInterface.launchMedicationFragment();
            mPthBaseFragment.showToast("UPDATE SUCCESS");
        }
        else
            mPthBaseFragment.showToast("UPDATE FAILED");
    }

    @Override
    public void onUpdateVitalsFailure(Throwable throwable) {
        mPthBaseFragment.showToast("onUpdateVitalsFailure throwable");
    }

    public boolean checkIfValueEntered(EditText editText) {
        return !(editText.toString().isEmpty() || editText.getText().length() == 0);
    }


    private void tagSuccess(){
        HashMap<String, String> map= new HashMap<String, String>();
        map.put("step2VitalsForVisit",((THSVitalsFragment) mPthBaseFragment).tagActions);
        map.put("specialEvents","step2VitalsAdded");
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, map);

    }
    int stringToInteger(String value) {
        if(null == value || value.isEmpty()){
            return 0;
        }
        return Integer.parseInt(value);
    }

    Double stringToDouble(String value) {
        if(null == value || value.isEmpty()){
            return 0.0;
        }
        return Double.parseDouble(value);
    }


    boolean isTextValid(EditText editText) {
        if (editText.getText().toString() == null || editText.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    String getTextFromEditText(EditText editText) {
        return editText.getText().toString();
    }
}
