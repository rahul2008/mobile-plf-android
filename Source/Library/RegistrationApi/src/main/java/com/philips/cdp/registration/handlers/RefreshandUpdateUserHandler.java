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
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.security.SecureStorage;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by 310202337 on 4/27/2016.
 */
public class RefreshandUpdateUserHandler implements JumpFlowDownloadStatusListener {

    private UpdateUserRecordHandler mUpdateUserRecordHandler;
    private Context mContext;
    private User user;
    private String password;
    private RefreshUserHandler refreshUserHandler;

    public RefreshandUpdateUserHandler(UpdateUserRecordHandler updateUserRecordHandler, Context context){
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mContext = context;
    }



    public void refreshAndUpdateUser( final RefreshUserHandler handler, final User user, final String password){
        refreshUserHandler = handler;
        this.user = user;
        this.password = password;
        if(!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }else{
            refreshUpdateUser(handler, user, password);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }

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
                if (!RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
                    handler.onRefreshUserSuccess();
                    return;
                }

                if (user.getEmailVerificationStatus()) {
                    DIUserProfile userProfile = getDIUserProfileFromDisk();
                    HsdpUser hsdpUser = new HsdpUser(mContext);
                    HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
                    if (userProfile != null && null != userProfile.getEmail() && null != password && hsdpUserRecord == null) {
                        LoginTraditional loginTraditional = new LoginTraditional(new TraditionalLoginHandler() {
                            @Override
                            public void onLoginSuccess() {
                                handler.onRefreshUserSuccess();
                            }

                            @Override
                            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                                handler.onRefreshUserFailed(RegConstants.HSDP_ACTIVATE_ACCOUNT_FAILED);
                            }
                        }, mContext, mUpdateUserRecordHandler, userProfile.getEmail(), password);
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

                System.out.println("Error " + failureParam.captureApiError);
                System.out.println("Error code" + failureParam.captureApiError.code);
                System.out.println("Error error " + failureParam.captureApiError.error);

                if (failureParam.captureApiError.code == 414 && failureParam.captureApiError.error.equalsIgnoreCase("access_token_expired")) {
                    //refresh login session

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

    private DIUserProfile getDIUserProfileFromDisk() {
        DIUserProfile diUserProfile = null;
        try {
            FileInputStream fis = mContext.openFileInput(RegConstants.DI_PROFILE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] enctText = (byte[]) ois.readObject();
            byte[] decrtext = SecureStorage.decrypt(enctText);
            diUserProfile = (DIUserProfile) SecureStorage.stringToObject(new String(decrtext));
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diUserProfile;
    }
    @Override
    public void onFlowDownloadSuccess() {
        refreshAndUpdateUser(refreshUserHandler,user,password);

        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }

    @Override
    public void onFlowDownloadFailure() {

        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }
}
