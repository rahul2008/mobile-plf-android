/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER_LIST;

public class THSAvailableProviderListBasedOnDatePresenter implements THSBasePresenter, THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> {
    private THSBaseFragment mThsBaseFragment;
    private OnDateSetChangedInterface onDateSetChangedInterface;

    THSAvailableProviderListBasedOnDatePresenter(THSBaseFragment thsBaseFragment,OnDateSetChangedInterface onDateSetChangedInterface){
        mThsBaseFragment = thsBaseFragment;
        this.onDateSetChangedInterface = onDateSetChangedInterface;
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
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            if (sdkError.getSdkError() != null) {
                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PROVIDER_LIST,sdkError.getSdkError()));
            }else {
                mThsBaseFragment.showToast("Available Providers list Success");
                if(mThsBaseFragment instanceof THSProviderNotAvailableFragment){
                    ((THSProviderNotAvailableFragment)mThsBaseFragment).updateProviderDetails(availableProviders);
                }
                ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).updateProviderAdapterList(availableProviders);
            }
        }

    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
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
