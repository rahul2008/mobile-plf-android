
package com.philips.cdp.registration.handlers;

public interface RefreshLoginSessionHandler {

	void onRefreshLoginSessionSuccess();

	void onRefreshLoginSessionFailedWithError(int error);

	void onRefreshLoginSessionInProgress(String message);

}
