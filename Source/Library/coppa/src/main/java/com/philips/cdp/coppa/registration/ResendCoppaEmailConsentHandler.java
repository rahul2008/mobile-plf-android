
package com.philips.cdp.coppa.registration;

public interface ResendCoppaEmailConsentHandler {

	void didResendCoppaEmailConsentSucess();

	void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError);

}
