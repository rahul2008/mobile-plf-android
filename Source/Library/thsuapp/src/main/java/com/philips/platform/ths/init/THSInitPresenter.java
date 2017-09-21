/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

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
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSInitializeCallBack;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.ths.utility.THSConstants.THS_USER_NOT_LOGGED_IN;

public class THSInitPresenter implements THSBasePresenter, THSInitializeCallBack<Void,THSSDKError>, THSCheckConsumerExistsCallback<Boolean, THSSDKError>, THSLoginCallBack<THSAuthentication,THSSDKError>,THSGetConsumerObjectCallBack {

    THSInitFragment mThsInitFragment;

    THSInitPresenter(THSInitFragment thsInitFragment) {
        mThsInitFragment = thsInitFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void initializeAwsdk() {
        User user = new User(mThsInitFragment.getContext());
        if (user == null || !user.isUserSignIn()) {
            mThsInitFragment.hideProgressBar();
            showError();
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
        }
    }

    @Override

    public void onInitializationResponse(Void aVoid, THSSDKError sdkError) {
        checkForUserExisitance();
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        mThsInitFragment.hideProgressBar();
        if (mThsInitFragment.getContext() != null) {
            (mThsInitFragment).showToast("Init Failed!!!!!");
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
        SDKError sdkError = thssdkError.getSdkError();
        if (null != sdkError) {
            mThsInitFragment.hideProgressBar();
            if (null != sdkError.getSDKErrorReason()) {
                mThsInitFragment.showToast(sdkError.getSDKErrorReason().name());
            }
            return;
        }

        if (aBoolean) {
            authenticateUser();
        } else {
            mThsInitFragment.hideProgressBar();
            mThsInitFragment.popSelfBeforeTransition();
            launchPreWelcomeScreen();
        }
    }

    private void authenticateUser() {
        try {
            THSManager.getInstance().authenticateMutualAuthToken(mThsInitFragment.getContext(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

   /* private void launchPreWelcomeScreen(){
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        mThsInitFragment.addFragment(thsPreWelcomeFragment,THSPreWelcomeFragment.TAG,null);
    }*/

    private void launchPreWelcomeScreen() {

        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        mThsInitFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null);
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError sdkError) {
        if (sdkError.getSdkError() != null && sdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            refreshToken();
            return;
        }

        try {
            if (thsAuthentication.needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(mThsInitFragment.getContext(), thsAuthentication, this);
            } else {
                THSManager.getInstance().getConsumerObject(mThsInitFragment.getContext(), thsAuthentication.getAuthentication(), this);
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
                mThsInitFragment.showToast("Refresh Signon failed with the following status code " + i + " please logout and login again");
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
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        launchWelcomeScreen();
    }

    private void launchWelcomeScreen() {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.popSelfBeforeTransition();
        THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
        mThsInitFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null);
    }

    @Override
    public void onError(Throwable throwable) {
        mThsInitFragment.hideProgressBar();
    }

    public void showError() {
        AlertDialogFragment alertDialogFragmentUserNotLoggedIn = (AlertDialogFragment) mThsInitFragment.getFragmentManager().findFragmentByTag(THS_USER_NOT_LOGGED_IN);
        final String message = mThsInitFragment.getString(R.string.ths_user_not_logged_in);
        if (null != alertDialogFragmentUserNotLoggedIn) {
            alertDialogFragmentUserNotLoggedIn.dismiss();
        }

        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mThsInitFragment.getFragmentActivity());
        builder.setMessage(message);
        builder.setTitle(mThsInitFragment.getResources().getString(R.string.ths_matchmaking_error));

        alertDialogFragmentUserNotLoggedIn = builder.setCancelable(false).create();
        View.OnClickListener onClickListener = getOnClickListener(alertDialogFragmentUserNotLoggedIn);
        builder.setPositiveButton(mThsInitFragment.getResources().getString(R.string.ths_matchmaking_ok_button), onClickListener);
        alertDialogFragmentUserNotLoggedIn.setPositiveButtonListener(onClickListener);
        alertDialogFragmentUserNotLoggedIn.show(mThsInitFragment.getFragmentManager(), THS_USER_NOT_LOGGED_IN);
    }

    @NonNull
    private View.OnClickListener getOnClickListener(final AlertDialogFragment finalAlertDialogFragmentStartVisit) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThsInitFragment.popFragmentByTag(THSInitFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                finalAlertDialogFragmentStartVisit.dismiss();
            }
        };
    }
}
