/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
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
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_APPOINTMENTS;

public class THSProviderNotAvailablePresenter implements THSBasePresenter {
    private THSBaseFragment mThsBaseFragment;

    THSProviderNotAvailablePresenter(THSBaseFragment thsBaseFragment) {
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
                    calendar.set(year, month, day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSProviderNotAvailableFragment) mThsBaseFragment).setDate(date);

                    launchProviderDetailsBasedOnAvailibilty(((THSProviderNotAvailableFragment) mThsBaseFragment).getPractice(),
                            ((THSProviderNotAvailableFragment) mThsBaseFragment).mDate, ((THSProviderNotAvailableFragment) mThsBaseFragment).getThsProviderEntity());

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }

    protected void launchProviderDetailsBasedOnAvailibilty(final Practice practice, final Date date, final THSProviderEntity thsProviderEntity) {
        try {
            THSManager.getInstance().getAvailableProvidersBasedOnDate(mThsBaseFragment.getContext(), practice, null, null, date, null, new THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError>() {
                @Override
                public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError) {
                    if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                        if (sdkError.getSdkError() != null) {
                            mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(),ANALYTICS_FETCH_APPOINTMENTS,sdkError.getSdkError()));
                        } else {

                            final THSAvailableProvider availableListContainsProviderChosen = isAvailableListContainsProviderChosen(availableProviders);
                            if (availableProviders.getAvailableProvidersList() == null || availableProviders.getAvailableProvidersList().size() == 0 || availableListContainsProviderChosen == null) {
                                ((THSProviderNotAvailableFragment) mThsBaseFragment).updateProviderDetails(availableProviders);
                            } else {
                                new THSBasePresenterHelper().launchAvailableProviderDetailFragment(mThsBaseFragment, availableListContainsProviderChosen, date, practice);
                            }

                        }
                    }
                }


                @Override
                public void onFailure(Throwable throwable) {
                    if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                        mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
                    }
                }
            });
        } catch (AWSDKInstantiationException e) {

        }
    }

    protected THSAvailableProvider isAvailableListContainsProviderChosen(THSAvailableProviderList availableProviders) {

        final List<AvailableProvider> availableProvidersList = availableProviders.getAvailableProviders().getAvailableProviders();
        final Provider providerSelected = ((THSProviderNotAvailableFragment) mThsBaseFragment).getProvider();

        for (AvailableProvider provider : availableProvidersList) {
            if (provider.getProviderInfo().getFirstName().equalsIgnoreCase(providerSelected.getFirstName())) {
                THSAvailableProvider thsAvailableProvider = new THSAvailableProvider();
                thsAvailableProvider.setAvailableProvider(provider);
                return thsAvailableProvider;
            }
        }
        return null;
    }

}
