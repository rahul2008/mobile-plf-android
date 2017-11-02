/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.providerdetails.THSFetchEstimatedCostCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.americanwell.sdk.entity.SDKErrorReason.GENERIC_EXCEPTION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_APPOINTMENTS;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SCHEDULE_APPOINTMENT;

class THSAvailableProviderDetailPresenter implements THSBasePresenter, THSProviderDetailsCallback, THSAvailableProviderCallback<List<Date>, THSSDKError>, THSFetchEstimatedCostCallback, THSSDKValidatedCallback {
    private THSBaseFragment mThsBaseFragment;
    private THSProviderDetailsDisplayHelper mthsProviderDetailsDisplayHelper;
    List<Date> dateList;
    private int position;

    THSAvailableProviderDetailPresenter(THSBaseFragment thsBaseFragment, THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelper) {
        mThsBaseFragment = thsBaseFragment;
        mthsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelper;
        dateList = new ArrayList<>();
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.calendar_container) {

            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment, THSDateEnum.HIDEPREVDATEANDSIXMONTHSLATERDATE);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSAvailableProviderDetailFragment) mThsBaseFragment).setDate(date);

                    launchAvailableProviderDetailBasedOnAvailibity();

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
        if (componentID == R.id.set_reminder_layout) {
            mthsProviderDetailsDisplayHelper.launchSetRemainderDialogFragment(mThsBaseFragment);
        }

    }

    @SuppressWarnings("unchecked")
    private void launchAvailableProviderDetailBasedOnAvailibity() {
        try {
            THSManager.getInstance().getProviderDetails(mThsBaseFragment.getContext(),
                    ((THSAvailableProviderDetailFragment) mThsBaseFragment).getTHSProviderInfo(), new THSProviderDetailsCallback() {
                        @Override
                        public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
                            ((THSAvailableProviderDetailFragment) mThsBaseFragment).setProvider(provider);
                            try {
                                THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                                        ((THSAvailableProviderDetailFragment) mThsBaseFragment).getDate(), new THSAvailableProviderCallback<List<Date>, THSSDKError>() {
                                            @Override
                                            public void onResponse(List<Date> dates, THSSDKError sdkError) {
                                                if (sdkError.getSdkError() != null) {
                                                    mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_APPOINTMENTS,sdkError.getSdkError()));
                                                } else {
                                                    if (dates == null || dates.size() == 0) {

                                                        final THSProviderNotAvailableFragment fragment = new THSProviderNotAvailableFragment();
                                                        fragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable(THSConstants.THS_DATE, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getDate());
                                                        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getPractice());
                                                        bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider());
                                                        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProviderEntitiy());
                                                        mThsBaseFragment.addFragment(fragment, THSProviderNotAvailableFragment.TAG, bundle, true);
                                                        mThsBaseFragment.hideProgressBar();
                                                    } else {
                                                        dateList = dates;
                                                        mthsProviderDetailsDisplayHelper.updateView(((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider(), dates);
                                                        mThsBaseFragment.hideProgressBar();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable throwable) {
                                                if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                                                    mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
                                                    mThsBaseFragment.hideProgressBar();
                                                }
                                            }
                                        });
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                                mThsBaseFragment.hideProgressBar();
                            }
                        }

                        @Override
                        public void onProviderDetailsFetchError(Throwable throwable) {
                            if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                                mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
                            }
                            mThsBaseFragment.hideProgressBar();
                        }
                    });
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
            mThsBaseFragment.hideProgressBar();
        }
    }

    void updateContinueButtonState(boolean isEnabled) {
        mthsProviderDetailsDisplayHelper.updateContinueButtonState(isEnabled);
    }

    void fetchProviderDetails(Context context, THSProviderInfo thsProviderInfo) {
        try {
            THSManager.getInstance().getProviderDetails(context, thsProviderInfo, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            if (null != sdkError) {
                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PROVIDER,sdkError));
            } else {
                ((THSAvailableProviderDetailFragment) mThsBaseFragment).setProvider(provider);
                try {
                    THSManager.getInstance().fetchEstimatedVisitCost(mThsBaseFragment.getContext(), provider, this);
                } catch (AWSDKInstantiationException e) {
                    e.printStackTrace();
                }
                getProviderAvailability(provider);
            }
        }
    }

    private void getProviderAvailability(Provider provider) {
        try {
            THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                    ((THSAvailableProviderDetailFragment) mThsBaseFragment).getDate(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDetailsFetchError(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
            mThsBaseFragment.hideProgressBar();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(List<Date> dates, THSSDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.hideProgressBar();
            if (null != sdkError.getSdkError()) {
                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_APPOINTMENTS,sdkError.getSdkError()));
            } else {
                mthsProviderDetailsDisplayHelper.updateView(((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider(), dates);
                dateList = dates;
            }
        }
    }

    @Override
    public void onResponse(Object o, SDKError sdkError) {
        if (sdkError == null) {
            mthsProviderDetailsDisplayHelper.launchConfirmAppointmentFragment(position);
        } else {
            if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                if (sdkError.getSDKErrorReason() != null && sdkError.getSDKErrorReason().name() != null) {
                    mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_SCHEDULE_APPOINTMENT,sdkError));
                } else {
                    mThsBaseFragment.showToast(mThsBaseFragment.getString(R.string.something_went_wrong));
                }
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
            mThsBaseFragment.hideProgressBar();
        }
    }

    @Override
    public void onEstimatedCostFetchSuccess(EstimatedVisitCost estimatedVisitCost, SDKError sdkError) {
        mthsProviderDetailsDisplayHelper.updateEstimateCost(estimatedVisitCost);
    }

    @Override
    public void onError(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }

    @SuppressWarnings("unchecked")
    void scheduleAppointment(int position) {
        this.position = position;
        try {
            THSManager.getInstance().scheduleAppointment(mThsBaseFragment.getContext(), ((THSAvailableProviderDetailFragment) mThsBaseFragment).getTHSProviderInfo(),
                    dateList.get(position), ((THSAvailableProviderDetailFragment) mThsBaseFragment).getReminderOptions(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailure(Map var1) {
        mThsBaseFragment.showToast(var1.toString());
    }
}
