package com.philips.amwelluapp.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPracticeFragment;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.AmwellLog;
import com.philips.amwelluapp.utility.PTHManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

//TODO: Review Comment - Spoorti - Do not implement PTHGetConsumerObjectCallBack in WelcomePresenter
//TODO: Review Comment - Spoorti - Can we rename PTHGetConsumerObjectCallBack to PTHGetConsumerCallBack?
public class PTHWelcomePresenter implements PTHBasePresenter, PTHInitializeCallBack<Void,PTHSDKError>, PTHLoginCallBack<PTHAuthentication,PTHSDKError> ,PTHGetConsumerObjectCallBack {
    PTHBaseFragment uiBaseView;
    private Consumer consumer;

    PTHWelcomePresenter(PTHBaseFragment uiBaseView){
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.init_amwell) {
            initializeAwsdk();
        }
    }

    protected void initializeAwsdk() {
        uiBaseView.showProgressBar();
        ((PTHWelcomeFragment) uiBaseView).enableInitButton(false);
        try {
            AmwellLog.i(AmwellLog.LOG,"Initialize - Call initiated from Client");
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
        AmwellLog.i(AmwellLog.LOG,"Initialize - UI updated");
        loginUserSilently();
    }

    private void loginUserSilently() {
        try {
            AmwellLog.i(AmwellLog.LOG,"Login - call initialted from client");
            PTHManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"spoorti.h86@gmail.com","sujata123*",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
        if (uiBaseView.getContext() != null) {
            (uiBaseView).showToast("Init Failed!!!!!");
            ((PTHWelcomeFragment) uiBaseView).enableInitButton(true);
        }
    }

    @Override
    public void onLoginResponse(PTHAuthentication pthAuthentication, PTHSDKError sdkError) {
        AmwellLog.i(AmwellLog.LOG,"Login - UI updated");
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login","Login success");
        uiBaseView.showProgressBar();
        try {
            PTHManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(),pthAuthentication.getAuthentication(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

   /* private boolean isResponseSuccess(Object responseObject) {
        if(responseObject!=null) {
            return true;
        }
        return false;
    }

    private void checkIfTheUserIsPartiallyRegistered(PTHAuthentication pthAuthentication) {
        boolean isUserPartiallyRegistered = pthAuthentication.needsToCompleteEnrollment();
        if (isUserPartiallyRegistered){
            uiBaseView.addFragment(new PTHRegistrationDetailsFragment(),PTHRegistrationDetailsFragment.TAG,null);
        }
    }*/

    //TODO: Move it to Login Presenter one's Silent Login is removed
    @Override
    public void onLoginFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
    }

    //TODO: Review Comment - Spoorti - move to the relevant presenter
    //TODO: Review Comment - Spoorti - use bundle to pass arguments to fragment instead od setter. Use PTHBaseView.addFragment for replacing the fragment
    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {

        this.consumer = consumer;
        //setting PTHconsumer  in PTHManager so any presenter/fragment can access it
        PTHConsumer pthConsumer = new PTHConsumer();
        pthConsumer.setConsumer(consumer);
        PTHManager.getInstance().setPTHConsumer(pthConsumer);
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login","Consumer object received");
        PTHPracticeFragment PTHPracticeFragment = new PTHPracticeFragment();
        PTHPracticeFragment.setConsumer(consumer);
       /* PTHPracticeFragment.setActionBarListener(((PTHWelcomeFragment) uiBaseView).getActionBarListener());
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(), PTHPracticeFragment,"PTHPractice List").addToBackStack(null).commit();*/

       uiBaseView.addFragment(PTHPracticeFragment,PTHPracticeFragment.TAG,null);

    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
    }
}
