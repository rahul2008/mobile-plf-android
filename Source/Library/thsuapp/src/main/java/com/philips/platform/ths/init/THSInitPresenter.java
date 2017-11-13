/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.onboarding.OnBoardingFragment;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSInitializeCallBack;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_CONSUMER_DETAILS;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_INITIALIZATION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_CONSUMER_EXIST_CHECK;

public class THSInitPresenter implements THSBasePresenter, THSInitializeCallBack<Void, THSSDKError>, THSCheckConsumerExistsCallback<Boolean, THSSDKError>, THSLoginCallBack<THSAuthentication, THSSDKError>, THSGetConsumerObjectCallBack {

    THSInitFragment mThsInitFragment;
    boolean isRefreshTokenRequestedBefore;

    THSInitPresenter(THSInitFragment thsInitFragment) {
        isRefreshTokenRequestedBefore = false;
        mThsInitFragment = thsInitFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void initializeAwsdk() {
        User user = THSManager.getInstance().getUser(mThsInitFragment.getContext());
        if (user == null || !user.isUserSignIn()) {
            mThsInitFragment.hideProgressBar();
            mThsInitFragment.doTagging(ANALYTICS_INITIALIZATION, mThsInitFragment.getString(R.string.ths_user_not_logged_in), false);
            mThsInitFragment.showError(mThsInitFragment.getString(R.string.ths_user_not_logged_in));
            return;
        }
        try {
            AmwellLog.i(AmwellLog.LOG, "Initialize - Call initiated from Client");
            THSManager.getInstance().initializeTeleHealth(mThsInitFragment.getContext(), this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        } catch (AWSDKInitializationException e) {
            e.printStackTrace();
        } catch(IllegalArgumentException e){
            mThsInitFragment.showError(mThsInitFragment.getString(R.string.initialization_failed));
        }
    }

    @Override

    public void onInitializationResponse(Void aVoid, THSSDKError sdkError) {
        if (sdkError.getSdkError() != null) {
            if (null != mThsInitFragment && mThsInitFragment.isFragmentAttached()) {
                mThsInitFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_INITIALIZATION, sdkError.getSdkError()));
            }
        }else {
            checkForUserExisitance();
        }
        //launchWelcomeScreen();
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        if (null != mThsInitFragment && mThsInitFragment.isFragmentAttached()) {
            mThsInitFragment.hideProgressBar();
            if (mThsInitFragment.getContext() != null) {
                String errorMesage = "";
                if(null!=var1 && null!=var1.getMessage()){
                    errorMesage=var1.getMessage();
                }
                mThsInitFragment.doTagging(ANALYTICS_INITIALIZATION,errorMesage,false);
                mThsInitFragment.showError(mThsInitFragment.getFragmentActivity().getString(R.string.ths_se_server_error_toast_message),true);
            }
        }
    }

    private void checkForUserExisitance() {
        AmwellLog.i(AmwellLog.LOG, "Initialize - UI updated");
        try {
            THSManager.getInstance().checkConsumerExists(mThsInitFragment.getContext(), this);
            //THSManager.getInstance().authenticate(uiBaseView.getContext(),"rohit.nihal@philips.com","Philips@123",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Boolean aBoolean, THSSDKError thssdkError) {
        SDKError sdkError=null;
        if(thssdkError!=null) {
            sdkError = thssdkError.getSdkError();
        }
        if (null != sdkError) {
            mThsInitFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTIC_CONSUMER_EXIST_CHECK, sdkError));
            return;
        }
        if (aBoolean) {
            authenticateUser();
        } else {
            mThsInitFragment.hideProgressBar();
            launchOnBoardingScreen();
        }
    }

    private void authenticateUser() {
        try {
            THSManager.getInstance().authenticateMutualAuthToken(mThsInitFragment.getContext(),this);
            // THSManager.getInstance().authenticate(uiBaseView.getContext(),"rohit.nihal@philips.com","Philips@123",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.showToast(R.string.ths_se_server_error_toast_message);
    }


    private void launchOnBoardingScreen() {
        mThsInitFragment.popSelfBeforeTransition();
        OnBoardingFragment thsOnBoardingFragment = new OnBoardingFragment();
        mThsInitFragment.addFragment(thsOnBoardingFragment, OnBoardingFragment.TAG, null, false);
    }


    private void launchPreWelcomeScreen() {
        mThsInitFragment.popSelfBeforeTransition();
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        mThsInitFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null, false);
    }

    protected void launchWelcomeScreen() {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.popSelfBeforeTransition();
        THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
        mThsInitFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError sdkError) {

        if (sdkError.getSdkError() != null && sdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            if (checkIfRefreshTokenWasTriedBefore()) {
                mThsInitFragment.showError(mThsInitFragment.getString(R.string.ths_user_not_authenticated));
                return;
            }
            isRefreshTokenRequestedBefore = true;
            refreshToken();
            return;
        }

        try {
            if (thsAuthentication.needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(mThsInitFragment.getContext(), thsAuthentication, this);
            } else {
                THSManager.getInstance().getConsumerObject(mThsInitFragment.getFragmentActivity(), thsAuthentication.getAuthentication(), this);
            }
        } catch (AWSDKInstantiationException e) {

        }
    }

    private void refreshToken() {
        THSManager.getInstance().getUser(mThsInitFragment.getContext()).refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                authenticateUser();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int i) {
                mThsInitFragment.showError("Refresh Signon failed with the following status code " + i + " please logout and login again");
                mThsInitFragment.hideProgressBar();
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {

            }
        });
    }

    @Override
    public void onLoginFailure(Throwable var1) {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.showToast(R.string.ths_se_server_error_toast_message);
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        mThsInitFragment.hideProgressBar();
        if (sdkError == null) {
           launchWelcomeScreen();
//            launchOnBoardingScreen();
        } else {
            mThsInitFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_CONSUMER_DETAILS, sdkError));
        }

    }

    @Override
    public void onError(Throwable throwable) {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.showToast(R.string.ths_se_server_error_toast_message);
    }


    private boolean checkIfRefreshTokenWasTriedBefore() {
        if (isRefreshTokenRequestedBefore) {
            isRefreshTokenRequestedBefore = false;
            mThsInitFragment.hideProgressBar();
            if (mThsInitFragment.getActivity() != null) {
                mThsInitFragment.doTagging(ANALYTICS_INITIALIZATION, mThsInitFragment.getString(R.string.ths_user_not_authenticated), false);
            }
            return true;
        }
        return false;
    }

}
