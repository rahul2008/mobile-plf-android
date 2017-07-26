package com.philips.platform.ths.providerdetails;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSAvailableProviderListBasedOnDateFragment;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;

public class THSProviderDetailsPresenter implements THSBasePresenter,THSProviderDetailsCallback{

    THSPRoviderDetailsViewInterface viewInterface;

    Provider mProvider;
    THSBaseFragment mThsBaseFragment;

    public THSProviderDetailsPresenter(THSPRoviderDetailsViewInterface viewInterface, THSBaseFragment thsBaseFragment){
        this.viewInterface = viewInterface;
        mThsBaseFragment = thsBaseFragment;
    }

    public void fetchProviderDetails(){
        try {
            if (viewInterface.getTHSProviderInfo() != null)
                getPTHManager().getProviderDetails(viewInterface.getContext(), viewInterface.getTHSProviderInfo(), this);
            else
                viewInterface.dismissRefreshLayout();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    protected THSManager getPTHManager() {
        return THSManager.getInstance();
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        viewInterface.updateView(provider);
    }

    @Override
    public void onProviderDetailsFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.detailsButtonOne) {
            THSConsumer THSConsumer = new THSConsumer();
            THSConsumer.setConsumer(viewInterface.getConsumerInfo());
            Bundle bundle = new Bundle();
            bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, viewInterface.getTHSProviderInfo());
            THSSymptomsFragment thsSymptomsFragment = new THSSymptomsFragment();
            thsSymptomsFragment.setConsumerObject(THSConsumer);
            mThsBaseFragment.addFragment(thsSymptomsFragment, THSSymptomsFragment.TAG, bundle);

        } else if (componentID == R.id.detailsButtonTwo) {
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(THSConstants.THS_DATE, date);
                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, viewInterface.getPracticeInfo());
                    mThsBaseFragment.addFragment(new THSAvailableProviderListBasedOnDateFragment(), THSAvailableProviderListBasedOnDateFragment.TAG, bundle);
                }
            };

            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);


        } else if (componentID == R.id.detailsButtonContinue) {

        } else if (componentID == R.id.calendar_container) {
            THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
                    thsAvailableProviderDetailFragment.setTHSProviderEntity(viewInterface.getTHSProviderInfo());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(THSConstants.THS_DATE, date);
                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, viewInterface.getPracticeInfo());
                    bundle.putParcelable(THSConstants.THS_PROVIDER, viewInterface.getProvider());
                    bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, viewInterface.getTHSProviderInfo());
                    mThsBaseFragment.addFragment(thsAvailableProviderDetailFragment, THSAvailableProviderDetailFragment.TAG, bundle);
                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }
}
