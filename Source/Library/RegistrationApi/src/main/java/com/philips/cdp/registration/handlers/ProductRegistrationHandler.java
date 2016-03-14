
package com.philips.cdp.registration.handlers;

public interface ProductRegistrationHandler {

	void onRegisterSuccess(String response);

	void onRegisterFailedWithFailure(int error);

	void onRefreshLoginSessionInProgress(String message);
}
