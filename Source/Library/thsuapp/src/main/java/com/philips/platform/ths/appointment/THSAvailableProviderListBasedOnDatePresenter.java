package com.philips.platform.ths.appointment;

import android.content.Context;

import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import java.util.Date;
import java.util.List;

public class THSAvailableProviderListBasedOnDatePresenter implements THSBasePresenter, THSAvailableProvidersBasedOnDateCallback<List, THSSDKError> {
    THSBaseFragment mThsBaseFragment;

    THSAvailableProviderListBasedOnDatePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    @Override
    public void onResponse(List availableProviders, THSSDKError sdkError) {
        mThsBaseFragment.showToast("Available Providers list Success");
        ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).updateProviderAdapterList(availableProviders);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsBaseFragment.showToast("Available Providers list Failure");
    }

    public void getAvailableProvidersBasedOnDate() throws AWSDKInstantiationException {
        THSManager.getInstance().getAvailableProvidersBasedOnDate(mThsBaseFragment.getContext(),
                ((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).getPractice(),
                null,null,((THSAvailableProviderListBasedOnDateFragment)mThsBaseFragment).mDate,3,this);

    }
}
