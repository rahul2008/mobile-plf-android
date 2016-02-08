
package com.philips.cdp.registration.handlers;

public interface ProductRegistrationHandler {

	public void onRegisterSuccess(String response);

	public void onRegisterFailedWithFailure(int error);
}
