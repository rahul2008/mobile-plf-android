
package com.philips.cl.di.reg.coppa;

import android.content.Context;

public interface CoppaExtensionHandler {

	CoppaStatus getCoppaEmailConsentStatus();

	void fetchCoppaEmailConsentStatus(Context context, FetchCoppaEmailConsentStatusHandler handler);

	void resendCoppaEmailConsentForUserEmail(String email);

}
