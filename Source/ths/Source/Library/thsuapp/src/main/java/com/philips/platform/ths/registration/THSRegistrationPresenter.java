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
import com.philips.platform.ths.utility.THSDateUtils;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_ENROLLMENT_MANGER;

public class THSRegistrationPresenter implements THSBasePresenter, THSSDKValidatedCallback <THSConsumerWrapper, SDKError>{

    private THSBaseFragment mTHSBaseFragment;
    private static String NAME_REGEX = "[A-Z0-9a-z\\s]{2,25}";

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
            if (null!=sdkPasswordError && sdkPasswordError.getSDKErrorReason() != null) {
                mTHSBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_ENROLLMENT_MANGER,sdkPasswordError));
                return;
            }

            switch (((THSRegistrationFragment) mTHSBaseFragment).mLaunchInput) {
                case THSConstants.THS_PRACTICES:
                    mTHSBaseFragment.popSelfBeforeTransition();
                    mTHSBaseFragment.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, true);
                    break;
                case THSConstants.THS_SCHEDULED_VISITS:
                    mTHSBaseFragment.popSelfBeforeTransition();
                    mTHSBaseFragment.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                case THSConstants.THS_VISITS_HISTORY:
                    mTHSBaseFragment.popSelfBeforeTransition();
                    mTHSBaseFragment.addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                default:
                    mTHSBaseFragment.popSelfBeforeTransition();
                    mTHSBaseFragment.addFragment(new THSWelcomeFragment(), THSWelcomeFragment.TAG, null, true);
                    break;
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(null!=mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();

                mTHSBaseFragment.showError(mTHSBaseFragment.getString(R.string.ths_se_server_error_toast_message));

        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        if(null!=mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            ((THSRegistrationFragment) mTHSBaseFragment).mContinueButton.hideProgressIndicator();
            mTHSBaseFragment.showError(mTHSBaseFragment.getString(R.string.validation_failed) + var1.toString());
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

    public boolean validateName(String nameString,boolean isFirstName){

        Pattern pattern = Pattern.compile(NAME_REGEX);

        if(isFirstName){
            if(nameString.isEmpty() || nameString.length() < 2){
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_first_name_validation_not_more_two_characters));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_first_name_validation_not_more_two_characters),false);
                return false;
            }else if(!pattern.matcher(nameString).matches()){
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_first_name_validation_only_alphabets));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_first_name_validation_only_alphabets),false);
                return false;
            }else {
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_first_name_validation_not_more_than_25_characters));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_first_name_validation_not_more_than_25_characters),false);
                return nameString.length() < 25;
            }
        } else {
            if(nameString.isEmpty() || nameString.length() < 2){
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_last_name_validation_not_more_two_characters));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_last_name_validation_not_more_two_characters),false);
                return false;
            }else if(!pattern.matcher(nameString).matches()){
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_last_name_validation_only_alphabets));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_last_name_validation_only_alphabets),false);
                return false;
            }else {
                ((THSRegistrationFragment) mTHSBaseFragment).setErrorString(mTHSBaseFragment.getString(R.string.ths_registration_last_name_validation_not_more_than_25_characters));
                ((THSRegistrationFragment) mTHSBaseFragment).doTagging(ANALYTICS_ENROLLMENT_MANGER,((THSRegistrationFragment) mTHSBaseFragment).getString(R.string.ths_registration_last_name_validation_not_more_than_25_characters),false);
                return nameString.length() < 25;
            }
        }

    }

    public boolean validateDOB(Date dob){
        if(!THSManager.getInstance().getThsConsumer(mTHSBaseFragment.getContext()).isDependent()) {
            if (null != dob) {
                int years = THSDateUtils.getDiffYears(dob, new Date(System.currentTimeMillis()));
                return years >= 18;
            } else {
                return false;
            }
        }else {
            return true;
        }

    }

    public boolean validateLocation(String text) {
        if(null != text){
            return (text.length() < 0 || text.isEmpty());
        }else {
            return false;
        }
    }
}
