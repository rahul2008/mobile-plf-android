package com.philips.amwelluapp.welcome;

import android.util.Log;
import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPracticeFragment;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.registration.PTHRegistrationDetailsFragment;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


public class PTHWelcomePresenter implements UIBasePresenter , PTHInitializeCallBack<Void,PTHSDKError>, PTHLoginCallBack<PTHAuthentication,PTHSDKError> ,PTHGetConsumerObjectCallBack {
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
    public void onInitializationResponse(Void aVoid, PTHSDKError sdkError) {
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
    public void onLoginResponse(PTHAuthentication pthAuthentication, PTHSDKError sdkError) {
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

    private boolean isResponseSuccess(Object responseObject) {
        if(responseObject!=null) {
            return true;
        }
        return false;
    }

    private void checkIfTheUserIsPartiallyRegistered(PTHAuthentication pthAuthentication) {
        boolean isUserPartiallyRegistered = pthAuthentication.needsToCompleteEnrollment();
        if (isUserPartiallyRegistered){
            final FragmentLauncher fragmentLauncher = ((PTHWelcomeFragment)uiBaseView).getFragmentLauncher();
            ((PTHLaunchActivity) fragmentLauncher.getFragmentActivity()).
                    addFragment(new PTHRegistrationDetailsFragment(), PTHRegistrationDetailsFragment.TAG);
        }
    }

    //TODO: Move it to Login Presenter one's Silent Login is removed
    @Override
    public void onLoginFailure(Throwable var1) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"LOGIN Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {

        this.consumer = consumer;
        //////////////////////////////////////
        //setting PTHconsumer  in PTHManager so any presenter/fragment can access it
        PTHConsumer pthConsumer = new PTHConsumer();
        pthConsumer.setConsumer(consumer);
        PTHManager.getInstance().setPTHConsumer(pthConsumer);
        //////////////////////////////////////
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Log.d("Login","Consumer object received");
        Toast.makeText(uiBaseView.getFragmentActivity(),"CONSUMER OBJECT RECEIVED",Toast.LENGTH_SHORT).show();
        PTHPracticeFragment PTHPracticeFragment = new PTHPracticeFragment();
        PTHPracticeFragment.setConsumer(consumer);
        PTHPracticeFragment.setActionBarListener(((PTHWelcomeFragment) uiBaseView).getActionBarListener());
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(), PTHPracticeFragment,"PTHPractice List").addToBackStack(null).commit();

    }

    @Override
    public void onError(Throwable throwable) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
    }
}
