package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.VisibleForTesting;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.update.UpdateUserProfile;

import javax.inject.Inject;

public class AddSecureEmailPresenter implements CaptureApiRequestCallback {

    @Inject
    UpdateUserProfile updateUserProfile;

    AddSecureEmailContract addSecureEmailContract;

    public AddSecureEmailPresenter(AddSecureEmailContract addSecureEmailContract) {
        URInterface.getComponent().inject(this);
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
        updateUserProfile.updateUserEmail(emailId, this);
    }

    @Override
    public void onSuccess() {
        addSecureEmailContract.onAddRecoveryEmailSuccess();
    }

    @Override
    public void onFailure(CaptureApiError e) {
        addSecureEmailContract.onAddRecoveryEmailFailure(e.error);
    }

    @VisibleForTesting
    @Deprecated
    public void injectMocks(UpdateUserProfile updateUserProfile) {
        this.updateUserProfile = updateUserProfile;
    }
}
