package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.VisibleForTesting;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.update.UpdateUserProfile;

import javax.inject.Inject;

public class AddSecureEmailPresenter implements CaptureApiRequestCallback, NetworStateListener {

    @Inject
    UpdateUserProfile updateUserProfile;

    AddSecureEmailContract addSecureEmailContract;

    public AddSecureEmailPresenter(AddSecureEmailContract addSecureEmailContract) {
        URInterface.getComponent().inject(this);
        this.addSecureEmailContract = addSecureEmailContract;
    }

    public void registerNetworkListener() {
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void maybeLaterClicked() {
        addSecureEmailContract.showWelcomeScreen();
    }

    public void addEmailClicked(String emailId) {
        if (!FieldsValidator.isValidEmail(emailId)) {
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

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            addSecureEmailContract.enableButtons();
            addSecureEmailContract.hideErrorMsg();
        } else {
            addSecureEmailContract.disableButtons();
            addSecureEmailContract.showErrorMsg();
        }
    }

    public void unRegisterNetworkListener() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @VisibleForTesting
    @Deprecated
    public void injectMocks(UpdateUserProfile updateUserProfile) {
        this.updateUserProfile = updateUserProfile;
    }


}

