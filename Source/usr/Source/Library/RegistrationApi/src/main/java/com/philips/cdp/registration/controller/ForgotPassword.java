
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.platform.appinfra.tagging.AppTaggingConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword implements Jump.ForgotPasswordResultHandler, JumpFlowDownloadStatusListener {

    private ForgotPasswordHandler mForgotPaswordHandler;
    private Context mContext;
    private static String TAG = ForgotPassword.class.getSimpleName();

    public ForgotPassword(final Context context, ForgotPasswordHandler forgotPaswordHandler) {
        mForgotPaswordHandler = forgotPaswordHandler;
        mContext = context;
    }

    @Override
    public void onSuccess() {
        ThreadUtils.postInMainThread(mContext, () ->
                mForgotPaswordHandler.onSendForgotPasswordSuccess());
        RLog.d(TAG,"onSuccess : is called ");
    }

    @Override
    public void onFailure(ForgetPasswordError error) {
        RLog.d(TAG,"onFailure : is called ");
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        handleOnlySocialSignIn(error.captureApiError, userRegistrationFailureInfo);
        ThreadUtils.postInMainThread(mContext, () ->
                mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo));
    }

    private void handleOnlySocialSignIn(CaptureApiError error,
                                        UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(TAG,"handleOnlySocialSignIn : is called");
        if (null != error && null != error.error
                && error.code == RegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE) {
            try {
                JSONObject object = error.raw_response;
                if (!object.isNull(RegConstants.MESSAGE)) {
                    userRegistrationFailureInfo.setErrorDescription(object
                            .getString(RegConstants.MESSAGE));
                }
            } catch (JSONException e) {
                RLog.d(TAG,"handleOnlySocialSignIn : "+e.getMessage());
            }
        }
    }


    private String mEmailAddress;

    public void performForgotPassword(final String emailAddress) {
        RLog.d(TAG,"performForgotPassword : is called");
        mEmailAddress = emailAddress;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            if (FieldsValidator.isValidEmail(emailAddress)) {
                Jump.performForgotPassword(emailAddress, this);
            }
            UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG,"onFlowDownloadSuccess : is called");
        Jump.performForgotPassword(mEmailAddress, this);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG,"onFlowDownloadFailure : is called");
        if (mForgotPaswordHandler != null) {
            RLog.d(TAG,"onFlowDownloadFailure : mForgotPaswordHandler is null");
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.FORGOT_PASSWORD_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();


    }
}
