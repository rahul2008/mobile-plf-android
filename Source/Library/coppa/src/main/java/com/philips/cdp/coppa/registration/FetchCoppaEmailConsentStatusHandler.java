
package com.philips.cdp.coppa.registration;

import com.janrain.android.Jump.CaptureApiResultHandler.CaptureAPIError;

public interface FetchCoppaEmailConsentStatusHandler {

	void didCoppaStatusFetchingSucess(CoppaStatus coppaStatus);

	void didCoppaStatusFectchingFailedWIthError(CaptureAPIError failureParam);

}
