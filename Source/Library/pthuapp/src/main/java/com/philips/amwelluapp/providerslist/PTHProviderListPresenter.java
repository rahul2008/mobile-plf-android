package com.philips.amwelluapp.providerslist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.intake.PTHSymptomsFragment;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.List;

public class PTHProviderListPresenter implements PTHProvidersListCallback, PTHBasePresenter{

    private PTHBaseView mUiBaseView;
    private PTHProviderListViewInterface PTHProviderListViewInterface;


    public PTHProviderListPresenter(PTHBaseView uiBaseView, PTHProviderListViewInterface PTHProviderListViewInterface){
        this.mUiBaseView = uiBaseView;
        this.PTHProviderListViewInterface = PTHProviderListViewInterface;
    }

    public void fetchProviderList(Consumer consumer, Practice practice){
        try {
            PTHManager.getInstance().getProviderList(mUiBaseView.getFragmentActivity(),consumer,practice,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }
    public void fetchProviderImage(Context context, ProviderInfo providerInfo, ImageView imageView, ProviderImageSize providerImageSize, Drawable drawable){
        /*try {
            //PTHManager.getInstance().fetchProviderImage(context,providerInfo,imageView,providerImageSize,drawable,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError) {
        PTHProviderListViewInterface.updateProviderAdapterList(providerInfoList);
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID) {

    }
}
