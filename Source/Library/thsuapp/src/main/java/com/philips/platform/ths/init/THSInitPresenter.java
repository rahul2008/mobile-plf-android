/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.User;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSInitializeCallBack;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class THSInitPresenter implements THSBasePresenter, THSInitializeCallBack<Void,THSSDKError>, THSCheckConsumerExistsCallback<Boolean, THSSDKError>{

    THSInitFragment mThsInitFragment;

    THSInitPresenter(THSInitFragment thsInitFragment) {
        mThsInitFragment = thsInitFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void initializeAwsdk() {
        User user = THSManager.getInstance().getUser(mThsInitFragment.getContext());
        if (user == null || !user.isUserSignIn()) {
            mThsInitFragment.hideProgressBar();
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
        }
    }

    @Override

    public void onInitializationResponse(Void aVoid, THSSDKError sdkError) {
        /*if(sdkError.getSdkError() != null){
            if(null!=mThsInitFragment && mThsInitFragment.isFragmentAttached()) {
                if(sdkError.getSDKErrorReason() != null) {
                    mThsInitFragment.showError(THSSDKErrorFactory.getErrorType(sdkError.getSDKErrorReason()));
                    return;
                }else {
                    mThsInitFragment.showError(THSConstants.THS_GENERIC_SERVER_ERROR);
                }
            }
        }else {
            checkForUserExisitance();
        }*/
        launchWelcomeScreen();
    }

    @Override
    public void onInitializationFailure(Throwable var1) {
        if(null!=mThsInitFragment && mThsInitFragment.isFragmentAttached()) {
            mThsInitFragment.hideProgressBar();
            if (mThsInitFragment.getContext() != null) {
                (mThsInitFragment).showToast(R.string.ths_se_server_error_toast_message);
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
        SDKError sdkError = thssdkError.getSdkError();
        mThsInitFragment.hideProgressBar();
        if (null != sdkError) {
            if (null != sdkError.getSDKErrorReason()) {
                mThsInitFragment.showError(THSSDKErrorFactory.getErrorType(sdkError.getSDKErrorReason()));
            }
            else if(sdkError.getHttpResponseCode() > 0){
                mThsInitFragment.showError(THSConstants.THS_GENERIC_SERVER_ERROR);
            }
            return;
        }
        if (aBoolean) {
            launchWelcomeScreen();
        } else {
            mThsInitFragment.hideProgressBar();
            mThsInitFragment.popSelfBeforeTransition();
            launchPreWelcomeScreen();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.showToast(R.string.ths_se_server_error_toast_message);
    }



    private void launchPreWelcomeScreen() {
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        mThsInitFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null, false);
    }

    private void launchWelcomeScreen() {
        mThsInitFragment.hideProgressBar();
        mThsInitFragment.popSelfBeforeTransition();
        THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
        mThsInitFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
    }


}
