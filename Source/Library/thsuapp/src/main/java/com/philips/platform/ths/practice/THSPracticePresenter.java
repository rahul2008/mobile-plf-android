package com.philips.platform.ths.practice;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.THSManager;


public class THSPracticePresenter implements THSBasePresenter, THSPracticesListCallback {

    THSBaseView uiBaseView;
    Consumer mConsumer;


     THSPracticePresenter(THSBaseView uiBaseView, Consumer consumer){
        this.uiBaseView = uiBaseView;
         this.mConsumer = consumer;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void fetchPractices(){
        try {
            THSManager.getInstance().getPractices(uiBaseView.getFragmentActivity(),mConsumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPracticesListReceived(THSPracticeList practices, SDKError sdkError) {
        ((THSPracticeFragment)uiBaseView).showPracticeList(practices);

    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
    }
}
