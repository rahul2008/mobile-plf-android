package com.philips.amwelluapp.welcome;

import android.util.Log;
import android.widget.Toast;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.providerslist.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.providerslist.PTHPracticesListCallback;
import com.philips.amwelluapp.providerslist.PTHProvidersListCallback;
import com.philips.amwelluapp.providerslist.ProvidersListFragment;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;


public class PTHWelcomePresenter implements UIBasePresenter , PTHInitializeCallBack, PTHLoginCallBack,PTHGetConsumerObjectCallBack,PTHPracticesListCallback ,PTHProvidersListCallback{
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
    public void onInitializationResponse(Object var1, SDKError var2) {
        loginUserSilently();
    }

    private void loginUserSilently() {
        try {
            PTHManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"spoorti.h86@gmail.com","sujata123*",null,this);
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
    public void onLoginResponse(Object var1, SDKError var2){
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Login success");
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN SUCCESS",Toast.LENGTH_SHORT).show();
        ((PTHWelcomeFragment) uiBaseView).showProgressBar();
        try {
            PTHManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(),(Authentication) var1,this);
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
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        this.consumer = consumer;
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Consumer object received");
        Toast.makeText(uiBaseView.getFragmentActivity(),"CONSUMER OBJECT RECEIVED",Toast.LENGTH_SHORT).show();
        ((PTHWelcomeFragment) uiBaseView).showProgressBar();
        try {
            PTHManager.getInstance().getPractices(uiBaseView.getFragmentActivity(),consumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
    }

    @Override
    public void onPracticesListReceived(List<Practice> practices, SDKError sdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Practice list received : "+practices.size());
        Toast.makeText(uiBaseView.getFragmentActivity(),"Practice list received",Toast.LENGTH_SHORT).show();
        try {
            PTHManager.getInstance().getProviderList(uiBaseView.getFragmentActivity(),consumer,practices.get(0),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
    }

    @Override
    public void onProvidersListReceived(List<ProviderInfo> providerInfoList, SDKError sdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Providers list received : "+providerInfoList.size());
        Toast.makeText(uiBaseView.getFragmentActivity(),"Providers list received",Toast.LENGTH_SHORT).show();
        ProvidersListFragment providersListFragment = new ProvidersListFragment();
        providersListFragment.setProvidersList(providerInfoList);
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(),providersListFragment,"Providers List").commit();
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {

    }
}
