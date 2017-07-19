package com.philips.platform.ths.providerslist;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderListBasedOnDateFragment;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class THSProviderListPresenter implements THSProvidersListCallback, THSBasePresenter,THSOnDemandSpecialtyCallback<List<THSOnDemandSpeciality>,THSSDKError> {

    private THSBaseFragment mThsBaseFragment;
    private THSProviderListViewInterface THSProviderListViewInterface;
    private List<THSOnDemandSpeciality> mThsOnDemandSpeciality;


    public THSProviderListPresenter(THSBaseFragment uiBaseView, THSProviderListViewInterface THSProviderListViewInterface){
        this.mThsBaseFragment = uiBaseView;
        this.THSProviderListViewInterface = THSProviderListViewInterface;
    }

    public void fetchProviderList(Consumer consumer, Practice practice){
        try {
            getPthManager().getProviderList(mThsBaseFragment.getFragmentActivity(),consumer,practice,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    THSManager getPthManager() {
        return THSManager.getInstance();
    }


    @Override
    public void onProvidersListReceived(List<THSProviderInfo> providerInfoList, SDKError sdkError) {
        boolean providerAvailable = isProviderAvailable(providerInfoList);
        if(providerAvailable){
            ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setVisibility(View.VISIBLE);
            ((THSProvidersListFragment) mThsBaseFragment).btn_schedule_appointment.setVisibility(View.GONE);
            if (mThsBaseFragment.getContext() != null) {
                ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setText((mThsBaseFragment).
                        getContext().getString(R.string.get_started));
            }
        }else {
            ((THSProvidersListFragment) mThsBaseFragment).btn_schedule_appointment.setVisibility(View.VISIBLE);
            ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setVisibility(View.GONE);
            if (mThsBaseFragment.getContext() != null) {
                ((THSProvidersListFragment) mThsBaseFragment).btn_schedule_appointment.setText((mThsBaseFragment).
                        getContext().getString(R.string.schedule_appointment));
            }
        }
        THSProviderListViewInterface.updateProviderAdapterList(providerInfoList);
    }

    boolean isProviderAvailable(List<THSProviderInfo> providerInfoList){
        for (THSProviderInfo thsProviderInfo: providerInfoList) {
            if(!ProviderVisibility.isOffline(thsProviderInfo.getVisibility())){
                return true;
            }
        }
        return false;
    }

    /*private List<THSProviderInfo> checkForProviderAvailibity(List<THSProviderInfo> providerInfoList) {
        List<THSProviderInfo> availableProviders = new ArrayList<>();
        List<THSProviderInfo> offlineProviders = new ArrayList<>();
        for (THSProviderInfo thsProviderInfo: providerInfoList) {
            if(ProviderVisibility.isOffline(thsProviderInfo.getVisibility())){
                ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setText(((THSProvidersListFragment) mThsBaseFragment).
                        getContext().getString(R.string.get_started));
                offlineProviders.add(thsProviderInfo);
            }else {
                ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setText(((THSProvidersListFragment) mThsBaseFragment).
                        getContext().getString(R.string.schedule_appointment));
                availableProviders.add(thsProviderInfo);
            }
        }
        if(availableProviders.size() == 0)
            return offlineProviders;
        else
            return availableProviders;
    }*/

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID) {
        final Practice practice = ((THSProvidersListFragment) mThsBaseFragment).getPractice();
        if (componentID == R.id.getStartedButton) {
            try {
                THSManager.getInstance().getOnDemandSpecialities(mThsBaseFragment.getFragmentActivity(),
                        practice, null, this);
            } catch (AWSDKInstantiationException e) {


            }
        } else if (componentID == R.id.getScheduleAppointmentButton) {
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
                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, practice);
                    mThsBaseFragment.addFragment(new THSAvailableProviderListBasedOnDateFragment(), THSAvailableProviderListBasedOnDateFragment.TAG, bundle);
                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }

    @Override
    public void onResponse(List<THSOnDemandSpeciality> onDemandSpecialties, THSSDKError sdkError) {
        if(onDemandSpecialties == null || onDemandSpecialties.size()==0){
            mThsBaseFragment.showToast("No OnDemandSpecialities available at present, please try after some time");
            mThsBaseFragment.hideProgressBar();
            return;
        }
        mThsOnDemandSpeciality = onDemandSpecialties;
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_ON_DEMAND,onDemandSpecialties.get(0));
        mThsBaseFragment.addFragment(new THSSymptomsFragment(),THSSymptomsFragment.TAG,bundle);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
