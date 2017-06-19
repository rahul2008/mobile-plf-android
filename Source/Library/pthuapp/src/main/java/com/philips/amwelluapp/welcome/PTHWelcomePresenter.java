package com.philips.amwelluapp.welcome;

import android.widget.Toast;

import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.registration.PTHRegistrationDetailsFragment;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


public class PTHWelcomePresenter implements UIBasePresenter , PTHInitializeCallBack<Void,PTHSDKError>, PTHLoginCallBack<PTHAuthentication,PTHSDKError> {
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
    public void onInitializationResponse(Void aVoid, PTHSDKError sdkError) {
        loginUserSilently();
    }

    private void loginUserSilently() {
        try {
            PTHManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"spoorti.hallur@philips.com","spoorti.hallur@philips.com",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        Toast.makeText(uiBaseView.getFragmentActivity(),"INIT FAILED",Toast.LENGTH_SHORT).show();
    }

    //TODO: Move it to Login Presenter one's Silent Login is removed
    @Override
    public void onLoginResponse(PTHAuthentication pthAuthentication, PTHSDKError pthsdkError) {
        ((PTHWelcomeFragment)uiBaseView).hideProgressBar();
        boolean isLoginSuccess = isResponseSuccess(pthAuthentication);
        if (isLoginSuccess){
            Toast.makeText(uiBaseView.getFragmentActivity(), "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();
            checkIfTheUserIsPartiallyRegistered(pthAuthentication);
        }else {
                Toast.makeText(uiBaseView.getFragmentActivity(), "LOGIN FAILED - " + pthsdkError.getMessage(), Toast.LENGTH_SHORT).show();
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
}
