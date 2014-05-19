package com.philips.cl.di.reg.errormapping;

import com.janrain.android.Jump.ForgotPasswordResultHandler.ForgetPasswordError;
import com.janrain.android.Jump.SignInResultHandler.SignInError;
import com.janrain.android.capture.CaptureApiError;

public class FailureErrorMaping {
	private SignInError error;
	private ForgetPasswordError forgetpassworderror;
	private CaptureApiError captureApiError;
	public int code = 0;

	public FailureErrorMaping(SignInError error,CaptureApiError captureApiError,ForgetPasswordError forgetpassworderror) {
		this.error = error;
		this.forgetpassworderror = forgetpassworderror;
		this.captureApiError = captureApiError;
		}

	public int checkSignInError() {

		if (error.reason == SignInError.FailureReason.AUTHENTICATION_CANCELED_BY_USER) {
			code = Error.AUTHENTICATION_CANCELED_BY_USER.geterrorList();
		} else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isMergeFlowError()) {
			code = Error.MERGE_FLOW_ERROR.geterrorList();
		} else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isTwoStepRegFlowError()) {
			code = Error.TWO_STEP_ERROR.geterrorList();
		} else if (error.reason == SignInError.FailureReason.JUMP_NOT_INITIALIZED) {
			code = Error.GENERIC_ERROR.geterrorList();
		} else if (error.reason == SignInError.FailureReason.INVALID_PASSWORD) {
			code = Error.INVALID_PARAM.geterrorList();
		} else if (error.captureApiError.code == 210) {
			code = Error.INVALID_USERNAME_OR_PASSWORD.geterrorList();
		} else if (error.captureApiError.code == -6) {
			code = Error.INVALID_USERNAME_OR_PASSWORD.geterrorList();
		} else if (error.reason == SignInError.FailureReason.ENGAGE_ERROR) {
			code = Error.GENERIC_ERROR.geterrorList();
		} else {
			code = Error.GENERIC_ERROR.geterrorList();
		}
		return code;
	}

	public int checkFogetPassWordError() {

		if (forgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_CAPTURE_API_ERROR) {
			code = Error.GENERIC_ERROR.geterrorList();
		} else if (forgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_JUMP_NOT_INITIALIZED) {
			code = Error.GENERIC_ERROR.geterrorList();
		} else if (forgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_FORM_NAME_NOT_INITIALIZED) {
			code = Error.INVALID_PARAM.geterrorList();
		} else if (error.captureApiError.code == 212) {
			code = Error.ACCOUNT_DOESNOT_EXIST.geterrorList();
		} else if (forgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_INVALID_EMAILID) {
			code = Error.INVALID_EMAILID.geterrorList();
		} else {
			code = Error.GENERIC_ERROR.geterrorList();
		}
		return code;
	}

	public int checkCaptureApiError() {
		if (captureApiError == CaptureApiError.INVALID_API_RESPONSE) {
			code = Error.GENERIC_ERROR.geterrorList();
		} else {
			code = Error.GENERIC_ERROR.geterrorList();
		}
		return code;
	}
}
