package com.philips.platform.ths.registration;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class THSRegistrationPresenter implements THSBasePresenter, THSSDKValidatedCallback <THSConsumer, SDKPasswordError>{

    private THSBaseFragment mTHSBaseFragment;

    THSRegistrationPresenter(THSBaseFragment thsBaseFragment){
        mTHSBaseFragment = thsBaseFragment;
    }

    private void launchPractice(THSConsumer thsConsumer) {
        THSManager.getInstance().setPTHConsumer(thsConsumer);
        AmwellLog.d("Login","Consumer object received");
        THSPracticeFragment thsPracticeFragment = new THSPracticeFragment();
        thsPracticeFragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
        thsPracticeFragment.setConsumer(thsConsumer.getConsumer());
        mTHSBaseFragment.addFragment(thsPracticeFragment,THSPracticeFragment.TAG,null);
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_edit_dob){
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mTHSBaseFragment);

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
    public void onResponse(THSConsumer thsConsumer, SDKPasswordError sdkPasswordError) {
        launchPractice(thsConsumer);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {

    }

    public void enrollUser(Date date, String firstname, String lastname, Gender gender, State state) {
        try {
            THSManager.getInstance().enrollConsumer(mTHSBaseFragment.getContext(), date,firstname,lastname,gender,state,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }
}
