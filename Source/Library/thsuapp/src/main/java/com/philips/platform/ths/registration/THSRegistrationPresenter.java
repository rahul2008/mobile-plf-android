/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class THSRegistrationPresenter implements THSBasePresenter, THSSDKValidatedCallback <THSConsumerWrapper, SDKPasswordError>{

    private THSBaseFragment mTHSBaseFragment;

    THSRegistrationPresenter(THSBaseFragment thsBaseFragment){
        mTHSBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_edit_dob){
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mTHSBaseFragment, THSDateEnum.HIDEFUTUREDATE);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSRegistrationFragment)mTHSBaseFragment).updateDobView(date);

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }

    @Override
    public void onResponse(THSConsumerWrapper thsConsumerWrapper, SDKPasswordError sdkPasswordError) {
        ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
        if(sdkPasswordError!=null){
            mTHSBaseFragment.showToast(sdkPasswordError.getSDKErrorReason().name());
            return;
        }
        mTHSBaseFragment.addFragment(new THSWelcomeFragment(), THSWelcomeFragment.TAG,null, true);
    }

    @Override
    public void onFailure(Throwable throwable) {
        ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
        final String localizedMessage = throwable.getLocalizedMessage();
        if(localizedMessage == null){
            mTHSBaseFragment.showToast(mTHSBaseFragment.getString(R.string.something_went_wrong));
        }else {
            mTHSBaseFragment.showToast(localizedMessage);
        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
        mTHSBaseFragment.showToast(mTHSBaseFragment.getString(R.string.validation_failed) + var1.toString());
    }

    public void enrollUser(Date date, String firstname, String lastname, Gender gender, State state) {
        try {
            THSManager.getInstance().enrollConsumer(null, date, firstname, lastname, gender, state, this, mTHSBaseFragment.getContext());
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }
}
