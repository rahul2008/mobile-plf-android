package com.philips.amwelluapp.welcome;

import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


public class PTHWelcomePresenter implements UIBasePresenter , PTHInitializeCallBack, PTHLoginCallBack {
    UIBaseView uiBaseView;

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
    public void onLoginResponse(Object var1, SDKError var2) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN SUCCESS",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailure(Throwable var1) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN Failed",Toast.LENGTH_SHORT).show();
    }
}
