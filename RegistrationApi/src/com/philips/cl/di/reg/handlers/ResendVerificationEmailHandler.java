package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.ResendMailFailureInfo;

public interface ResendVerificationEmailHandler {
	public void onResendVerificationEmailSuccess();
	public void onResendVerificationEmailFailedWithError(ResendMailFailureInfo resendMailFailureInfo);
}
