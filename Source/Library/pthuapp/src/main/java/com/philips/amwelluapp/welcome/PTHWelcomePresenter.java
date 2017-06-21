package com.philips.amwelluapp.welcome;

import android.util.Log;
import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPractice;
import com.philips.amwelluapp.practice.PTHPracticesListCallback;
import com.philips.amwelluapp.practice.PracticeFragment;
import com.philips.amwelluapp.providerslist.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.providerslist.PTHProvidersListCallback;
import com.philips.amwelluapp.providerslist.PTHProvidersListFragment;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;


public class PTHWelcomePresenter implements UIBasePresenter , PTHInitializeCallBack<Void,SDKError>, PTHLoginCallBack<PTHAuthentication,SDKError> ,PTHGetConsumerObjectCallBack,PTHPracticesListCallback,PTHProvidersListCallback {
    UIBaseView uiBaseView;
    private Consumer consumer;

    PTHWelcomePresenter(UIBaseView uiBaseView){
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void initializeAwsdk() {
        ((PTHWelcomeFragment) uiBaseView).showProgressBar();
        try {
            PTHManager.getInstance().initializeTeleHealth(uiBaseView.getFragmentActivity(), this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }catch (AWSDKInitializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationResponse(Void aVoid, SDKError sdkError) {
        loginUserSilently();
    }

    private void loginUserSilently() {
        try {
            PTHManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"sumit.prasad@philips.com","Philips@123",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"INIT FAILED",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginResponse(PTHAuthentication pthAuthentication, SDKError sdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Login success");
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN SUCCESS",Toast.LENGTH_SHORT).show();
        ((PTHWelcomeFragment) uiBaseView).showProgressBar();
        try {
            PTHManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(),pthAuthentication.getAuthentication(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoginFailure(Throwable var1) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Providers list received : "+providerInfoList.size());
        Toast.makeText(uiBaseView.getFragmentActivity(),"Providers list received",Toast.LENGTH_SHORT).show();
        PTHProvidersListFragment PTHProvidersListFragment = new PTHProvidersListFragment();
        PTHProvidersListFragment.setProvidersList(providerInfoList);
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(), PTHProvidersListFragment,"Providers List").commit();
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {

        this.consumer = consumer;
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Consumer object received");
        Toast.makeText(uiBaseView.getFragmentActivity(),"CONSUMER OBJECT RECEIVED",Toast.LENGTH_SHORT).show();
       /* ((PTHWelcomeFragment) uiBaseView).showProgressBar();
        try {
            PTHManager.getInstance().getPractices(uiBaseView.getFragmentActivity(),consumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }*/
        PracticeFragment practiceFragment = new PracticeFragment();
        practiceFragment.setConsumer(consumer);
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(),practiceFragment,"PTHPractice List").commit();

    }

    @Override
    public void onError(Throwable throwable) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
    }

    @Override
    public void onPracticesListReceived(PTHPractice practices, SDKError sdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","PTHPractice list received : "+practices.getPractices().size());
        Toast.makeText(uiBaseView.getFragmentActivity(),"PTHPractice list received",Toast.LENGTH_SHORT).show();
        try {
            PTHManager.getInstance().getProviderList(uiBaseView.getFragmentActivity(),consumer,practices.getPractices().get(0),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
    }
}
