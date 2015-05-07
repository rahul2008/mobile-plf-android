
package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.SignInTraditionalFailuerInfo;

public interface TraditionalLoginHandler {

	public void onLoginSuccess();

	public void onLoginFailedWithError(SignInTraditionalFailuerInfo signInTraditionalFailuerInfo);
}
