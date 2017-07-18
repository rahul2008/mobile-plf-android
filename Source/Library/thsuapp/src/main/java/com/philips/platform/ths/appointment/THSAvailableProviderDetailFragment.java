package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;

public class THSAvailableProviderDetailFragment extends THSProviderDetailsFragment implements View.OnClickListener{
    public static final String TAG = THSAvailableProviderDetailFragment.class.getSimpleName();

    Date mDate;

    THSProviderEntity thsProviderEntity;
    THSAvailableProviderDetailPresenter thsAvailableDetailProviderPresenter;
    Practice mPractice;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_pick_time), true);
        }
        Bundle arguments = getArguments();
        thsProviderEntity = arguments.getParcelable(THSConstants.THS_PROVIDER_ENTITY);
        mDate = (Date) arguments.getSerializable(THSConstants.THS_DATE);
        mPractice = arguments.getParcelable(THSConstants.THS_PRACTICE_INFO);

        THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(getContext(),this,this,this,this,getView());
        thsAvailableDetailProviderPresenter = new THSAvailableProviderDetailPresenter(this,mDate,thsProviderDetailsDisplayHelper);



        thsAvailableDetailProviderPresenter.fetchProviderDetails(getContext(),getProviderEntity());
    }

    @Override
    public String getFragmentTag() {
        return THSAvailableProviderDetailFragment.TAG;
    }

    @Override
    public Practice getPracticeInfo(){
        return mPractice;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.calendar_container){
           thsAvailableDetailProviderPresenter.onEvent(R.id.calendar_container);
        }
    }

    public THSProviderInfo getProviderEntity() {
        ProviderInfo providerInfo = ((THSAvailableProvider) thsProviderEntity).getProviderInfo();

        THSProviderInfo thsProviderInfo = new THSProviderInfo();
        thsProviderInfo.setTHSProviderInfo(providerInfo);
        return thsProviderInfo;
    }
}
