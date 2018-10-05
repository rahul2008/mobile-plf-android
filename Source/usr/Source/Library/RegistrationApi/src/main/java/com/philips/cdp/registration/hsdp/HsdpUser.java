/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Base64;

import com.janrain.android.Jump;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.app.tagging.Encryption;
import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.security.SecureStorage;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.util.Map;

import javax.inject.Inject;


/**
 * class hsdp user
 */
public class HsdpUser {

    private final LoggingInterface loggingInterface;
    private String TAG = "HsdpUser";
    private DhpAuthenticationResponse dhpAuthenticationResponse = null;

    @Inject
    HSDPConfiguration hsdpConfiguration;

    @Inject
    NetworkUtility networkUtility;

    private Context mContext;

    private final String SUCCESS_CODE = "200";

    private final String HSDP_RECORD_FILE = "hsdpRecord";

    private DhpResponse dhpResponse = null;


    /**
     * User file write listener interface
     */
    private interface UserFileWriteListener {
        void onFileWriteSuccess();

        void onFileWriteFailure();
    }

    /**
     * Class constructor
     *
     * @param context
     */
    public HsdpUser(Context context) {
        this.mContext = context;
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        loggingInterface = RegistrationConfiguration.getInstance().getComponent().getLoggingInterface();
    }

    /**
     * Get HSDP information for specified configuration
     *
     * @return HSDPInfo Object
     */
    private HSDPInfo getHSDPInfo() {

        String sharedId = hsdpConfiguration.getHsdpSharedId();

        String secreteId = hsdpConfiguration.getHsdpSecretId();

        String baseUrl = hsdpConfiguration.getHsdpBaseUrl();

        String appName = hsdpConfiguration.getHsdpAppName();

        RLog.d(TAG, "sharedId" + sharedId + "Secret " + secreteId + " baseUrl " + baseUrl);

        if (appName == null && sharedId == null && secreteId == null && baseUrl == null) {
            return null;
        }
        return new HSDPInfo(sharedId, secreteId, baseUrl, appName);
    }

