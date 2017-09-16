/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class THSProviderNotAvailablePresenter implements THSBasePresenter{
    private THSBaseFragment mThsBaseFragment;

    THSProviderNotAvailablePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.calendar_view) {
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment, THSDateEnum.HIDEPREVDATEANDSIXMONTHSLATERDATE);

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
                    final THSAvailableProvider availableListContainsProviderChosen = isAvailableListContainsProviderChosen(availableProviders);

                    if (availableProviders.getAvailableProvidersList() == null || availableProviders.getAvailableProvidersList().size() == 0 || availableListContainsProviderChosen == null) {
                        ((THSProviderNotAvailableFragment) mThsBaseFragment).updateProviderDetails(availableProviders);
                    } else {
                        new THSBasePresenterHelper().launchAvailableProviderDetailFragment(mThsBaseFragment, availableListContainsProviderChosen, date, practice);
                    } /*else {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(THSConstants.THS_AVAILABLE_PROVIDER_LIST, availableProviders);
                        bundle.putSerializable(THSConstants.THS_DATE, date);
                        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, practice);
                        THSAvailableProviderListBasedOnDateFragment thsAvailableProviderListBasedOnDateFragment = new THSAvailableProviderListBasedOnDateFragment();
                        mThsBaseFragment.addFragment(thsAvailableProviderListBasedOnDateFragment, THSAvailableProviderListBasedOnDateFragment.TAG, bundle);
                    }*/
                }


                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    private THSAvailableProvider isAvailableListContainsProviderChosen(THSAvailableProviderList availableProviders) {

        final List<AvailableProvider> availableProvidersList = availableProviders.getAvailableProviders().getAvailableProviders();
        final Provider providerSelected = ((THSProviderNotAvailableFragment) mThsBaseFragment).getProvider();

        for (AvailableProvider provider: availableProvidersList) {
            if(provider.getProviderInfo().getFirstName().equalsIgnoreCase(providerSelected.getFirstName())){
                THSAvailableProvider thsAvailableProvider = new THSAvailableProvider();
                thsAvailableProvider.setAvailableProvider(provider);
                return thsAvailableProvider;
            }
        }
        return null;
    }

}
