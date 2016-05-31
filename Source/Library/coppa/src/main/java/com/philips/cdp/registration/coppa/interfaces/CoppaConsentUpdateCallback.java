/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.interfaces;

import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;

/**
 * Created by 310202337 on 3/25/2016.
 */
public interface CoppaConsentUpdateCallback {

    void onSuccess();

    void onFailure(final int message);
}
