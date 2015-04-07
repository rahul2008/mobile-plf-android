package com.philips.cl.di.reg.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;

public class LoginTraditional implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private Context mContext;
	private TraditionalLoginHandler mTraditionalLoginHandler;
	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler,
			Context context, UpdateUserRecordHandler updateUserRecordHandler,
			String email, String password) {
		mTraditionalLoginHandler = traditionalLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordLogin();
		mTraditionalLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping ea = new FailureErrorMaping(error, null, null);
		int errorCondition = ea.checkSignInError();
		mTraditionalLoginHandler.onLoginFailedWithError(errorCondition);
	}
}
