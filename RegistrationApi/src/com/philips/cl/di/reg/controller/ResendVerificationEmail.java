package com.philips.cl.di.reg.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;

public class ResendVerificationEmail implements CaptureApiRequestCallback {
    private ResendVerificationEmailHandler resendVerificationEmail;

	public ResendVerificationEmail(ResendVerificationEmailHandler resendVerificationEmail) {
       this.resendVerificationEmail = resendVerificationEmail;
	}
	public void onSuccess() {
       resendVerificationEmail.onResendVerificationEmailSuccess();

	}
	public void onFailure(CaptureApiError error) {
       FailureErrorMaping errorMapping = new FailureErrorMaping(null, error, null);
       int getError = errorMapping.checkCaptureApiError();
       resendVerificationEmail.onResendVerificationEmailFailedWithError(getError);
	}
}
