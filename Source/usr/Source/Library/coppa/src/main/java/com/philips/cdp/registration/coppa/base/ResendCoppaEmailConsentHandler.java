
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

public interface ResendCoppaEmailConsentHandler {

	void didResendCoppaEmailConsentSucess();

	void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError);

}