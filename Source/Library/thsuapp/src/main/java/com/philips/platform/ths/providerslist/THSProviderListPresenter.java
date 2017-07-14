package com.philips.platform.ths.providerslist;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.ArrayList;
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
    public void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError) {
        List<ProviderInfo> avaiableProviders = checkForProviderAvailibity(providerInfoList);
        THSProviderListViewInterface.updateProviderAdapterList(avaiableProviders);
    }

    private List<ProviderInfo> checkForProviderAvailibity(List<ProviderInfo> providerInfoList) {
        List<ProviderInfo> availableProviders = new ArrayList<>();
        List<ProviderInfo> offlineProviders = new ArrayList<>();
        for (ProviderInfo providerInfo: providerInfoList) {
            if(ProviderVisibility.isOffline(providerInfo.getVisibility())){
                ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setText(((THSProvidersListFragment) mThsBaseFragment).
                        getContext().getString(R.string.get_started));
                offlineProviders.add(providerInfo);
            }else {
                ((THSProvidersListFragment) mThsBaseFragment).btn_get_started.setText(((THSProvidersListFragment) mThsBaseFragment).
                        getContext().getString(R.string.schedule_appointment));
                availableProviders.add(providerInfo);
            }
        }
        if(availableProviders.size() == 0)
            return offlineProviders;
        else
            return availableProviders;
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID){
        if(componentID == R.id.getStartedButton){
            try {
                THSManager.getInstance().getOnDemandSpecialities(mThsBaseFragment.getFragmentActivity(),
                        ((THSProvidersListFragment) mThsBaseFragment).getmPractice(),null,this);
            } catch (AWSDKInstantiationException e) {


            }
        }
    }

    @Override
    public void onResponse(List<THSOnDemandSpeciality> onDemandSpecialties, THSSDKError sdkError) {
        mThsOnDemandSpeciality = onDemandSpecialties;
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_ON_DEMAND,onDemandSpecialties.get(0));
        mThsBaseFragment.addFragment(new THSSymptomsFragment(),THSSymptomsFragment.TAG,bundle);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
