
package com.philips.cl.di.reg.coppa;

public interface ResendCoppaEmailConsentHandler {

	void didResendCoppaEmailConsentSucess();

	void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError);

}
