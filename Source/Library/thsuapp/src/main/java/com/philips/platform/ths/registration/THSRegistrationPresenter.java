/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_ENROLLMENT_MANGER;

public class THSRegistrationPresenter implements THSBasePresenter, THSSDKValidatedCallback <THSConsumerWrapper, SDKError>{

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
    public void onResponse(THSConsumerWrapper thsConsumerWrapper, SDKError sdkPasswordError) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
            if (sdkPasswordError.getSDKErrorReason() != null) {
                mTHSBaseFragment.showToast(THSSDKErrorFactory.getErrorType(ANALYTICS_ENROLLMENT_MANGER,sdkPasswordError));
                return;
            }
            switch (((THSRegistrationFragment) mTHSBaseFragment).mLaunchInput) {
                case THSConstants.THS_PRACTICES:
                    mTHSBaseFragment.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, true);
                    break;
                case THSConstants.THS_SCHEDULED_VISITS:
                    mTHSBaseFragment.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                case THSConstants.THS_VISITS_HISTORY:
                    mTHSBaseFragment.addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                default:
                    mTHSBaseFragment.addFragment(new THSWelcomeFragment(), THSWelcomeFragment.TAG, null, true);
                    break;
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(null!=mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();

                mTHSBaseFragment.showToast(R.string.ths_se_server_error_toast_message);

        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        if(null!=mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
            mTHSBaseFragment.showToast(mTHSBaseFragment.getString(R.string.validation_failed) + var1.toString());
        }
    }

    public void enrollUser(Date date, String firstname, String lastname, Gender gender, State state) {
        try {
            THSManager.getInstance().enrollConsumer(mTHSBaseFragment.getContext(), date,firstname,lastname,gender,state,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void enrollDependent(Date date, String firstname, String lastname, Gender gender, State state) {
        try {
            THSManager.getInstance().enrollDependent(mTHSBaseFragment.getContext(), date, firstname, lastname, gender, state, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }
}
