
package com.philips.cdp.registration.handlers;

public interface RefreshLoginSessionHandler {

	public void onRefreshLoginSessionSuccess();

	public void onRefreshLoginSessionFailedWithError(int error);

}
