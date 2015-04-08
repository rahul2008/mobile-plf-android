package com.philips.cl.di.reg.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;

public class ResendVerificationEmail implements CaptureApiRequestCallback {
    private ResendVerificationEmailHandler mResendVerificationEmail;

	public ResendVerificationEmail(ResendVerificationEmailHandler resendVerificationEmail) {
       mResendVerificationEmail = resendVerificationEmail;
	}
	public void onSuccess() {
       mResendVerificationEmail.onResendVerificationEmailSuccess();

	}
	public void onFailure(CaptureApiError error) {
       FailureErrorMaping errorMapping = new FailureErrorMaping(null, error, null);
       int getError = errorMapping.checkCaptureApiError();
       mResendVerificationEmail.onResendVerificationEmailFailedWithError(getError);
	}
}
