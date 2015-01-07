package com.philips.cl.di.reg.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.AddConsumerInterestHandler;

public class AddConsumerInterest implements CaptureApiRequestCallback {

	private AddConsumerInterestHandler mAddConsumerInterest;

	public AddConsumerInterest(AddConsumerInterestHandler addConsumerInterest) {
		mAddConsumerInterest = addConsumerInterest;
	}

	public void onSuccess() {

		mAddConsumerInterest.onAddConsumerInterestSuccess();
	}

	public void onFailure(CaptureApiError error) {

		FailureErrorMaping errorMapping = new FailureErrorMaping(null, error,
				null);
		int getError = errorMapping.checkCaptureApiError();
		mAddConsumerInterest.onAddConsumerInterestFailedWithError(getError);
	}
}