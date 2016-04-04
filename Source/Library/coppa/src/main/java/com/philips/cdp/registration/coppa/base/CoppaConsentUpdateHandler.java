package com.philips.cdp.registration.coppa.base;

import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;

/**
 * Created by 310202337 on 3/28/2016.
 */
 class CoppaConsentUpdateHandler implements Capture.CaptureApiRequestCallback {

    private CoppaConsentUpdateCallback mCoppaConsentUpdateCallback;
    CoppaConsentUpdateHandler(final CoppaConsentUpdateCallback coppaConsentUpdateCallback){
        mCoppaConsentUpdateCallback = coppaConsentUpdateCallback;
    }

    @Override
    public void onSuccess() {
        mCoppaConsentUpdateCallback.onSuccess();

    }

    @Override
    public void onFailure(CaptureApiError e) {
        mCoppaConsentUpdateCallback.onFailure(e.error_description);

    }
}
