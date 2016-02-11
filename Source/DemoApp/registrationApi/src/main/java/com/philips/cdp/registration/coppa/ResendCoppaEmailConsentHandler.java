
package com.philips.cdp.registration.coppa;

public interface ResendCoppaEmailConsentHandler {

	void didResendCoppaEmailConsentSucess();

	void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError);

}
