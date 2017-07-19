package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Date;

public class THSAvailableProviderListBasedOnDatePresenter implements THSBasePresenter, THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> {
    THSBaseFragment mThsBaseFragment;

    THSAvailableProviderListBasedOnDatePresenter(THSBaseFragment thsBaseFragment){
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
                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);

        }
    }

    @Override
    public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError) {
        mThsBaseFragment.showToast("Available Providers list Success");
        if(mThsBaseFragment instanceof THSProviderNotAvailableFragment){
            ((THSProviderNotAvailableFragment)mThsBaseFragment).updateProviderDetails(mThsBaseFragment.getView());
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY,thsProviderInfo);
        bundle.putSerializable(THSConstants.THS_DATE,date);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practice);
        THSAvailableProviderDetailFragment fragment = new THSAvailableProviderDetailFragment();
        fragment.setTHSProviderEntity(thsProviderInfo);
        mThsBaseFragment.addFragment(fragment, THSAvailableProviderDetailFragment.TAG,bundle);
    }
}