    /**
     * Logout
     *
     * @param logoutHandler logout handler
     */
    public void logOut(final LogoutHandler logoutHandler) {
        if (networkUtility.isNetworkAvailable()) {
            final Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() -> {
                DhpAuthenticationManagementClient authenticationManagementClient
                        = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());

                dhpResponse = null;
                if (null != getHsdpUserRecord() && null != getHsdpUserRecord().getAccessCredential()) {
                    RLog.d(TAG, "logOut: is called from DhpAuthenticationManagementClient ");
                    dhpResponse = authenticationManagementClient.
                            logout(getHsdpUserRecord().getUserUUID(),
                                    getHsdpUserRecord().getAccessCredential().getAccessToken());
                }
                if (dhpResponse == null) {
                    RLog.e(TAG, "logOut:  dhpResponse is NULL");
                    handler.post(() -> ThreadUtils.postInMainThread(mContext, () ->
                            logoutHandler.
                                    onLogoutFailure(ErrorCodes.NETWORK_ERROR, mContext.
                                            getString(R.string.USR_Generic_Network_ErrorMsg))));
                } else {
                    if (dhpResponse.responseCode != null &&
                            dhpResponse.responseCode.equals(SUCCESS_CODE)) {
                        handler.post(() -> {
                            RLog.d(TAG, "logOut: onHsdsLogoutSuccess response"
                                    + dhpResponse.rawResponse.toString());
                            ThreadUtils.postInMainThread(mContext, logoutHandler::onLogoutSuccess);
                        });
                    } else {
                        if (dhpResponse.responseCode != null &&
                                (dhpResponse.responseCode.equals(RegConstants.
                                        INVALID_ACCESS_TOKEN_CODE) || dhpResponse.
                                        responseCode.equals(RegConstants.
                                        INVALID_REFRESH_TOKEN_CODE))) {
                            RLog.d(TAG, "logOut: onHsdsLogoutFailure : responseCode : "
                                    + dhpResponse.responseCode + " message : "
                                    + dhpResponse.message);
                            ThreadUtils.postInMainThread(mContext, () ->
                                    logoutHandler.onLogoutFailure(Integer.
                                                    parseInt(dhpResponse.responseCode),
                                            dhpResponse.message));
                        } else {
                            handler.post(() -> {
                                RLog.d(TAG, "logOut: onHsdsLogoutFailure : responseCode : " +
                                        dhpResponse.responseCode +
                                        " message : " + dhpResponse.message);
                                ThreadUtils.postInMainThread(mContext, () ->
                                        logoutHandler.onLogoutFailure(Integer.
                                                        parseInt(dhpResponse.responseCode),
                                                new URError(mContext).getLocalizedError(ErrorType.HSDP, Integer.parseInt(dhpResponse.responseCode))));
                            });
                        }
                    }
                }
            }).start();
        } else {
            RLog.e(TAG, "logOut : No Network Connection");
            ThreadUtils.postInMainThread(mContext, () ->
                    logoutHandler.onLogoutFailure(ErrorCodes.NO_NETWORK,
                            new URError(mContext).getLocalizedError(ErrorType.NETWOK, ErrorCodes.NO_NETWORK)));
        }
    }


    /**
     * Refresh token
     *
     * @param refreshHandler refresh handler
     */
    public void refreshToken(final RefreshLoginSessionHandler refreshHandler) {
        final Handler handler = new Handler(Looper.getMainLooper());
        if (networkUtility.isNetworkAvailable()) {
            new Thread(() -> {

                DhpAuthenticationManagementClient authenticationManagementClient =
                        new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                dhpAuthenticationResponse = null;
                if (getHsdpUserRecord() != null &&
                        getHsdpUserRecord().getAccessCredential() != null &&
                        getHsdpUserRecord().getAccessCredential().getRefreshToken() != null
                        ) {
                    dhpAuthenticationResponse = authenticationManagementClient.
                            refresh(getHsdpUserRecord().getUserUUID(),
                                    getHsdpUserRecord().getAccessCredential().getRefreshToken());
                } else if (getHsdpUserRecord() != null &&
                        null != getHsdpUserRecord().getUserUUID() &&
                        null != getHsdpUserRecord().getAccessCredential()) {
                    dhpAuthenticationResponse = authenticationManagementClient.
                            refreshSecret(getHsdpUserRecord().getUserUUID(),
                                    getHsdpUserRecord().getAccessCredential().
                                            getAccessToken(), getHsdpUserRecord().
                                            getRefreshSecret());
                }
                if (dhpAuthenticationResponse == null) {
                    handler.post(() -> ThreadUtils.postInMainThread(mContext, () ->
                            refreshHandler.
                                    onRefreshLoginSessionFailedWithError
                                            (ErrorCodes.NETWORK_ERROR)));
                } else if (null != dhpAuthenticationResponse.responseCode &&
                        dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                    if (null != getHsdpUserRecord() && null != getHsdpUserRecord().getAccessCredential()) {
                        getHsdpUserRecord().getAccessCredential().setRefreshToken
                                (dhpAuthenticationResponse.refreshToken);
                        getHsdpUserRecord().getAccessCredential().setAccessToken
                                (dhpAuthenticationResponse.accessToken);
                    }
                    saveToDisk(new UserFileWriteListener() {
                        @Override
                        public void onFileWriteSuccess() {
                        }

                        @Override
                        public void onFileWriteFailure() {
                        }
                    });
                    handler.post(() -> {
                        RLog.d(TAG, "onHsdpRefreshSuccess : response :" +
                                dhpAuthenticationResponse.rawResponse.toString());
                        ThreadUtils.postInMainThread(mContext, refreshHandler::onRefreshLoginSessionSuccess);
                    });
                } else {
                    if (dhpAuthenticationResponse.responseCode != null &&
                            dhpAuthenticationResponse.responseCode
                                    .equals(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                        handler.post(() -> {
                            RLog.d(TAG, "onHsdpRefreshFailure : responseCode : "
                                    + dhpAuthenticationResponse.responseCode +
                                    " message : " + dhpAuthenticationResponse.message);
                            ThreadUtils.postInMainThread(mContext, () ->
                                    refreshHandler.onRefreshLoginSessionFailedWithError(Integer
                                            .parseInt(dhpAuthenticationResponse.responseCode)));
                        });
                    } else {
                        handler.post(() -> {
                            RLog.d(TAG, "onHsdpRefreshFailure : responseCode : "
                                    + dhpAuthenticationResponse.responseCode +
                                    " message : " + dhpAuthenticationResponse.message);
                            ThreadUtils.postInMainThread(mContext, () ->
                                    refreshHandler.onRefreshLoginSessionFailedWithError(Integer.
                                            parseInt(dhpAuthenticationResponse.responseCode)));
                        });
                    }
                }
            }).start();
        } else {
            ThreadUtils.postInMainThread(mContext, () ->
                    refreshHandler.onRefreshLoginSessionFailedWithError(ErrorCodes.NO_NETWORK));
        }
    }

    /**
     * get dhp api client configuration
     *
     * @return DhpApiClientConfiguration object
     * {@link DhpApiClientConfiguration}
     */
    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        HSDPInfo hsdpInfo = getHSDPInfo();
        if (null != hsdpInfo && null != hsdpInfo.getBaseURL() && null !=
                hsdpInfo.getSecreteId() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getApplicationName()) {

            RLog.d(TAG, "Base URL " + hsdpInfo.getBaseURL());
            dhpApiClientConfiguration = new DhpApiClientConfiguration(hsdpInfo.getBaseURL(),
                    hsdpInfo.getApplicationName(), hsdpInfo.getSharedId(), hsdpInfo.getSecreteId());
        }
        return dhpApiClientConfiguration;
    }

    /**
     * Save to disk
     *
     * @param userFileWriteListener user file write listener
     */
    private void saveToDisk(UserFileWriteListener userFileWriteListener) {
        RLog.d(TAG, "Saving Hsdp record to secure storage");
        Parcel parcel = Parcel.obtain();
        getHsdpUserRecord().writeToParcel(parcel, 0);
        String parcelString = Base64.encodeToString(parcel.marshall(), Base64.DEFAULT);
        try {
            mContext.deleteFile(HSDP_RECORD_FILE);
            Jump.getSecureStorageInterface().storeValueForKey(HsdpUserRecordV2.SS_KEY_FOR_SAVING_RECORD,
                    parcelString, new SecureStorageInterface.SecureStorageError());
            userFileWriteListener.onFileWriteSuccess();
        } catch (Exception e) {
            userFileWriteListener.onFileWriteFailure();
        } finally {
            parcel.recycle();
        }
    }

    /**
     * Get hsdp user record
     *
     * @return HsdpUserRecordV2 object
     * {@link HsdpUserRecordV2}
     */
    public HsdpUserRecordV2 getHsdpUserRecord() {
        if (null != HsdpUserInstance.getInstance().getHsdpUserRecordV2()) {
            RLog.d(TAG, "getHsdpUserRecordV2 = " + HsdpUserInstance.getInstance().getHsdpUserRecordV2());
            return HsdpUserInstance.getInstance().getHsdpUserRecordV2();
        }
        //Check if HsdpRecord v2 is present in secure storage or not.
        RLog.d(TAG, "Checking if hsdp record v2 is present in SS or not?");
        if (Jump.getSecureStorageInterface().doesStorageKeyExist(HsdpUserRecordV2.SS_KEY_FOR_SAVING_RECORD)) {
            RLog.d(TAG, "Hsdp record v2 present");
            String hsdpRecord = Jump.getSecureStorageInterface().fetchValueForKey(HsdpUserRecordV2.SS_KEY_FOR_SAVING_RECORD,
                    new SecureStorageInterface.SecureStorageError());
            if (hsdpRecord != null) {
                byte[] hsdpRecordByteArray = Base64.decode(hsdpRecord, Base64.DEFAULT);
                RLog.d(TAG, "Unmarshalling hsdp record v2");
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(hsdpRecordByteArray, 0, hsdpRecordByteArray.length);
                parcel.setDataPosition(0);
                RLog.d(TAG, "Stting hsdp record v2");
                HsdpUserInstance.getInstance().setHsdpUserRecordV2(HsdpUserRecordV2.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
        } else {

            String hsdpRecord = Jump.getSecureStorageInterface().fetchValueForKey(HSDP_RECORD_FILE,
                    new SecureStorageInterface.SecureStorageError());
            RLog.d(TAG, "getHsdpUserRecordV2 hsdpRecord = " + hsdpRecord + " Not keeping in secure storage");
            if (hsdpRecord != null) {
                RLog.d(TAG, "Migrating hsdp record v1 to v2");
                Object obj = SecureStorage.stringToObject(hsdpRecord);
                if (obj instanceof HsdpUserRecord) {
                    final HsdpUserRecord hsdpUserRecord = (HsdpUserRecord) obj;
                    HsdpUserRecordV2 hsdpUserRecordV2 = new HsdpUserRecordV2();
                    hsdpUserRecordV2.setRefreshSecret(hsdpUserRecord.getRefreshSecret());
                    hsdpUserRecordV2.setUserUUID(hsdpUserRecord.getUserUUID());
                    HsdpUserRecordV2.AccessCredential accessCredential = hsdpUserRecordV2.new AccessCredential();
                    accessCredential.setRefreshToken(hsdpUserRecord.getAccessCredential().getRefreshToken());
                    accessCredential.setAccessToken(hsdpUserRecord.getAccessCredential().getAccessToken());
                    hsdpUserRecordV2.setAccessCredential(accessCredential);
                    HsdpUserInstance.getInstance().setHsdpUserRecordV2(hsdpUserRecordV2);
                    if (loggingInterface != null) {
                        loggingInterface.setHSDPUserUUID(hsdpUserRecordV2.getUserUUID());
                    }
                    sendEncryptedUUIDToAnalytics(hsdpUserRecordV2);
                    saveToDisk(new UserFileWriteListener() {
                        @Override
                        public void onFileWriteSuccess() {
                            RLog.d(TAG, "Deleting v1 record");
                            Jump.getSecureStorageInterface().removeValueForKey(HSDP_RECORD_FILE);
                        }

                        @Override
                        public void onFileWriteFailure() {
                            RLog.e(TAG, "getHsdpUserRecord: Error while saving hsdp record to SS");
                        }
                    });
                }
            } else {
                RLog.d(TAG, "getHsdpUserRecord: Hsdp record not available");
            }
        }


        return HsdpUserInstance.getInstance().getHsdpUserRecordV2();
    }

    /**
     * Delete From disk
     */
    public void deleteFromDisk() {
        mContext.deleteFile(HSDP_RECORD_FILE);
        Jump.getSecureStorageInterface().removeValueForKey(HSDP_RECORD_FILE);
        Jump.getSecureStorageInterface().removeValueForKey(HsdpUserRecordV2.SS_KEY_FOR_SAVING_RECORD);
        HsdpUserInstance.getInstance().setHsdpUserRecordV2(null);
    }


    public void login(final String email, final String accessToken,
                      final LoginHandler loginHandler) {

        RLog.d(TAG,
                "HSDP login");
        if (networkUtility.isNetworkAvailable()) {
            final Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() -> {
                try {
                    DhpAuthenticationManagementClient authenticationManagementClient =
                            new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse1 =
                            authenticationManagementClient.loginSocialProviders(email,
                                    accessToken, Jump.getRefreshSecret());

                    if (dhpAuthenticationResponse1 == null) {
                        handler.post(() -> handleSocialNetworkFailure(loginHandler));
                        return;
                    }

                    if (dhpAuthenticationResponse1.responseCode.equals(SUCCESS_CODE)) {
                        final Map<String, Object> rawResponse = dhpAuthenticationResponse1.
                                rawResponse;

                        final HsdpUserRecordV2 hsdpUserRecordV2 = new HsdpUserRecordV2();
                        hsdpUserRecordV2.parseHsdpUserInfo(rawResponse);
                        hsdpUserRecordV2.setRefreshSecret(Jump.getRefreshSecret());
                        HsdpUserInstance.getInstance().setHsdpUserRecordV2(hsdpUserRecordV2);
                        saveToDisk(new UserFileWriteListener() {
                            @Override
                            public void onFileWriteSuccess() {
                                if (loggingInterface != null) {
                                    loggingInterface.setHSDPUserUUID(hsdpUserRecordV2.getUserUUID());
                                }
                                handler.post(() -> {
                                    RLog.d(TAG, "Social onHsdpLoginSuccess : response :"
                                            + rawResponse.toString());
                                    HsdpUser hsdpUser = new HsdpUser(mContext);
                                    if (hsdpUser.getHsdpUserRecord() != null) {
                                        sendEncryptedUUIDToAnalytics(hsdpUserRecordV2);
                                    }
                                    ThreadUtils.postInMainThread(mContext, loginHandler::onLoginSuccess);
                                    UserRegistrationHelper.getInstance().notifyOnHSDPLoginSuccess();
                                });
                            }

                            @Override
                            public void onFileWriteFailure() {
                                handleSocialNetworkFailure(loginHandler);

                            }
                        });

                    } else {
                        handler.post(() -> {
                            RLog.d(TAG, "Social onHsdpLoginFailure :  responseCode : "
                                    + dhpAuthenticationResponse1.responseCode +
                                    " message : " + dhpAuthenticationResponse1.message);
                            handleSocialConnectionFailed(loginHandler, Integer.parseInt(
                                    dhpAuthenticationResponse1.responseCode), new URError(mContext).getLocalizedError(ErrorType.HSDP, Integer.parseInt(dhpAuthenticationResponse1.responseCode)), dhpAuthenticationResponse1.message);
                        });
                    }
                } catch (Exception e) {
                    RLog.e(TAG, "login : exception " + e.getMessage());
                    handleSocialNetworkFailure(loginHandler);
                }
            }).start();
        } else {
            handleSocialNetworkFailure(loginHandler);
        }

    }

    private void sendEncryptedUUIDToAnalytics(HsdpUserRecordV2 hsdpUserRecordV2) {
        if (RegistrationConfiguration.getInstance().isHsdpUuidShouldUpload()) {
            Encryption encryption = new Encryption();
            final String userUID = encryption.encrypt(hsdpUserRecordV2.getUserUUID());
            if (null != userUID) {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                        "evar2", userUID);
                RLog.d(TAG, "sendEncryptedUUIDToAnalytics : HSDP evar2 userUID" + userUID);
            }
        }
    }

    /**
     * handle social connection failed
     *
     * @param loginHandler login handler
     * @param errorCode                  error code
     * @param description                string
     */
    private void handleSocialConnectionFailed(LoginHandler loginHandler,
                                              int errorCode, String description, String errorTagging) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(description);
        userRegistrationFailureInfo.setErrorTagging(errorTagging);
        AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
        UserRegistrationHelper.getInstance().notifyOnHSDPLoginFailure(userRegistrationFailureInfo.getErrorCode(), userRegistrationFailureInfo.getErrorDescription());
        ThreadUtils.postInMainThread(mContext, () ->
                loginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
    }

    /**
     * handle social hsdp failure
     *
     * @param loginHandler login handler
     */
    private void handleSocialNetworkFailure(LoginHandler loginHandler) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
        userRegistrationFailureInfo.setErrorCode(ErrorCodes.HSDP_SYSTEM_ERROR_403);
        userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.HSDP, ErrorCodes.HSDP_SYSTEM_ERROR_403));
        userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.NETWORK_ERROR);
        AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
        UserRegistrationHelper.getInstance().notifyOnHSDPLoginFailure(userRegistrationFailureInfo.getErrorCode(), userRegistrationFailureInfo.getErrorDescription());
        ThreadUtils.postInMainThread(mContext, () ->
                loginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
    }

    /**
     * Hspd user signed in
     *
     * @return true if hsdp user signed in else false
     */
    public boolean isHsdpUserSignedIn() {
        HsdpUserRecordV2 hsdpUserRecordV2 = getHsdpUserRecord();

        final boolean isSignedIn = hsdpUserRecordV2 != null && ((hsdpUserRecordV2.getAccessCredential() != null &&
                hsdpUserRecordV2.getAccessCredential().getRefreshToken() != null)
                || hsdpUserRecordV2.getRefreshSecret() != null) &&
                hsdpUserRecordV2.getUserUUID() != null
                && (getHsdpUserRecord().getAccessCredential() != null &&
                getHsdpUserRecord().getAccessCredential().getAccessToken() != null);
        RLog.d(TAG, "isHsdpUserSignedIn : isSignedIn" + isSignedIn);
        RLog.d(TAG, "HsdpUserRecordV2 : hsdpUserRecord is available" + (hsdpUserRecordV2 != null ? hsdpUserRecordV2.toString() : null));
        return isSignedIn;
    }
}
