package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class THSAvailableProviderDetailPresenter implements THSBasePresenter, THSProviderDetailsCallback, THSAvailableProviderCallback<List, THSSDKError> {
    THSBaseFragment mThsBaseFragment;
    THSProviderDetailsDisplayHelper mthsProviderDetailsDisplayHelper;
    OnDateSetChangedInterface onDateSetChangedInterface;

    THSAvailableProviderDetailPresenter(THSBaseFragment thsBaseFragment, THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelper,
                                        OnDateSetChangedInterface onDateSetChangedInterface) {
        mThsBaseFragment = thsBaseFragment;
        mthsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelper;
        this.onDateSetChangedInterface = onDateSetChangedInterface;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.calendar_container) {
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSAvailableProviderDetailFragment)mThsBaseFragment).setDate(date);

                    launchAvailableProviderDetailBasedOnAvailibity();

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }

    }

    private void launchAvailableProviderDetailBasedOnAvailibity() {
        try {
            THSManager.getInstance().getProviderDetails(mThsBaseFragment.getContext(),
                    ((THSAvailableProviderDetailFragment) mThsBaseFragment).getTHSProviderInfo(), new THSProviderDetailsCallback() {
                        @Override
                        public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
                            ((THSAvailableProviderDetailFragment) mThsBaseFragment).setProvider(provider);
                            try {
                                THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                                        ((THSAvailableProviderDetailFragment) mThsBaseFragment).getDate(), new THSAvailableProviderCallback<List, THSSDKError>() {
                                            @Override
                                            public void onResponse(List dates, THSSDKError sdkError) {
                                                if(dates == null || dates.size()==0){
                                                    final THSProviderNotAvailableFragment fragment = new THSProviderNotAvailableFragment();
                                                    fragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(THSConstants.THS_DATE, ((THSAvailableProviderDetailFragment)mThsBaseFragment).getDate());
                                                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getPracticeInfo());
                                                    bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider());
                                                    bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProviderEntitiy());
                                                    mThsBaseFragment.addFragment(fragment, THSProviderNotAvailableFragment.TAG, bundle);
                                                    mThsBaseFragment.hideProgressBar();
                                                }else {
                                                    mthsProviderDetailsDisplayHelper.updateView(((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider(), dates);
                                                    mThsBaseFragment.hideProgressBar();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable throwable) {
                                                mThsBaseFragment.hideProgressBar();
                                            }
                                        });
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                                mThsBaseFragment.hideProgressBar();
                            }
                        }

                        @Override
                        public void onProviderDetailsFetchError(Throwable throwable) {
                            mThsBaseFragment.hideProgressBar();
                        }
                    });
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
            mThsBaseFragment.hideProgressBar();
        }
    }

    public void fetchProviderDetails(Context context, THSProviderInfo thsProviderInfo) {
        try {
            THSManager.getInstance().getProviderDetails(context, thsProviderInfo, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        ((THSAvailableProviderDetailFragment) mThsBaseFragment).setProvider(provider);
        getProviderAvailability(provider);
    }

    private void getProviderAvailability(Provider provider) {
        try {
            THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                    ((THSAvailableProviderDetailFragment)mThsBaseFragment).getDate(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDetailsFetchError(Throwable throwable) {
        mThsBaseFragment.hideProgressBar();
    }

    @Override
    public void onResponse(List dates, THSSDKError sdkError) {
        mthsProviderDetailsDisplayHelper.updateView(((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider(), dates);
        mThsBaseFragment.hideProgressBar();
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsBaseFragment.hideProgressBar();
    }
}
