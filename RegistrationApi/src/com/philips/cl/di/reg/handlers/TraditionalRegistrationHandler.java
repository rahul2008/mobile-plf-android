
package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.CreateAccountFailuerInfo;

public interface TraditionalRegistrationHandler {

	public void onRegisterSuccess();

	public void onRegisterFailedWithFailure(CreateAccountFailuerInfo createAccountFailuerInfo);
}
