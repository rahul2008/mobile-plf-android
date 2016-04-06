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
