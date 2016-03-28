
package com.philips.cdp.registration.coppa.base;

public interface ResendCoppaEmailConsentHandler {

	void didResendCoppaEmailConsentSucess();

	void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError);

}
