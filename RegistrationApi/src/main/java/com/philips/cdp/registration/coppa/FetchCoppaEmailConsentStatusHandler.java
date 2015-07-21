
package com.philips.cdp.registration.coppa;

import com.janrain.android.Jump.CaptureApiResultHandler.CaptureAPIError;

public interface FetchCoppaEmailConsentStatusHandler {

	void didCoppaStatusFetchingSucess(CoppaStatus coppaStatus);

	void didCoppaStatusFectchingFailedWIthError(CaptureAPIError failureParam);

}
