
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
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword implements Jump.ForgotPasswordResultHandler, JumpFlowDownloadStatusListener {

    private ForgotPasswordHandler mForgotPaswordHandler;
    private Context mContext;

    public ForgotPassword(final Context context, ForgotPasswordHandler forgotPaswordHandler) {
        mForgotPaswordHandler = forgotPaswordHandler;
        mContext = context;
    }

    @Override
    public void onSuccess() {
        ThreadUtils.postInMainThread(mContext, () ->
                mForgotPaswordHandler.onSendForgotPasswordSuccess());

    }

    @Override
    public void onFailure(ForgetPasswordError error) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setError(error.captureApiError);
        handleOnlySocialSignIn(error.captureApiError, userRegistrationFailureInfo);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        ThreadUtils.postInMainThread(mContext, () ->
                mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo));
    }

    private void handleOnlySocialSignIn(CaptureApiError error,
                                        UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.code == RegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE) {
            try {
                JSONObject object = error.raw_response;
                if (!object.isNull(RegConstants.MESSAGE)) {
                    userRegistrationFailureInfo.setErrorDescription(object
                            .getString(RegConstants.MESSAGE));
                }
            } catch (JSONException e) {
                //NOP
            }
        }
    }


    private String mEmailAddress;

    public void performForgotPassword(final String emailAddress) {
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
        Jump.performForgotPassword(mEmailAddress, this);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        if (mForgotPaswordHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorCode(RegConstants.FORGOT_PASSWORD_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();


    }
}