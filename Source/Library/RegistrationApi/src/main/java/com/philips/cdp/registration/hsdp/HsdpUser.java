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

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.security.SecurityHelper;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.util.Map;

/**
 * Created by 310190722 on 9/15/2015.
 * class hsdp user
 */
public class HsdpUser {

    private Context mContext;

    private static HsdpUserRecord mHsdpUserRecord;

    private final String SUCCESS_CODE = "200";

    private final int NETWORK_ERROR_CODE = 111;

    private final String HSDP_RECORD_FILE = "hsdpRecord";

    /**
     * Class constructor
     * @param context
     */
    public HsdpUser(Context context) {
        this.mContext = context;
    }

    /**
     * Login
     * @param email Email
     * @param password password
     * @param refreshSecret refresh secret
     * @param loginHandler loging handler
     */
    public void login(final String email, final String password,final String refreshSecret, final TraditionalLoginHandler loginHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.authenticate(email, password,refreshSecret );
                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handleServerConnectionFailed(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
                            }
                        });
                        return;
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        final Map<String, Object> rawResponse = dhpAuthenticationResponse.rawResponse;
                        mHsdpUserRecord = new HsdpUserRecord(mContext);
                        mHsdpUserRecord = mHsdpUserRecord.parseHsdpUserInfo(rawResponse);
                        mHsdpUserRecord.setRefreshSecret(refreshSecret);
                        saveToDisk(new UserFileWriteListener() {
                            @Override
                            public void onFileWriteSuccess() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RLog.i(RLog.HSDP, "onHsdpLoginSuccess : response :" + rawResponse.toString());
                                        loginHandler.onLoginSuccess();
                                    }
                                });
                            }

                            @Override
                            public void onFileWriteFailure() {
                                handleHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_NoNetworkConnection));
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLoginFailure :  responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                handleServerConnectionFailed(loginHandler, Integer.parseInt(dhpAuthenticationResponse.responseCode) + RegConstants.HSDP_LOWER_ERROR_BOUND, dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            handleHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_NoNetworkConnection));
        }
    }

    /**
     * handle server connection failed
     * @param loginHandler login handler
     * @param errorCode error code
     * @param message message
     */
    private void handleServerConnectionFailed(TraditionalLoginHandler loginHandler, int errorCode, String message) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(message);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    /**
     * Handle hsdp failure
     * @param loginHandler login handler
     * @param errorCode error code
     * @param message message
     */
    private void handleHsdpFailure(TraditionalLoginHandler loginHandler, int errorCode, String message) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(message);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    /**
     * Logout
     * @param logoutHandler logout handler
     */
    public void logOut(final LogoutHandler logoutHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    if (mHsdpUserRecord == null) {
                        mHsdpUserRecord = getHsdpUserRecord();
                    }
                    final DhpResponse dhpAuthenticationResponse = authenticationManagementClient.logout(mHsdpUserRecord.getUserUUID(), mHsdpUserRecord.getAccessCredential().getAccessToken());
                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
                            }
                        });
                    } else if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdsLogoutSuccess : response :" + dhpAuthenticationResponse.rawResponse.toString());
                                logoutHandler.onLogoutSuccess();
                            }
                        });

                    } else if (dhpAuthenticationResponse.responseCode.equals(RegConstants.INVALID_ACCESS_TOKEN_CODE) || dhpAuthenticationResponse.responseCode.equals(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                        RLog.i(RLog.HSDP, "onHsdsLogoutFailure : responseCode : " + dhpAuthenticationResponse.responseCode + " message : " + dhpAuthenticationResponse.message);
                        logoutHandler.onLogoutFailure(Integer.parseInt(dhpAuthenticationResponse.responseCode), dhpAuthenticationResponse.message);

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdsLogoutFailure : responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                logoutHandler.onLogoutFailure(Integer.parseInt(dhpAuthenticationResponse.responseCode) + RegConstants.HSDP_LOWER_ERROR_BOUND, dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_NoNetworkConnection));
        }
    }

    /**
     * Refresh token
     * @param refreshHandler refresh handler
     */
    public void refreshToken(final RefreshLoginSessionHandler refreshHandler) {
        final Handler handler = new Handler();
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    if (mHsdpUserRecord == null) {
                        mHsdpUserRecord = getHsdpUserRecord();
                    }
                    final DhpAuthenticationResponse dhpAuthenticationResponse;
                    if(mHsdpUserRecord.getAccessCredential().getRefreshToken()==null && mHsdpUserRecord!=null ){
                        dhpAuthenticationResponse = authenticationManagementClient.refreshSecret(mHsdpUserRecord.getUserUUID(), mHsdpUserRecord.getAccessCredential().getAccessToken(),mHsdpUserRecord.getRefreshSecret());
                    }else{
                        dhpAuthenticationResponse = authenticationManagementClient.refresh(mHsdpUserRecord.getUserUUID(), mHsdpUserRecord.getAccessCredential().getRefreshToken());
                    }

                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
                            }
                        });
                        return;
                    } else if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        mHsdpUserRecord.getAccessCredential().setExpiresIn(dhpAuthenticationResponse.expiresIn.intValue());
                        mHsdpUserRecord.getAccessCredential().setRefreshToken(dhpAuthenticationResponse.refreshToken);
                        mHsdpUserRecord.getAccessCredential().setAccessToken(dhpAuthenticationResponse.accessToken);
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
                                RLog.i(RLog.HSDP, "onHsdpRefreshSuccess : response :" + dhpAuthenticationResponse.rawResponse.toString());
                                refreshHandler.onRefreshLoginSessionSuccess();
                            }
                        });
                    } else if (dhpAuthenticationResponse.responseCode.toString().equals(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpRefreshFailure : responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                refreshHandler.onRefreshLoginSessionFailedWithError(Integer.parseInt(dhpAuthenticationResponse.responseCode));
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpRefreshFailure : responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                refreshHandler.onRefreshLoginSessionFailedWithError(Integer.parseInt(dhpAuthenticationResponse.responseCode) + RegConstants.HSDP_LOWER_ERROR_BOUND);
                            }
                        });
                    }
                }
            }).start();
        } else {
            refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
        }
    }

    /**
     * get dhp api client configuration
     * @return DhpApiClientConfiguration object
     * {@link DhpApiClientConfiguration}
     */
    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        String environment = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        HSDPInfo hsdpInfo = RegistrationConfiguration.getInstance().getHsdpConfiguration().getHSDPInfo(RegUtility.getConfiguration(environment));
        if (null != hsdpInfo && null != hsdpInfo.getBaseURL() && null != hsdpInfo.getSecreteId() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getApplicationName()) {

            RLog.i(RLog.HSDP, "Base URL " + hsdpInfo.getBaseURL());
            dhpApiClientConfiguration = new DhpApiClientConfiguration(hsdpInfo.getBaseURL(), hsdpInfo.getApplicationName(), hsdpInfo.getSharedId(), hsdpInfo.getSecreteId());
        }
        return dhpApiClientConfiguration;
    }

    /**
     * Save to disk
     * @param userFileWriteListener user file write listener
     */
    private void saveToDisk(UserFileWriteListener userFileWriteListener) {
            SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
            boolean isWrittenToFile = secureStorageInterface.storeValueForKey(HSDP_RECORD_FILE, SecurityHelper.objectToString(mHsdpUserRecord));
            if (isWrittenToFile){
                userFileWriteListener.onFileWriteSuccess();
            }else{
                userFileWriteListener.onFileWriteFailure();
            }
    }

    /**
     * Get hsdp user record
     * @return HsdpUserRecord object
     * {@link HsdpUserRecord}
     */
    public HsdpUserRecord getHsdpUserRecord() {
        if(mHsdpUserRecord!=null){
            return mHsdpUserRecord;
        }
        SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
        mHsdpUserRecord = (HsdpUserRecord) SecurityHelper.stringToObject(secureStorageInterface.fetchValueForKey(HSDP_RECORD_FILE));
        return mHsdpUserRecord;
    }

    /**
     * Delete From disk
     *
     */
    public void deleteFromDisk() {
        SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
        secureStorageInterface.removeValueForKey(HSDP_RECORD_FILE);
        mHsdpUserRecord = null;
    }

    /**
     * Social login
     * @param email email
     * @param accessToken acess token
     * @param loginHandler login handler
     */
    public void socialLogin(final String email, final String accessToken, final SocialLoginHandler loginHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.loginSocialProviders(email, accessToken);
                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handleSocialConnectionFailed(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
                            }
                        });
                        return;
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        final Map<String, Object> rawResponse = dhpAuthenticationResponse.rawResponse;
                        mHsdpUserRecord = new HsdpUserRecord(mContext);
                        mHsdpUserRecord = mHsdpUserRecord.parseHsdpUserInfo(rawResponse);
                        saveToDisk(new UserFileWriteListener() {

                            @Override
                            public void onFileWriteSuccess() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RLog.i(RLog.HSDP, "Social onHsdpLoginSuccess : response :" + rawResponse.toString());
                                        HsdpUser hsdpUser = new HsdpUser(mContext);
                                        if (hsdpUser.getHsdpUserRecord() != null)
                                            loginHandler.onLoginSuccess();
                                    }
                                });
                            }

                            @Override
                            public void onFileWriteFailure() {
                                handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_NoNetworkConnection));
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "Social onHsdpLoginFailure :  responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                handleSocialConnectionFailed(loginHandler, Integer.parseInt(dhpAuthenticationResponse.responseCode) + RegConstants.HSDP_LOWER_ERROR_BOUND, dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.reg_NoNetworkConnection));
        }
    }

    /**
     * handle social connection failed
     * @param loginHandler login handler
     * @param errorCode error code
     * @param string string
     */
    private void handleSocialConnectionFailed(SocialLoginHandler loginHandler, int errorCode, String string) {
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
    private void handleSocialHsdpFailure(SocialLoginHandler loginHandler, int errorCode, String string) {
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
     * @return true if hsdp user signed in else false
     */
    public boolean isHsdpUserSignedIn(){
        HsdpUserRecord hsdpUserRecord = getHsdpUserRecord();
        if(hsdpUserRecord != null && (hsdpUserRecord.getAccessCredential().getRefreshToken()!=null || hsdpUserRecord.getRefreshSecret()!=null) && hsdpUserRecord.getUserUUID()!=null
                && getHsdpUserRecord().getAccessCredential().getAccessToken()!=null){
            return true;
        }
        return false;
    }
}
