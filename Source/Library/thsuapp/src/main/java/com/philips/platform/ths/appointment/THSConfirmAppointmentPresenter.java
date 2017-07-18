package com.philips.platform.ths.appointment;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

public class THSConfirmAppointmentPresenter implements THSBasePresenter, THSSDKValidatedCallback {
    THSBaseFragment mTHSBaseFragment;
    THSAppointmentInterface mThsAppointmentInterface;

    THSConfirmAppointmentPresenter(THSBaseFragment thsBaseFragment, THSAppointmentInterface thsAppointmentInterface){
        mTHSBaseFragment = thsBaseFragment;
        mThsAppointmentInterface = thsAppointmentInterface;
    }
    @Override
    public void onEvent(int componentID) {

    }

    public void scheduleAppointment() throws AWSDKInstantiationException {
        THSManager.getInstance().scheduleAppointment(mTHSBaseFragment.getContext(),mThsAppointmentInterface.getTHSProviderInfo(),
                mThsAppointmentInterface.getAppointmentDate(),this);
    }

    @Override
    public void onValidationFailure(Map var1) {
        mTHSBaseFragment.showToast("VALIDATION FAILED");
    }

    @Override
    public void onResponse(Object o, SDKError sdkError) {
        mTHSBaseFragment.showToast("ONSUCCESS");
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTHSBaseFragment.showToast("ON FAIl");
    }
}
