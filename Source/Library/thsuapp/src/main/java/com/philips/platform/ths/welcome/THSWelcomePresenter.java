package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.net.ssl.HttpsURLConnection;

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

        if (sdkError.getSdkError() != null && sdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            refreshToken();
            return;
        }

        try {
            if (thsAuthentication.getAuthentication().needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(uiBaseView.getContext(), thsAuthentication, this);
            } else {
                THSManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(), thsAuthentication.getAuthentication(), this);
            }
        } catch (AWSDKInstantiationException e) {

        }
    }

    private void refreshToken() {
        new User(uiBaseView.getContext()).refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                authenticateUser();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int i) {
                uiBaseView.showToast("Refresh Signon failed with the following status code " + i + " please logout and login again");
                uiBaseView.hideProgressBar();
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {
                uiBaseView.showToast(s);
            }
        });
    }

    void checkIfUserExisits() throws AWSDKInstantiationException {
        THSManager.getInstance().checkConsumerExists(uiBaseView.getContext(),this);
    }

    @Override
    public void onLoginFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        uiBaseView.hideProgressBar();
        launchPractice(consumer);
    }

    private void launchPractice(Consumer consumer) {
        this.consumer = consumer;
        //setting PTHconsumer  in singleton THSManager so any presenter/fragment can access it
        THSConsumer THSConsumer = new THSConsumer();
        THSConsumer.setConsumer(consumer);
        THSManager.getInstance().setPTHConsumer(THSConsumer);
        uiBaseView.hideProgressBar();
        AmwellLog.d("Login","Consumer object received");
        final THSPracticeFragment fragment = new THSPracticeFragment();
        fragment.setFragmentLauncher(uiBaseView.getFragmentLauncher());
        uiBaseView.addFragment(fragment,THSPracticeFragment.TAG,null);
    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
    }

    @Override
    public void onResponse(Boolean aBoolean, THSSDKError sdkError) {
        if(aBoolean){
            authenticateUser();
        }else {
            uiBaseView.hideProgressBar();
            launchAmwellRegistrationFragment();
        }
    }

    private void authenticateUser() {
        try {
            THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
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
