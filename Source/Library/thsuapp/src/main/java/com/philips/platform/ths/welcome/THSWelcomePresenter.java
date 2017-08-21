/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
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
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.net.ssl.HttpsURLConnection;

public class THSWelcomePresenter implements THSBasePresenter, THSInitializeCallBack<Void,THSSDKError>,
        THSLoginCallBack<THSAuthentication,THSSDKError>,THSGetConsumerObjectCallBack,THSCheckConsumerExistsCallback<Boolean, THSSDKError>{
    private THSBaseFragment uiBaseView;
    private Consumer consumer;
    private final int APPOINTMENTS = 1;
    private final int VISIT_HISTORY = 2;
    private final int HOW_IT_WORKS = 3;
    private final int PRACTICES = 4;
    private int launchInput = -1;

    THSWelcomePresenter(THSBaseFragment uiBaseView){
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.appointments) {
            launchInput = 1;
            checkForUserExisitance();
        } else if (componentID == R.id.visit_history) {
            launchInput = 2;
            checkForUserExisitance();
        } else if (componentID == R.id.how_it_works) {
            launchInput = 3;
            uiBaseView.showToast("Coming Soon!!!");
        }else if(componentID == R.id.ths_start){
            launchInput = 4;
            checkForUserExisitance();
        }
    }

    private void checkForUserExisitance() {
        AmwellLog.i(AmwellLog.LOG,"Initialize - UI updated");
        try {
            checkIfUserExisits();
            //THSManager.getInstance().authenticate(uiBaseView.getContext(),"rohit.nihal@philips.com","Philips@123",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    protected void initializeAwsdk() {
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
        uiBaseView.hideProgressBar();
        ((THSWelcomeFragment)uiBaseView).updateView();
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
        if (uiBaseView.getContext() != null) {
            (uiBaseView).showToast("Init Failed!!!!!");
        }
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError sdkError) {

        if (sdkError.getSdkError() != null && sdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            refreshToken();
            return;
        }

        try {
            if (thsAuthentication.needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(uiBaseView.getContext(), thsAuthentication, this);
            } else {
                THSManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(), thsAuthentication.getAuthentication(), this);
            }
        } catch (AWSDKInstantiationException e) {

        }
    }

    private void refreshToken() {
        THSManager.getInstance().getUser(uiBaseView.getContext()).refreshLoginSession(new RefreshLoginSessionHandler() {
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
        switch (launchInput){
            case APPOINTMENTS:
                uiBaseView.hideProgressBar();
                uiBaseView.addFragment(new THSScheduledVisitsFragment(),THSScheduledVisitsFragment.TAG,null);
                break;
            case VISIT_HISTORY:
                uiBaseView.hideProgressBar();
                uiBaseView.addFragment(new THSVisitHistoryFragment(),THSScheduledVisitsFragment.TAG,null);
                break;
            case HOW_IT_WORKS:
                uiBaseView.hideProgressBar();
                break;
            case PRACTICES:
                uiBaseView.hideProgressBar();
                launchPractice(consumer);
                break;
        }
    }

    private void launchPractice(Consumer consumer) {
        this.consumer = consumer;
        //setting PTHconsumer  in singleton THSManager so any presenter/fragment can access it
        THSConsumer THSConsumer = new THSConsumer();
        THSConsumer.setConsumer(consumer);
        THSManager.getInstance().setPTHConsumer(THSConsumer);
        AmwellLog.d("Login","Consumer object received");
        final THSPracticeFragment fragment = new THSPracticeFragment();
        fragment.setFragmentLauncher(uiBaseView.getFragmentLauncher());
        uiBaseView.getActivity().getSupportFragmentManager().popBackStack();
        uiBaseView.addFragment(fragment,THSPracticeFragment.TAG,null);
    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
    }

    @Override
    public void onResponse(Boolean aBoolean, THSSDKError thssdkError) {
        SDKError sdkError = thssdkError.getSdkError();
        if(null != sdkError){
            uiBaseView.hideProgressBar();
            if(null != sdkError.getSDKErrorReason()) {
                uiBaseView.showToast(sdkError.getSDKErrorReason().name());
            }
            return;
        }

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
