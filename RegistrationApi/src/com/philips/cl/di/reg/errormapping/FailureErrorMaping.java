package com.philips.cl.di.reg.errormapping;

import com.janrain.android.Jump.ForgotPasswordResultHandler.ForgetPasswordError;
import com.janrain.android.Jump.SignInResultHandler.SignInError;
import com.janrain.android.capture.CaptureApiError;

public class FailureErrorMaping {
	private SignInError mError;
	private ForgetPasswordError mForgetpassworderror;
	private CaptureApiError mCaptureApiError;
	public int mCode = 0;

	public FailureErrorMaping(SignInError error,CaptureApiError captureApiError,ForgetPasswordError forgetpassworderror) {
		mError = error;
		mForgetpassworderror = forgetpassworderror;
		mCaptureApiError = captureApiError;
		}

	public int checkSignInError() {
		
		if (mError.reason == SignInError.FailureReason.AUTHENTICATION_CANCELED_BY_USER) {
			mCode = Error.AUTHENTICATION_CANCELED_BY_USER.geterrorList();
		} 
		else if (mError.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& mError.captureApiError.isMergeFlowError()) {
			mCode = Error.MERGE_FLOW_ERROR.geterrorList();
		} else if (mError.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& mError.captureApiError.isTwoStepRegFlowError()) {
			mCode = Error.TWO_STEP_ERROR.geterrorList();
		} else if (mError.reason == SignInError.FailureReason.JUMP_NOT_INITIALIZED) {
			mCode = Error.GENERIC_ERROR.geterrorList();
		} else if (mError.reason == SignInError.FailureReason.INVALID_PASSWORD) {
			mCode = Error.INVALID_PARAM.geterrorList();
		} 
		else if ((mError.reason.toString()).equals("ENGAGE_ERROR")) {
			  mCode = Error.ENGAGE_ERROR.geterrorList();
			}
		else if (mError.captureApiError.code == -1) {
			mCode = Error.NO_NETWORK_CONNECTION.geterrorList();
		}
		else if (mError.captureApiError.code == 210) {
			mCode = Error.INVALID_USERNAME_OR_PASSWORD.geterrorList();
		} else if (mError.captureApiError.code == 390) {
			mCode = Error.EMAIL_ALREADY_EXIST.geterrorList();
		}else if (mError.captureApiError.code == -6) {
			mCode = Error.INVALID_USERNAME_OR_PASSWORD.geterrorList();
		} else if (mError.reason == SignInError.FailureReason.ENGAGE_ERROR) {
			mCode = Error.GENERIC_ERROR.geterrorList();
		} else {
			mCode = Error.GENERIC_ERROR.geterrorList();
		}
		return mCode;
	}

	public int checkFogetPassWordError() {

		if (mForgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_CAPTURE_API_ERROR) {
			mCode = Error.GENERIC_ERROR.geterrorList();
		} else if (mForgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_JUMP_NOT_INITIALIZED) {
			mCode = Error.GENERIC_ERROR.geterrorList();
		} else if (mForgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_FORM_NAME_NOT_INITIALIZED) {
			mCode = Error.INVALID_PARAM.geterrorList();
		} else if (mError.captureApiError.code == 212) {
			mCode = Error.ACCOUNT_DOESNOT_EXIST.geterrorList();
		} else if (mForgetpassworderror.reason == ForgetPasswordError.FailureReason.FORGOTPASSWORD_INVALID_EMAILID) {
			mCode = Error.INVALID_EMAILID.geterrorList();
		} else {
			mCode = Error.GENERIC_ERROR.geterrorList();
		}
		return mCode;
	}

	public int checkCaptureApiError() {
		if (mCaptureApiError == CaptureApiError.INVALID_API_RESPONSE) {
			mCode = Error.GENERIC_ERROR.geterrorList();
		} else {
			mCode = Error.GENERIC_ERROR.geterrorList();
		}
		return mCode;
	}
}
