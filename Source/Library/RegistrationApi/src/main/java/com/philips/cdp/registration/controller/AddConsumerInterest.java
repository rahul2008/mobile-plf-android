
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;

public class AddConsumerInterest implements CaptureApiRequestCallback {

	private AddConsumerInterestHandler mAddConsumerInterest;

	public AddConsumerInterest(AddConsumerInterestHandler addConsumerInterest) {
		mAddConsumerInterest = addConsumerInterest;
	}

	public void onSuccess() {

		mAddConsumerInterest.onAddConsumerInterestSuccess();
	}

	public void onFailure(CaptureApiError error) {
		mAddConsumerInterest.onAddConsumerInterestFailedWithError(error.code);
	}
}
