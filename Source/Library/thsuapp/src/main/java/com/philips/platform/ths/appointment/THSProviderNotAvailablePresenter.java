package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class THSProviderNotAvailablePresenter implements THSBasePresenter{
    THSBaseFragment mThsBaseFragment;

    THSProviderNotAvailablePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.calendar_view) {
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSProviderNotAvailableFragment)mThsBaseFragment).setDate(date);

                   launchProviderDetailsBasedOnAvailibilty(((THSProviderNotAvailableFragment)mThsBaseFragment).getPractice(),
                           ((THSProviderNotAvailableFragment)mThsBaseFragment).mDate,((THSProviderNotAvailableFragment)mThsBaseFragment).getThsProviderEntity());

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }

    private void launchProviderDetailsBasedOnAvailibilty(final Practice practice, final Date date, final THSProviderEntity thsProviderEntity) {
        try {
            THSManager.getInstance().getAvailableProvidersBasedOnDate(mThsBaseFragment.getContext(), practice, null, null, date, null, new THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError>() {
                @Override
                public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError) {
                    if(availableProviders.getAvailableProvidersList()==null || availableProviders.getAvailableProvidersList().size()==0){
                        ((THSProviderNotAvailableFragment)mThsBaseFragment).updateProviderDetails(availableProviders);
                    }else {
                        final THSBasePresenterHelper thsBasePresenterHelper = new THSBasePresenterHelper();
                        thsBasePresenterHelper.launchAvailableProviderDetailFragment(mThsBaseFragment,thsProviderEntity,date,practice);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

}
