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
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.EditText;

import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_VITALS;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

public class THSVitalsPresenter implements THSBasePresenter, THSVitalSDKCallback<THSVitals, THSSDKError>, THSUpdateVitalsCallBack {
    private THSBaseFragment mPthBaseFragment;
    private THSVItalsUIInterface thsvItalsUIInterface;

    public THSVitalsPresenter(THSVItalsUIInterface thsvItalsUIInterface, THSVitalsFragment thsVitalsFragment) {
        mPthBaseFragment = thsVitalsFragment;
        this.thsvItalsUIInterface = thsvItalsUIInterface;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.vitals_continue_btn) {
            thsvItalsUIInterface.updateVitalsData();

            try {
                THSManager.getInstance().updateVitals(mPthBaseFragment.getContext(), thsvItalsUIInterface.getTHSVitals(), this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }

        } else if (componentID == R.id.vitals_skip) {
            thsvItalsUIInterface.launchMedicationFragment();
        }
    }

    public void getVitals() throws AWSDKInstantiationException {
        THSManager.getInstance().getVitals(mPthBaseFragment.getFragmentActivity(), this);
    }

    @Override
    public void onResponse(THSVitals thsVitals, THSSDKError var2) {
        if (null != mPthBaseFragment && mPthBaseFragment.isFragmentAttached()) {
            thsvItalsUIInterface.updateUI(thsVitals);
        }
    }

    @Override
    public void onFailure(Throwable var1) {
        if (null != mPthBaseFragment && mPthBaseFragment.isFragmentAttached()) {
            mPthBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }

    @Override
    public void onUpdateVitalsValidationFailure(Map<String, ValidationReason> map) {
        if (null != mPthBaseFragment && mPthBaseFragment.isFragmentAttached()) {
            mPthBaseFragment.showToast("Vitals Validation Failure");
        }
    }

    @Override
    public void onUpdateVitalsResponse(SDKError sdkError) {
        if (null != mPthBaseFragment && mPthBaseFragment.isFragmentAttached()) {
            if (sdkError == null) {
                tagSuccess();
                thsvItalsUIInterface.launchMedicationFragment();
                mPthBaseFragment.showToast("UPDATE SUCCESS");
            } else if (null != sdkError) {
                mPthBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_UPDATE_VITALS, sdkError));
            }
        }
    }

    @Override
    public void onUpdateVitalsFailure(Throwable throwable) {
        if (null != mPthBaseFragment && mPthBaseFragment.isFragmentAttached()) {
            mPthBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }

    public boolean checkIfValueEntered(EditText editText) {
        String ed_text = editText.getText().toString().trim();
        return !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals(""));
    }


    private void tagSuccess() {
        THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "step2VitalsAdded");

    }

    int stringToInteger(String value) {
        if (null == value || value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    Double stringToDouble(String value) {
        if (null == value || value.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }


    boolean isTextValid(EditText editText) {
        String ed_text = editText.getText().toString().trim();
        return !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null);
    }

    String getTextFromEditText(EditText editText) {
        return editText.getText().toString();
    }
}
