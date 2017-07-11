package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;

import java.util.ArrayList;
import java.util.List;

public class THSProviderListPresenter implements THSProvidersListCallback, THSBasePresenter {

    private THSBaseView mUiBaseView;
    private THSProviderListViewInterface THSProviderListViewInterface;


    public THSProviderListPresenter(THSBaseView uiBaseView, THSProviderListViewInterface THSProviderListViewInterface){
        this.mUiBaseView = uiBaseView;
        this.THSProviderListViewInterface = THSProviderListViewInterface;
    }

    public void fetchProviderList(Consumer consumer, Practice practice){
        try {
            getPthManager().getProviderList(mUiBaseView.getFragmentActivity(),consumer,practice,this);
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
                ((THSProvidersListFragment)mUiBaseView).btn_get_started.setText(((THSProvidersListFragment) mUiBaseView).
                        getContext().getString(R.string.get_started));
                offlineProviders.add(providerInfo);
            }else {
                ((THSProvidersListFragment)mUiBaseView).btn_get_started.setText(((THSProvidersListFragment) mUiBaseView).
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
    public void onEvent(int componentID) {
        if(componentID == R.id.getStartedButton){
            
        }
    }
}
