package com.philips.cl.di.reg.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;

public class RegisterTraditional implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private Context mContext;

	private TraditionalRegistrationHandler mTraditionalRegisterHandler;
	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public RegisterTraditional(
			TraditionalRegistrationHandler traditionalRegisterHandler,
			Context context,UpdateUserRecordHandler updateUserRecordHandler) {
		mTraditionalRegisterHandler = traditionalRegisterHandler;
		mContext = context;
		mUpdateUserRecordHandler =updateUserRecordHandler ;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordRegister();
		mTraditionalRegisterHandler.onRegisterSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null,
				null);
		int errorCondition = errorMapping.checkSignInError();
		mTraditionalRegisterHandler.onRegisterFailedWithFailure(errorCondition);
	}

}
