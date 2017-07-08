package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.PTHManager;

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

    PTHManager getPthManager() {
        return PTHManager.getInstance();
    }


    @Override
    public void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError) {
        THSProviderListViewInterface.updateProviderAdapterList(providerInfoList);
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID) {

    }
}
