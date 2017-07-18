package com.philips.platform.ths.appointment;

import android.os.Bundle;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;

public class THSDatePickerPresenter implements THSBasePresenter{

    THSBaseFragment mTHSBaseFragment;
    Date date;

    THSDatePickerPresenter(THSBaseFragment thsBaseFragment){
        mTHSBaseFragment = thsBaseFragment;
    }


    @Override
    public void onEvent(int componentID)  {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void launchProviderListBasedOnTime() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_DATE,getDate());
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,((THSDatePickerFragment)mTHSBaseFragment).getPractice());
        mTHSBaseFragment.addFragment(new THSAvailableProviderListBasedOnDateFragment(),THSAvailableProviderListBasedOnDateFragment.TAG,bundle);
    }

    public void launchProviderDetailBasedOnTime() {
        THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        thsAvailableProviderDetailFragment.setTHSProviderEntity(((THSDatePickerFragment)mTHSBaseFragment).getThsProviderEntity());
        Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_DATE,getDate());
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,((THSDatePickerFragment)mTHSBaseFragment).getPractice());
        bundle.putParcelable(THSConstants.THS_PROVIDER,((THSDatePickerFragment)mTHSBaseFragment).getProvider());
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY,((THSDatePickerFragment)mTHSBaseFragment).getThsProviderEntity());
        mTHSBaseFragment.addFragment(thsAvailableProviderDetailFragment,THSAvailableProviderDetailFragment.TAG,bundle);
    }
}
