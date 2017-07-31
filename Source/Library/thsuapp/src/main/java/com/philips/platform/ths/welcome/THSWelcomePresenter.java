package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.R;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

//TODO: Review Comment - Spoorti - Do not implement THSGetConsumerObjectCallBack in WelcomePresenter
//TODO: Review Comment - Spoorti - Can we rename THSGetConsumerObjectCallBack to PTHGetConsumerCallBack?
public class THSWelcomePresenter implements THSBasePresenter, THSInitializeCallBack<Void,THSSDKError>,
        THSLoginCallBack<THSAuthentication,THSSDKError>,THSGetConsumerObjectCallBack,THSCheckConsumerExistsCallback<Boolean, THSSDKError>{
    THSBaseFragment uiBaseView;
    private Consumer consumer;

    THSWelcomePresenter(THSBaseFragment uiBaseView){
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
        ((THSWelcomeFragment) uiBaseView).enableInitButton(false);
        try {
            AmwellLog.i(AmwellLog.LOG,"Initialize - Call initiated from Client");
            THSManager.getInstance().initializeTeleHealth(uiBaseView.getFragmentActivity(), this);
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

    public void onInitializationResponse(Void aVoid, THSSDKError sdkError) {
        AmwellLog.i(AmwellLog.LOG,"Initialize - UI updated");
        try {
            checkIfUserExisits();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    private void loginUserSilently() {
        try {
            AmwellLog.i(AmwellLog.LOG,"Login - call initialted from client");
            THSManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"spoorti.h86@gmail.com","sujata123*",null,this);
           // THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
            //THSManager.getInstance().checkConsumerExists(uiBaseView.getContext(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
        if (uiBaseView.getContext() != null) {
            (uiBaseView).showToast("Init Failed!!!!!");
            ((THSWelcomeFragment) uiBaseView).enableInitButton(true);
        }
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError sdkError) {
        AmwellLog.i(AmwellLog.LOG, "Login - UI updated");
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login", "Login success");

        if(thsAuthentication.getAuthentication() == null){
            if(sdkError!=null && sdkError.getSDKErrorReason()!=null){
                uiBaseView.showToast(sdkError.getSDKErrorReason().name());
            }else {
                uiBaseView.showToast("Something went wrong!!");
            }
            return;
        }

        try {
            if (thsAuthentication.getAuthentication().needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(uiBaseView.getContext(), thsAuthentication, this);
            } else {
                uiBaseView.showProgressBar();
                THSManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(), thsAuthentication.getAuthentication(), this);
            }
        } catch (AWSDKInstantiationException e) {

        }
    }

    void checkIfUserExisits() throws AWSDKInstantiationException {
        THSManager.getInstance().checkConsumerExists(uiBaseView.getContext(),this);
    }

   /* private boolean isResponseSuccess(Object responseObject) {
        if(responseObject!=null) {
            return true;
        }
        return false;
    }

    private void checkIfTheUserIsPartiallyRegistered(THSAuthentication pthAuthentication) {
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
    //TODO: Review Comment - Spoorti - use bundle to pass arguments to fragment instead od setter. Use THSBaseView.addFragment for replacing the fragment
    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {

        launchPractice(consumer);

    }

    private void launchPractice(Consumer consumer) {
        this.consumer = consumer;
        //setting PTHconsumer  in THSManager so any presenter/fragment can access it
        THSConsumer THSConsumer = new THSConsumer();
        THSConsumer.setConsumer(consumer);
        THSManager.getInstance().setPTHConsumer(THSConsumer);
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login","Consumer object received");
        THSPracticeFragment PTHPracticeFragment = new THSPracticeFragment();
        PTHPracticeFragment.setConsumer(consumer);
       /* THSPracticeFragment.setActionBarListener(((THSWelcomeFragment) uiBaseView).getActionBarListener());
        uiBaseView.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(uiBaseView.getContainerID(), THSPracticeFragment,"THSPracticeList List").addToBackStack(null).commit();*/

        uiBaseView.addFragment(PTHPracticeFragment,PTHPracticeFragment.TAG,null);
    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
    }

    @Override
    public void onResponse(Boolean aBoolean, THSSDKError sdkError) {
        uiBaseView.hideProgressBar();
        if(aBoolean){
            try {
                THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }else {
            launchAmwellRegistrationFragment();
        }
    }

    private void launchAmwellRegistrationFragment() {
        THSRegistrationFragment thsRegistrationFragment = new THSRegistrationFragment();
        thsRegistrationFragment.setFragmentLauncher(uiBaseView.getFragmentLauncher());
        uiBaseView.addFragment(thsRegistrationFragment,THSRegistrationFragment.TAG,null);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
