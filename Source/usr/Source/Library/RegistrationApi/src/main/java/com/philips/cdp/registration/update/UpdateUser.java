package com.philips.cdp.registration.update;

import android.util.Log;

import com.janrain.android.capture.*;

import org.json.JSONObject;

public class UpdateUser implements Capture.CaptureApiRequestCallback {

    private UpdateUserListener mUpdateUserListener;

    public interface UpdateUserListener {

        void onUserUpdateSuccess();

        void onUserUpdateFailed(int error);
    }

    public void update(JSONObject updatedUserData, JSONObject userData,
                       UpdateUserListener updateUserListener) {
        mUpdateUserListener = updateUserListener;
        if (null != updatedUserData && null != userData) {
            try {
                ((CaptureRecord) updatedUserData).synchronize(this, userData);
            } catch (Capture.InvalidApidChangeException e) {
                mUpdateUserListener.onUserUpdateFailed(-1);
            }
        } else {
            mUpdateUserListener.onUserUpdateFailed(-1);
        }
    }

    @Override
    public void onSuccess() {
        mUpdateUserListener.onUserUpdateSuccess();
    }

    @Override
    public void onFailure(CaptureApiError e) {
        mUpdateUserListener.onUserUpdateFailed(e.code);
    }
}
