
package com.philips.cdp.registration.handlers;

public interface UpdateConsumerInterestHandler {

	public void onUpdateConsumerInterestSuccess();

	public void onUpdateConsumerInterestFailedWithError( com.janrain.android.capture.CaptureApiError error);

}
