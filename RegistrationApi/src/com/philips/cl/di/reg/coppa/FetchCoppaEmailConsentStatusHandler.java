
package com.philips.cl.di.reg.coppa;

import com.janrain.android.Jump.CaptureApiResultHandler.CaptureAPIError;
import com.janrain.android.capture.CaptureApiError;

public interface FetchCoppaEmailConsentStatusHandler {

	void didCoppaStatusFetchingSucess(CoppaStatus coppaStatus);

	void didCoppaStatusFectchingFailedWIthError(CaptureAPIError failureParam);

}
