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

import com.janrain.android.Jump;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.security.SecureStorage;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import javax.inject.Inject;


/**
 * class hsdp user
 */
public class HsdpUser {

    @Inject
    NetworkUtility networkUtility;

    private Context mContext;

    private final String SUCCESS_CODE = "200";

    private final int NETWORK_ERROR_CODE = 111;

    private final String HSDP_RECORD_FILE = "hsdpRecord";

    /**
     * Class constructor
     * @param context
     */
    public HsdpUser(Context context) {
        this.mContext = context;
        URInterface.getComponent().inject(this);
    }

    private DhpResponse dhpResponse = null;

    /**
     * Logout
     *
     * @param logoutHandler logout handler
     */
    public void logOut(final LogoutHandler logoutHandler) {
        if (networkUtility.isNetworkAvailable()) {
            final Handler handler = new Handler(Looper.getMainLooper());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient
                            = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());

                    dhpResponse = null;
                    if (null != getHsdpUserRecord() && null != getHsdpUserRecord().getAccessCredential()) {
                        dhpResponse = authenticationManagementClient.
                                logout(getHsdpUserRecord().getUserUUID(),
                                        getHsdpUserRecord().getAccessCredential().getAccessToken());
                    }
                    if (dhpResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                logoutHandler.
                                        onLogoutFailure(NETWORK_ERROR_CODE +
                                                RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.
                                                getString(R.string.
                                                        reg_JanRain_Server_Connection_Failed));
                            }
                        });
                    } else {
                        if (dhpResponse.responseCode != null &&
                                dhpResponse.responseCode.equals(SUCCESS_CODE)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RLog.i(RLog.HSDP, "onHsdsLogoutSuccess : response :"
                                            + dhpResponse.rawResponse.toString());
                                    logoutHandler.onLogoutSuccess();
                                }
                            });
                        } else {
                            if (dhpResponse.responseCode != null &&
                                    (dhpResponse.responseCode.equals(RegConstants.
                                            INVALID_ACCESS_TOKEN_CODE) || dhpResponse.
                                            responseCode.equals(RegConstants.
                                            INVALID_REFRESH_TOKEN_CODE))) {
                                RLog.i(RLog.HSDP, "onHsdsLogoutFailure : responseCode : "
                                        + dhpResponse.responseCode + " message : "
                                        + dhpResponse.message);
                                logoutHandler.onLogoutFailure(Integer.
                                                parseInt(dhpResponse.responseCode),
                                        dhpResponse.message);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RLog.i(RLog.HSDP, "onHsdsLogoutFailure : responseCode : " +
                                                dhpResponse.responseCode +
                                                " message : " + dhpResponse.message);
                                        logoutHandler.onLogoutFailure(Integer.
                                                        parseInt(dhpResponse.responseCode) +
                                                        RegConstants.HSDP_LOWER_ERROR_BOUND,
                                                dhpResponse.message);
                                    }
                                });
                            }
                        }
                    }
                }
            }).start();
        } else {
            logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE +
                            RegConstants.HSDP_LOWER_ERROR_BOUND,
                    mContext.getString(R.string.reg_NoNetworkConnection));
        }
    }


    private DhpAuthenticationResponse dhpAuthenticationResponse = null;

    /**
     * Refresh token
     *
     * @param refreshHandler refresh handler
     */
    public void refreshToken(final RefreshLoginSessionHandler refreshHandler) {
        final Handler handler = new Handler(Looper.getMainLooper());
        if (networkUtility.isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

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
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshHandler.
                                        onRefreshLoginSessionFailedWithError
                                                (NETWORK_ERROR_CODE +
                                                        RegConstants.HSDP_LOWER_ERROR_BOUND);
                            }
                        });
                    } else if (null!= dhpAuthenticationResponse.responseCode &&
                            dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        getHsdpUserRecord().getAccessCredential().setExpiresIn(
                                dhpAuthenticationResponse.expiresIn);
                        getHsdpUserRecord().getAccessCredential().setRefreshToken
                                (dhpAuthenticationResponse.refreshToken);
                        getHsdpUserRecord().getAccessCredential().setAccessToken
                                (dhpAuthenticationResponse.accessToken);
                        saveToDisk(new UserFileWriteListener() {
                            @Override
                            public void onFileWriteSuccess() {
                            }

                            @Override
                            public void onFileWriteFailure() {
                            }
                        });
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpRefreshSuccess : response :" +
                                        dhpAuthenticationResponse.rawResponse.toString());
                                refreshHandler.onRefreshLoginSessionSuccess();
                            }
                        });
                    } else {
                        if (dhpAuthenticationResponse.responseCode != null &&
                                dhpAuthenticationResponse.responseCode
                                        .equals(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RLog.i(RLog.HSDP, "onHsdpRefreshFailure : responseCode : "
                                            + dhpAuthenticationResponse.responseCode +
                                            " message : " + dhpAuthenticationResponse.message);
                                    refreshHandler.onRefreshLoginSessionFailedWithError(Integer
                                            .parseInt(dhpAuthenticationResponse.responseCode));
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RLog.i(RLog.HSDP, "onHsdpRefreshFailure : responseCode : "
                                            + dhpAuthenticationResponse.responseCode +
                                            " message : " + dhpAuthenticationResponse.message);
                                    refreshHandler.onRefreshLoginSessionFailedWithError(Integer.
                                            parseInt(dhpAuthenticationResponse.responseCode) +
                                            RegConstants.HSDP_LOWER_ERROR_BOUND);
                                }
                            });
                        }
                    }
                }
            }).start();
        } else {
            refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE +
                    RegConstants.HSDP_LOWER_ERROR_BOUND);
        }
    }
    /**
     * get dhp api client configuration
     * @return DhpApiClientConfiguration object
     * {@link DhpApiClientConfiguration}
     */
    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        HSDPInfo hsdpInfo = RegistrationConfiguration.getInstance().getHSDPInfo();
        if (null != hsdpInfo && null != hsdpInfo.getBaseURL() && null !=
                hsdpInfo.getSecreteId() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getApplicationName()) {

            RLog.i(RLog.HSDP, "Base URL " + hsdpInfo.getBaseURL());
            dhpApiClientConfiguration = new DhpApiClientConfiguration(hsdpInfo.getBaseURL(),
                    hsdpInfo.getApplicationName(), hsdpInfo.getSharedId(), hsdpInfo.getSecreteId());
        }
        return dhpApiClientConfiguration;
    }

    /**
     * Save to disk
     * @param userFileWriteListener user file write listener
     */
    private void saveToDisk(UserFileWriteListener userFileWriteListener) {
        String objectPlainString = SecureStorage.objectToString(getHsdpUserRecord());
        try {
            mContext.deleteFile(HSDP_RECORD_FILE);
            Jump.getSecureStorageInterface().storeValueForKey(HSDP_RECORD_FILE,
                    new String(objectPlainString) ,new SecureStorageInterface.SecureStorageError());
            userFileWriteListener.onFileWriteSuccess();

        } catch (Exception e) {
            userFileWriteListener.onFileWriteFailure();
        }
    }

    /**
     * Get hsdp user record
     *
     * @return HsdpUserRecord object
     * {@link HsdpUserRecord}
     */
    public HsdpUserRecord getHsdpUserRecord() {
        if (null != HsdpUserInstance.getInstance().getHsdpUserRecord()) {
            return HsdpUserInstance.getInstance().getHsdpUserRecord();
        }

        try {
            File file = mContext.getFileStreamPath(HSDP_RECORD_FILE);
            if (file != null && file.exists()) {
                final FileInputStream fis = mContext.openFileInput(HSDP_RECORD_FILE);
                final ObjectInputStream ois = new ObjectInputStream(fis);
                byte[] enctText = (byte[]) ois.readObject();
                byte[] decrtext = SecureStorage.decrypt(enctText);
                mContext.deleteFile(HSDP_RECORD_FILE);
                Jump.getSecureStorageInterface().storeValueForKey(HSDP_RECORD_FILE,
                        new String(decrtext), new SecureStorageInterface.SecureStorageError());
                ois.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String hsdpRecord = Jump.getSecureStorageInterface().fetchValueForKey(HSDP_RECORD_FILE,
                new SecureStorageInterface.SecureStorageError());

        if (hsdpRecord != null) {
            Object obj = SecureStorage.stringToObject(hsdpRecord);
            if (obj instanceof HsdpUserRecord) {
                HsdpUserInstance.getInstance().setHsdpUserRecord((HsdpUserRecord) obj);
            }
        }
        return HsdpUserInstance.getInstance().getHsdpUserRecord();
    }

    /**
     * Delete From disk
     *
     */
    public void deleteFromDisk() {
        mContext.deleteFile(HSDP_RECORD_FILE);
        Jump.getSecureStorageInterface().removeValueForKey(HSDP_RECORD_FILE);
        HsdpUserInstance.getInstance().setHsdpUserRecord(null);
    }


    public void socialLogin(final String email, final String accessToken,
                            final String refreshSecret,final SocialLoginHandler loginHandler) {

        if (networkUtility.isNetworkAvailable()) {
            final Handler handler = new Handler(Looper.getMainLooper());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient =
                            new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse =
                            authenticationManagementClient.loginSocialProviders(email,
                                    accessToken ,refreshSecret);

                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handleSocialConnectionFailed(loginHandler, NETWORK_ERROR_CODE +
                                                RegConstants.HSDP_LOWER_ERROR_BOUND,
                                        mContext.getString(R.string.
                                                reg_JanRain_Server_Connection_Failed));

                            }
                        });
                        return;
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        final Map<String, Object> rawResponse = dhpAuthenticationResponse.
                                rawResponse;

                        HsdpUserRecord hsdpUserRecord = new HsdpUserRecord();
                        hsdpUserRecord = hsdpUserRecord.parseHsdpUserInfo(rawResponse);
                        hsdpUserRecord.setRefreshSecret(refreshSecret);
                        HsdpUserInstance.getInstance().setHsdpUserRecord(hsdpUserRecord);
                        saveToDisk(new UserFileWriteListener() {

                            @Override
                            public void onFileWriteSuccess() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RLog.i(RLog.HSDP, "Social onHsdpLoginSuccess : response :"
                                                + rawResponse.toString());
                                        HsdpUser hsdpUser = new HsdpUser(mContext);
                                        if (hsdpUser.getHsdpUserRecord() != null)
                                            loginHandler.onLoginSuccess();
                                    }
                                });
                            }

                            @Override
                            public void onFileWriteFailure() {
                                handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE +
                                                RegConstants.HSDP_LOWER_ERROR_BOUND,
                                        mContext.getString(R.string.reg_NoNetworkConnection));

                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "Social onHsdpLoginFailure :  responseCode : "
                                        + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                handleSocialConnectionFailed(loginHandler, Integer.parseInt(
                                        dhpAuthenticationResponse.responseCode) +
                                                RegConstants.HSDP_LOWER_ERROR_BOUND,
                                        dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE +
                            RegConstants.HSDP_LOWER_ERROR_BOUND,
                    mContext.getString(R.string.reg_NoNetworkConnection));
        }
    }

    /**
     * handle social connection failed
     * @param loginHandler login handler
     * @param errorCode error code
     * @param string string
     */
    private void handleSocialConnectionFailed(SocialLoginHandler loginHandler,
                                              int errorCode, String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    /**
     * handle social hsdp failure
     * @param loginHandler login handler
     * @param errorCode error code
     * @param string string
     */
    private void handleSocialHsdpFailure(SocialLoginHandler loginHandler, int errorCode,
                                         String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    /**
     * User file write listener interface
     */
    private interface UserFileWriteListener {
        void onFileWriteSuccess();

        void onFileWriteFailure();
    }

    /**
     * Hspd user signed in
     *
     * @return true if hsdp user signed in else false
     */
    public boolean isHsdpUserSignedIn() {
        HsdpUserRecord hsdpUserRecord = getHsdpUserRecord();
        return hsdpUserRecord != null && ((hsdpUserRecord.getAccessCredential() != null &&
                hsdpUserRecord.getAccessCredential().getRefreshToken() != null)
                || hsdpUserRecord.getRefreshSecret() != null) &&
                hsdpUserRecord.getUserUUID() != null
                && (getHsdpUserRecord().getAccessCredential() != null &&
                getHsdpUserRecord().getAccessCredential().getAccessToken() != null);
    }
}
