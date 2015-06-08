
package com.philips.cl.di.reg.handlers;

public interface RefreshLoginSessionHandler {

	public void onRefreshLoginSessionSuccess();

	public void onRefreshLoginSessionFailedWithError(int error);
}
