package com.philips.cl.di.reg.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.RefreshLoginSessionHandler;

public class RefreshLoginSession implements CaptureApiRequestCallback {
	private RefreshLoginSessionHandler refreshLoginSessionHandler;

	public RefreshLoginSession(
			RefreshLoginSessionHandler refreshLoginSessionHandler) {
		this.refreshLoginSessionHandler = refreshLoginSessionHandler;
	}

	public void onSuccess() {
		this.refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
	}

	public void onFailure(CaptureApiError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(null, error,
				null);
		int getError = errorMapping.checkCaptureApiError();
		this.refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(getError);
	}
}
