
package com.philips.cdp.registration.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

public class RefreshLoginSession implements CaptureApiRequestCallback {

	private RefreshLoginSessionHandler mRefreshLoginSessionHandler;

	public RefreshLoginSession(RefreshLoginSessionHandler refreshLoginSessionHandler) {
		mRefreshLoginSessionHandler = refreshLoginSessionHandler;
	}

	public void onSuccess() {
		mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();
	}

	public void onFailure(CaptureApiError error) {
		mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error.code);
	}
}
