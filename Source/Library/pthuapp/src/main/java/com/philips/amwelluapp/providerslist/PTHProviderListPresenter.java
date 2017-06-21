package com.philips.amwelluapp.providerslist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;

/**
 * Created by philips on 6/20/17.
 */

public class PTHProviderListPresenter implements PTHPracticeProviderImageCallback{

    public PTHProviderListPresenter(){

    }

    public void fetchProviderImage(Context context, ProviderInfo providerInfo, ImageView imageView, ProviderImageSize providerImageSize, Drawable drawable){
        /*try {
            //PTHManager.getInstance().fetchProviderImage(context,providerInfo,imageView,providerImageSize,drawable,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onImageLoaded() {

    }

    @Override
    public void onImageDownloadError() {

    }
}
