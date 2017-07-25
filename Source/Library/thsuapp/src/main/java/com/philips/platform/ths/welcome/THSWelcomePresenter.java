package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.R;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

//TODO: Review Comment - Spoorti - Do not implement THSGetConsumerObjectCallBack in WelcomePresenter
//TODO: Review Comment - Spoorti - Can we rename THSGetConsumerObjectCallBack to PTHGetConsumerCallBack?
public class THSWelcomePresenter implements THSBasePresenter, THSInitializeCallBack<Void,THSSDKError>,
        THSLoginCallBack<THSAuthentication,THSSDKError>,THSGetConsumerObjectCallBack,THSSDKValidatedCallback<THSConsumer,SDKError> {
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
        loginUserSilently();
    }

    private void loginUserSilently() {
        try {
            AmwellLog.i(AmwellLog.LOG,"Login - call initialted from client");
          //  THSManager.getInstance().authenticate(uiBaseView.getFragmentActivity(),"spoorti.h86@gmail.com","sujata123*",null,this);
           // THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
            THSManager.getInstance().checkConsumerExists(uiBaseView.getContext(),this);
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
    public void onLoginResponse(THSAuthentication THSAuthentication, THSSDKError sdkError) {
        AmwellLog.i(AmwellLog.LOG,"Login - UI updated");
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login","Login success");
        uiBaseView.showProgressBar();
        try {
            THSManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(), THSAuthentication.getAuthentication(),this);
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
    public void onValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onResponse(THSConsumer thsConsumer, SDKError thssdkError) {
        launchPractice(thsConsumer.getConsumer());
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
