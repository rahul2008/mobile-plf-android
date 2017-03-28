package com.philips.cdp.registration.ui.traditional.mobile;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.ui.utils.FieldsValidator;

import org.json.JSONException;

public class AddSecureEmailPresenter implements CaptureApiRequestCallback {

    AddSecureEmailContract addSecureEmailContract;

    public AddSecureEmailPresenter(AddSecureEmailContract addSecureEmailContract) {
        this.addSecureEmailContract = addSecureEmailContract;
    }

    public void maybeLaterClicked() {
        addSecureEmailContract.showWelcomeScreen();
    }

    public void addEmailClicked(String emailId) {
        if(!FieldsValidator.isValidEmail(emailId)) {
            addSecureEmailContract.showInvalidEmailError();
            return;
        }

        updateUserEmail(emailId);

    }

    private void updateUserEmail(String emailId) {
        CaptureRecord currentUser = Jump.getSignedInUser();
        try {
            currentUser.put("email", emailId);
            Capture.updateUserProfile(currentUser, "editProfileForm", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess() {
        
    }

    @Override
    public void onFailure(CaptureApiError e) {

    }
}
