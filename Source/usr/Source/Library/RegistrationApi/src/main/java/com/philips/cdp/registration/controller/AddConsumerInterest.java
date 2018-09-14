
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
import com.philips.cdp.registration.ui.utils.RLog;

public class AddConsumerInterest implements CaptureApiRequestCallback {
	private final static String TAG = AddConsumerInterest.class.getSimpleName();

	AddConsumerInterestHandler mAddConsumerInterest;

	public AddConsumerInterest(AddConsumerInterestHandler addConsumerInterest) {
		mAddConsumerInterest = addConsumerInterest;
	}

	public void onSuccess() {

		mAddConsumerInterest.onAddConsumerInterestSuccess();
	}

	public void onFailure(CaptureApiError error) {
		RLog.e(TAG,"AddConsumerInterest onFailure error: "+ error.raw_response);

		mAddConsumerInterest.onAddConsumerInterestFailedWithError(error.code);
	}
}
