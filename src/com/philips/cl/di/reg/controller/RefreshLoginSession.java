package com.philips.cl.di.reg.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.RefreshLoginSessionHandler;

public class RefreshLoginSession implements CaptureApiRequestCallback {
	private RefreshLoginSessionHandler mRefreshLoginSessionHandler;

	public RefreshLoginSession(
			RefreshLoginSessionHandler refreshLoginSessionHandler) {
		mRefreshLoginSessionHandler = refreshLoginSessionHandler;
	}

	public void onSuccess() {
		mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();
	}

	public void onFailure(CaptureApiError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(null, error,
				null);
		int getError = errorMapping.checkCaptureApiError();
		mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(getError);
	}
}
