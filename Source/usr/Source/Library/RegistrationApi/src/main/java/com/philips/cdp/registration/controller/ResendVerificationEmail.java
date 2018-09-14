
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
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

public class ResendVerificationEmail implements CaptureApiRequestCallback, JumpFlowDownloadStatusListener {

    public ResendVerificationEmailHandler mResendVerificationEmail;
    private Context mContext;
    private String mEmailAddress;
    private static final String TAG = ResendVerificationEmail.class.getSimpleName();

    public ResendVerificationEmail(final Context context, final ResendVerificationEmailHandler resendVerificationEmail) {
        mResendVerificationEmail = resendVerificationEmail;
        mContext = context;
    }

    public void onSuccess() {
        RLog.d(TAG, "onSuccess : call onResendVerificationEmailSuccess ");
        ThreadUtils.postInMainThread(mContext, () ->
                mResendVerificationEmail.onResendVerificationEmailSuccess());

    }

    public void onFailure(CaptureApiError error) {
        try {
            RLog.e(TAG, "onFailure : call onResendVerificationEmailFailedWithError " + error.raw_response);
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error, mContext);
            userRegistrationFailureInfo.setErrorCode(error.code);
            ThreadUtils.postInMainThread(mContext, () ->
                    mResendVerificationEmail
                            .onResendVerificationEmailFailedWithError(userRegistrationFailureInfo));
        } catch (Exception e){
            RLog.e(TAG, "onFailure :  Exception " + e.getMessage());
        }
    }


    public void resendVerificationMail(final String emailAddress) {
        mEmailAddress = emailAddress;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.resendEmailVerification(emailAddress, this);
            return;
        }

        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }

    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG, "onFlowDownloadSuccess : call unregisterJumpFlowDownloadListener ");
        Jump.resendEmailVerification(mEmailAddress, this);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        if (mResendVerificationEmail != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.RESEND_MAIL_FAILED_SERVER_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.RESEND_MAIL_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mResendVerificationEmail.onResendVerificationEmailFailedWithError(userRegistrationFailureInfo));
            RLog.d(TAG, "onFlowDownloadFailure : call onResendVerificationEmailFailedWithError ");
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
        RLog.d(TAG, "onFlowDownloadFailure : call unregisterJumpFlowDownloadListener ");

    }
}
