package com.philips.platform.ths.appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerdetails.THSPRoviderDetailsViewInterface;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerdetails.THSProviderDetailsPresenter;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Date;
import java.util.List;

public class THSAvailableProviderDetailPresenter implements THSBasePresenter, THSProviderDetailsCallback, THSAvailableProviderCallback<List,THSSDKError> {
    THSBaseFragment mThsBaseFragment;
    Date mDate;
    THSProviderDetailsDisplayHelper mthsProviderDetailsDisplayHelper;

    THSAvailableProviderDetailPresenter(THSBaseFragment thsBaseFragment, Date date, THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelper){
        mThsBaseFragment = thsBaseFragment;
        mDate = date;
        mthsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelper;
    }
    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.calendar_container){
            Bundle bundle = new Bundle();
            bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,((THSAvailableProviderDetailFragment)mThsBaseFragment).getPracticeInfo());
            bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY ,((THSAvailableProviderDetailFragment)mThsBaseFragment).getProviderEntitiy());
            bundle.putParcelable(THSConstants.THS_PROVIDER ,((THSAvailableProviderDetailFragment)mThsBaseFragment).getProvider());
            bundle.putBoolean(THSConstants.THS_IS_DETAILS,true);
            mThsBaseFragment.addFragment(new THSDatePickerFragment(), THSDatePickerFragment.TAG,bundle);
        }
    }

    public void fetchProviderDetails(Context context, THSProviderInfo thsProviderInfo){
        try {
            THSManager.getInstance().getProviderDetails(context, thsProviderInfo, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        ((THSAvailableProviderDetailFragment)mThsBaseFragment).setProvider(provider);
        getProviderAvailability(provider);
    }

    private void getProviderAvailability(Provider provider) {
        try {
            THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(),provider,mDate,this);
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
        if (dates == null || dates.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(THSConstants.THS_DATE, mDate);
            bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getPracticeInfo());
            bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider());
            bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, ((THSAvailableProviderDetailFragment) mThsBaseFragment).getProviderEntitiy());
            mThsBaseFragment.addFragment(new THSProviderNotAvailableFragment(), THSProviderNotAvailableFragment.TAG, bundle);
        } else {
            mthsProviderDetailsDisplayHelper.updateView(((THSAvailableProviderDetailFragment) mThsBaseFragment).getProvider(), dates);
            mThsBaseFragment.hideProgressBar();
        }

        /*Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_DATE,mDate);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,((THSAvailableProviderDetailFragment)mThsBaseFragment).getPracticeInfo());
        bundle.putParcelable(THSConstants.THS_PROVIDER ,((THSAvailableProviderDetailFragment)mThsBaseFragment).getProvider());
        mThsBaseFragment.addFragment(new THSProviderNotAvailableFragment(),THSProviderNotAvailableFragment.TAG,bundle);
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY ,((THSAvailableProviderDetailFragment)mThsBaseFragment).getProviderEntitiy());*/
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsBaseFragment.hideProgressBar();
    }
}
