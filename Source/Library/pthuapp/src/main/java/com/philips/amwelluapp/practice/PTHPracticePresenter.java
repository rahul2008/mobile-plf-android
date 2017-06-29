package com.philips.amwelluapp.practice;

import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.utility.PTHManager;


public class PTHPracticePresenter implements PTHBasePresenter, PTHPracticesListCallback {

    PTHBaseView uiBaseView;
    Consumer mConsumer;


     PTHPracticePresenter(PTHBaseView uiBaseView, Consumer consumer){
        this.uiBaseView = uiBaseView;
         this.mConsumer = consumer;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void fetchPractices(){
        //((PTHPracticeFragment) uiBaseView).showProgressBar();
       // PTHManager.getInstance().getPractices(uiBaseView.getFragmentActivity().getApplicationContext(),mConsumer,);
        try {
            PTHManager.getInstance().getPractices(uiBaseView.getFragmentActivity(),mConsumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPracticesListReceived(PTHPractice practices, SDKError sdkError) {
        ((PTHPracticeFragment)uiBaseView).showPracticeList(practices);

    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
        Toast.makeText(uiBaseView.getFragmentActivity(),"PTHPractice fetch FAILED",Toast.LENGTH_SHORT).show();
    }
}
