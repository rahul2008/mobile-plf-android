package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;

public class THSAvailableProviderListBasedOnDatePresenter implements THSBasePresenter, THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> {
    THSBaseFragment mThsBaseFragment;
    OnDateSetChangedInterface onDateSetChangedInterface;

    THSAvailableProviderListBasedOnDatePresenter(THSBaseFragment thsBaseFragment,OnDateSetChangedInterface onDateSetChangedInterface){
        mThsBaseFragment = thsBaseFragment;
        this.onDateSetChangedInterface = onDateSetChangedInterface;
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
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).setDate(date);

                    onDateSetChangedInterface.refreshView();
                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }

    @Override
    public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError) {
        mThsBaseFragment.showToast("Available Providers list Success");
        if(mThsBaseFragment instanceof THSProviderNotAvailableFragment){
            ((THSProviderNotAvailableFragment)mThsBaseFragment).updateProviderDetails(availableProviders);
        }
        ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).updateProviderAdapterList(availableProviders);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsBaseFragment.showToast("Available Providers list Failure");
    }

    public void getAvailableProvidersBasedOnDate() throws AWSDKInstantiationException {
        THSManager.getInstance().getAvailableProvidersBasedOnDate(mThsBaseFragment.getContext(),
                ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).getPractice(),
                null,null,((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).mDate,null,this);

    }

    public void launchAvailableProviderDetailFragment(THSProviderEntity thsProviderInfo, Date date, Practice practice) {
        final THSBasePresenterHelper thsBasePresenterHelper = new THSBasePresenterHelper();
        thsBasePresenterHelper.launchAvailableProviderDetailFragment(mThsBaseFragment,thsProviderInfo,date,practice);
    }
}
