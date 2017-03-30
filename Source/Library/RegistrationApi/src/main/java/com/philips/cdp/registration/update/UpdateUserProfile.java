package com.philips.cdp.registration.update;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;

public interface UpdateUserProfile {

    void updateUserEmail(String emailId, CaptureApiRequestCallback captureApiRequestCallback);
}
