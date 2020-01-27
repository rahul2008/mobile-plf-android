package com.philips.cdp.registration.ui.traditional.mobile;

import androidx.annotation.VisibleForTesting;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.cdp.registration.update.UpdateUserProfile;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class AddSecureEmailPresenter implements NetworkStateListener {

    @Inject
    UpdateUserProfile updateUserProfile;

    private final AddSecureEmailContract addSecureEmailContract;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public AddSecureEmailPresenter(AddSecureEmailContract addSecureEmailContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.addSecureEmailContract = addSecureEmailContract;
    }

    public void registerNetworkListener() {
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void maybeLaterClicked() {
        addSecureEmailContract.registrationComplete();
    }

    public void addEmailClicked(String emailId) {
        if (!FieldsValidator.isValidEmail(emailId)) {
            addSecureEmailContract.showInvalidEmailError();
            return;
        }
        addSecureEmailContract.showProgress();
        updateUserEmail(emailId);
    }

    private void updateUserEmail(String emailId) {
        disposables.add(updateUserProfile.updateUserEmail(emailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        addSecureEmailContract.storePreference(emailId);
                        addSecureEmailContract.hideProgress();
                        addSecureEmailContract.onAddRecoveryEmailSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        addSecureEmailContract.hideProgress();
                        addSecureEmailContract.onAddRecoveryEmailFailure(e.getMessage());
                    }
                }));
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            addSecureEmailContract.enableButtons();
            addSecureEmailContract.hideError();
        } else {
            addSecureEmailContract.disableButtons();
            addSecureEmailContract.showNetworkUnavailableError();
        }
    }


    public void cleanUp() {
        disposables.clear();
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @VisibleForTesting
    @Deprecated
    public void injectMocks(UpdateUserProfile updateUserProfile) {
        this.updateUserProfile = updateUserProfile;
    }


}

