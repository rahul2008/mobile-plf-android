package com.philips.amwelluapp.providerslist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.List;

public class PTHProviderListPresenter implements PTHProvidersListCallback{

    private Context context;
    private PTHProviderListViewInterface PTHProviderListViewInterface;


    public PTHProviderListPresenter(Context context,PTHProviderListViewInterface PTHProviderListViewInterface){
        this.context = context;
        this.PTHProviderListViewInterface = PTHProviderListViewInterface;
    }

    public void fetchProviderList(Consumer consumer, Practice practice){
        try {
            PTHManager.getInstance().getProviderList(context,consumer,practice,this);
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
}
