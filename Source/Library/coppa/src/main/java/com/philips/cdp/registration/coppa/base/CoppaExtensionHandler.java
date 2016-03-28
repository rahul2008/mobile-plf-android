
package com.philips.cdp.registration.coppa.base;

import android.content.Context;

public interface CoppaExtensionHandler {

	CoppaStatus getCoppaEmailConsentStatus();

	void fetchCoppaEmailConsentStatus(Context context, FetchCoppaEmailConsentStatusHandler handler);

	void resendCoppaEmailConsentForUserEmail(String email, ResendCoppaEmailConsentHandler resendCoppaEmailConsentHandler);

}
