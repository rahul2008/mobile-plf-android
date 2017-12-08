
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

import com.janrain.android.Jump.CaptureApiResultHandler.CaptureAPIError;

public interface FetchCoppaEmailConsentStatusHandler {

	void didCoppaStatusFetchingSucess(CoppaStatus coppaStatus);

	void didCoppaStatusFectchingFailedWIthError(CaptureAPIError failureParam);

}
