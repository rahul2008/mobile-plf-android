package com.philips.cl.di.reg.controller;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;

public class ForgotPassword implements Jump.ForgotPasswordResultHandler {
    private ForgotPasswordHandler mForgotPaswordHandler;

    public ForgotPassword(ForgotPasswordHandler forgotPaswordHandler) {
		mForgotPaswordHandler = forgotPaswordHandler;
	}
	@Override
	public void onSuccess() {
		mForgotPaswordHandler.onSendForgotPasswordSuccess();

	}
	@Override
	public void onFailure(ForgetPasswordError error) {
	    FailureErrorMaping errorMapping = new FailureErrorMaping(null, null, error);
		int getError = errorMapping.checkFogetPassWordError();
		mForgotPaswordHandler.onSendForgotPasswordFailedWithError(getError);
		}
	}
