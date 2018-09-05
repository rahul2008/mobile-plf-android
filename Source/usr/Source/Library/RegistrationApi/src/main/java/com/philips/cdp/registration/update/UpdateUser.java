package com.philips.cdp.registration.update;

import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.ui.utils.RLog;

import org.json.JSONObject;

public class UpdateUser implements Capture.CaptureApiRequestCallback {

    private String TAG = UpdateUser.class.getSimpleName();

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
                RLog.d(TAG, "updating User  ");
                ((CaptureRecord) updatedUserData).synchronize(this, userData);
            } catch (Capture.InvalidApidChangeException e) {
                RLog.e(TAG, "Exception occured while updating User Info ");
                mUpdateUserListener.onUserUpdateFailed(ErrorCodes.UNKNOWN_ERROR);
            }
        } else {
            RLog.e(TAG, "update: updatedUserData NULL ");
            mUpdateUserListener.onUserUpdateFailed(ErrorCodes.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onSuccess() {
        RLog.d(TAG, "onSuccess  ");
        mUpdateUserListener.onUserUpdateSuccess();
    }

    @Override
    public void onFailure(CaptureApiError e) {
        RLog.e(TAG, "onFailure updating User Info " + e.code);
        mUpdateUserListener.onUserUpdateFailed(e.code);
    }
}
