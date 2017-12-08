package com.philips.cdp.registration.update;

import com.janrain.android.Jump;
import com.janrain.android.capture.*;
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;

import org.json.JSONException;

import io.reactivex.Completable;

public class UpdateJanRainUserProfile implements UpdateUserProfile {

    private static final String JANRAIN_UPDATE_EMAIL_KEY = "email";
    private static final String EDIT_PROFILE_FORM_NAME = "editProfileForm";

    @Override
    public Completable updateUserEmail(String emailId) {
        return Completable.create(emitter -> {
            CaptureApiRequestCallback callback = new CaptureApiRequestCallback() {
                @Override
                public void onSuccess() {
                    emitter.onComplete();
                }

                @Override
                public void onFailure(CaptureApiError e) {
                    emitter.onError(new Throwable(e.error));
                }
            };
            updateUserEmail(emailId, callback);
        });
    }

    void updateUserEmail(String emailId, CaptureApiRequestCallback captureApiRequestCallback) {
        CaptureRecord currentUser = Jump.getSignedInUser();
        try {
            currentUser.put(JANRAIN_UPDATE_EMAIL_KEY, emailId);
            Capture.updateUserProfile(currentUser, EDIT_PROFILE_FORM_NAME, captureApiRequestCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
