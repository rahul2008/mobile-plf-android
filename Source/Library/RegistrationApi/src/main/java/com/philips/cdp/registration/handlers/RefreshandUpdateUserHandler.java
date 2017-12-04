/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.controller.LoginTraditional;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONObject;

public class RefreshandUpdateUserHandler implements JumpFlowDownloadStatusListener {

    public UpdateUserRecordHandler mUpdateUserRecordHandler;
    private Context mContext;
    private User user;
    private String password;
    private RefreshUserHandler refreshUserHandler;

    public RefreshandUpdateUserHandler(UpdateUserRecordHandler updateUserRecordHandler, Context context) {
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mContext = context;
    }

    public void refreshAndUpdateUser(final RefreshUserHandler handler, final User user, final String password) {
        refreshUserHandler = handler;
        this.user = user;
        this.password = password;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated() && UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
            return;
        }

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated() && !UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
            return;
        }

        refreshUpdateUser(handler, user, password);
    }

    private void refreshUpdateUser(final RefreshUserHandler handler, final User user, final String password) {
        if (Jump.getSignedInUser() == null) {
            handler.onRefreshUserFailed(0);
            return;
        }
        Jump.performFetchCaptureData(new Jump.CaptureApiResultHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Jump.saveToDisk(mContext);
                if (!RegistrationConfiguration.getInstance().isHsdpFlow()) {
                    handler.onRefreshUserSuccess();
                    return;
                }

                if ((user.isEmailVerified() || user.isMobileVerified())) {
                    HsdpUser hsdpUser = new HsdpUser(mContext);
                    HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
                    if (hsdpUserRecord == null) {
                        LoginTraditional loginTraditional = new LoginTraditional(new TraditionalLoginHandler() {
                            @Override
                            public void onLoginSuccess() {
                                handler.onRefreshUserSuccess();
                            }

                            @Override
                            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                                handler.onRefreshUserFailed(RegConstants.HSDP_ACTIVATE_ACCOUNT_FAILED);
                            }
                        }, mContext, mUpdateUserRecordHandler, null, null);
                        loginTraditional.loginIntoHsdp();
                    } else {
                        handler.onRefreshUserSuccess();
                    }
                } else {
                    handler.onRefreshUserSuccess();
                }
            }

            @Override
            public void onFailure(CaptureAPIError failureParam) {
                if (failureParam.captureApiError.code == 414 && failureParam.captureApiError.error.equalsIgnoreCase("access_token_expired")) {

                    user.refreshLoginSession(new RefreshLoginSessionHandler() {
                        @Override
                        public void onRefreshLoginSessionSuccess() {
                            handler.onRefreshUserSuccess();
                            return;
                        }

                        @Override
                        public void onRefreshLoginSessionFailedWithError(int error) {
                            handler.onRefreshUserFailed(error);
                            return;
                        }

                        @Override
                        public void onRefreshLoginSessionInProgress(String message) {
                        }
                    });
                }
                handler.onRefreshUserFailed(0);
            }
        });
    }

    @Override
    public void onFlowDownloadSuccess() {
        refreshAndUpdateUser(refreshUserHandler, user, password);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }

    @Override
    public void onFlowDownloadFailure() {
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
        if (refreshUserHandler != null) {
            refreshUserHandler.onRefreshUserFailed(0);
        }

    }
}
