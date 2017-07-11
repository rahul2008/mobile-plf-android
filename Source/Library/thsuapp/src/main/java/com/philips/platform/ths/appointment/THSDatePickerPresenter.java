package com.philips.platform.ths.appointment;

import android.os.Bundle;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;

public class THSDatePickerPresenter implements THSBasePresenter, THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError>{

    THSBaseFragment mTHSBaseFragment;
    Date date;

    THSDatePickerPresenter(THSBaseFragment thsBaseFragment){
        mTHSBaseFragment = thsBaseFragment;
    }


    @Override
    public void onEvent(int componentID)  {
    }

    @Override
    public void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError) {

    }

    @Override
    public void onFailure(Throwable throwable) {

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
}
