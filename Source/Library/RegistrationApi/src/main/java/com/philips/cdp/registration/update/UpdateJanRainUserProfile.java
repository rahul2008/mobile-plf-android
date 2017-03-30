package com.philips.cdp.registration.update;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureRecord;

import org.json.JSONException;

public class UpdateJanRainUserProfile implements UpdateUserProfile {

    private static final String JANRAIN_UPDATE_EMAIL_KEY = "email";
    private static final String EDIT_PROFILE_FORM_NAME = "editProfileForm";

    @Override
    public void updateUserEmail(String emailId, CaptureApiRequestCallback captureApiRequestCallback) {
        CaptureRecord currentUser = Jump.getSignedInUser();
        try {
            currentUser.put(JANRAIN_UPDATE_EMAIL_KEY, emailId);
            Capture.updateUserProfile(currentUser, EDIT_PROFILE_FORM_NAME, captureApiRequestCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
