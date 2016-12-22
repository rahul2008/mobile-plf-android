package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

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
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.security.SecureStorage;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class HsdpUser {

    private Context mContext;

    private HsdpUserRecord mHsdpUserRecord;

    private final String SUCCESS_CODE = "200";

    private final int NETWORK_ERROR_CODE = 111;

    private final String HSDP_RECORD_FILE = "hsdpRecord";

    public HsdpUser(Context context) {
        this.mContext = context;
    }

    private DhpResponse dhpResponse = null;

    /**
     * Logout
     *
     * @param logoutHandler logout handler
     */
    public void logOut(final LogoutHandler logoutHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler(Looper.getMainLooper());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient
                            = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    if (mHsdpUserRecord == null) {
                        mHsdpUserRecord = getHsdpUserRecord();
                    }
                    dhpResponse = null;
                    if (null != mHsdpUserRecord && null != mHsdpUserRecord.getAccessCredential()) {
                        dhpResponse = authenticationManagementClient.
                                logout(mHsdpUserRecord.getUserUUID(),
                                        mHsdpUserRecord.getAccessCredential().getAccessToken());
                    }
                    if (dhpResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                logoutHandler.
                                        onLogoutFailure(NETWORK_ERROR_CODE +
                                                RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.
                                                getString(R.string.
                                                        JanRain_Server_Connection_Failed));
                            }
                        });
                    } else {
                        if ( dhpResponse.responseCode != null &&
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
                    mContext.getString(R.string.NoNetworkConnection));
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
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mHsdpUserRecord == null) {
                        mHsdpUserRecord = getHsdpUserRecord();
                    }
                    DhpAuthenticationManagementClient authenticationManagementClient =
                            new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    dhpAuthenticationResponse = null;
                    if (mHsdpUserRecord != null &&
                            mHsdpUserRecord.getAccessCredential() != null &&
                            mHsdpUserRecord.getAccessCredential().getRefreshToken() != null
                            ) {
                        RLog.i(RLog.HSDP,"issuing refresh "+ SystemClock.elapsedRealtime());
                        dhpAuthenticationResponse = authenticationManagementClient.
                                refresh(mHsdpUserRecord.getUserUUID(),
                                        mHsdpUserRecord.getAccessCredential().getRefreshToken());

                    } else if (mHsdpUserRecord != null &&
                            null != mHsdpUserRecord.getUserUUID() &&
                            null != mHsdpUserRecord.getAccessCredential()) {
                        RLog.i(RLog.HSDP,"issuing refreshSecret "+ SystemClock.elapsedRealtime());
                        dhpAuthenticationResponse = authenticationManagementClient.
                                refreshSecret(mHsdpUserRecord.getUserUUID(),
                                        mHsdpUserRecord.getAccessCredential().
                                                getAccessToken(), mHsdpUserRecord.
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
                        RLog.i(RLog.HSDP,"Response recvd"+ SystemClock.elapsedRealtime()+" response "+dhpAuthenticationResponse);
                        mHsdpUserRecord.getAccessCredential().setExpiresIn(
                                dhpAuthenticationResponse.expiresIn);
                        mHsdpUserRecord.getAccessCredential().setRefreshToken
                                (dhpAuthenticationResponse.refreshToken);
                        mHsdpUserRecord.getAccessCredential().setAccessToken
                                (dhpAuthenticationResponse.accessToken);
                        saveToDisk(new UserFileWriteListener() {
                            @Override
                            public void onFileWriteSuccess() {
                                RLog.i(RLog.HSDP,"Writing to file successfull"+ SystemClock.elapsedRealtime());
                            }

                            @Override
                            public void onFileWriteFailure() {
                                RLog.i(RLog.HSDP,"Writing to file failure"+ SystemClock.elapsedRealtime());
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
            RLog.i(RLog.HSDP," onRefreshLoginSessionFailedWithError"+ SystemClock.elapsedRealtime());
            refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE +
                    RegConstants.HSDP_LOWER_ERROR_BOUND);
        }
    }


    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        String environment = RegistrationConfiguration.getInstance().
                getPilConfiguration().getRegistrationEnvironment();
        HSDPInfo hsdpInfo = RegistrationConfiguration.getInstance().getHsdpConfiguration().
                getHSDPInfo(RegUtility.getConfiguration(environment));

        if (null != hsdpInfo && null != hsdpInfo.getBaseURL() && null !=
                hsdpInfo.getSecreteId() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getApplicationName()) {
            RLog.i(RLog.HSDP, "Base URL " + hsdpInfo.getBaseURL());
            dhpApiClientConfiguration = new DhpApiClientConfiguration(hsdpInfo.getBaseURL(),
                    hsdpInfo.getApplicationName(), hsdpInfo.getSharedId(), hsdpInfo.getSecreteId());
        }
        return dhpApiClientConfiguration;
    }

    private void saveToDisk(UserFileWriteListener userFileWriteListener) {
        try {

            FileOutputStream fos = mContext.openFileOutput(HSDP_RECORD_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            RLog.i(RLog.HSDP,"inside SavetoDIsk converting mHsdpUserRecord to string --start "+ SystemClock.elapsedRealtime());
            String objectPlainString = SecureStorage.objectToString(mHsdpUserRecord);
            RLog.i(RLog.HSDP,"inside SavetoDIsk converting mHsdpUserRecord to string --End "+ SystemClock.elapsedRealtime());
            RLog.i(RLog.HSDP,"inside SavetoDIsk encrypt --start "+ SystemClock.elapsedRealtime());
            byte[] ectext = SecureStorage.encrypt(objectPlainString);
            RLog.i(RLog.HSDP,"inside SavetoDIsk encrypt --End "+ SystemClock.elapsedRealtime());
            RLog.i(RLog.HSDP,"inside SavetoDIsk write object of encrypted text --start "+ SystemClock.elapsedRealtime());
            oos.writeObject(ectext);
            RLog.i(RLog.HSDP,"inside SavetoDIsk write object of encrypted text --end "+ SystemClock.elapsedRealtime());
            oos.close();
            fos.close();
            userFileWriteListener.onFileWriteSuccess();
        } catch (Exception e) {
            RLog.i(RLog.HSDP,"inside SavetoDIsk Exception occured "+e+" "+ SystemClock.elapsedRealtime());
            userFileWriteListener.onFileWriteFailure();
        }
    }

    public HsdpUserRecord getHsdpUserRecord() {
        if (mHsdpUserRecord != null) {
            return mHsdpUserRecord;
        }
        try {
            RLog.i(RLog.HSDP,"inside getHsdpUserRecord read object --start "+ SystemClock.elapsedRealtime());
            FileInputStream fis = mContext.openFileInput(HSDP_RECORD_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] enctText = (byte[]) ois.readObject();
            RLog.i(RLog.HSDP,"inside getHsdpUserRecord read object --end "+ SystemClock.elapsedRealtime());

            RLog.i(RLog.HSDP,"inside getHsdpUserRecord decrypt  --start "+ SystemClock.elapsedRealtime());
            byte[] decrtext = SecureStorage.decrypt(enctText);
            RLog.i(RLog.HSDP,"inside getHsdpUserRecord decrypt  --end "+ SystemClock.elapsedRealtime());

            RLog.i(RLog.HSDP,"inside getHsdpUserRecord convert string to object  --start "+ SystemClock.elapsedRealtime());
            mHsdpUserRecord = (HsdpUserRecord) SecureStorage.stringToObject(new String(decrtext));
            RLog.i(RLog.HSDP,"inside getHsdpUserRecord convert string to object  --end "+ SystemClock.elapsedRealtime());
        } catch (Exception e) {
            RLog.d("HSDP file operation", e.getMessage());
        }
        return mHsdpUserRecord;
    }

    public void deleteFromDisk() {
        RLog.i(RLog.HSDP,"inside deleteFromDisk deleting record start"+ SystemClock.elapsedRealtime());
        mContext.deleteFile(HSDP_RECORD_FILE);
        mHsdpUserRecord = null;
    }

    public void socialLogin(final String email, final String accessToken,
                            final String refreshSecret,final SocialLoginHandler loginHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
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
                                                JanRain_Server_Connection_Failed));
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
                                        RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.
                                        getString(R.string.NoNetworkConnection));
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
                    RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.
                    getString(R.string.NoNetworkConnection));
        }
    }

    private void handleSocialConnectionFailed(SocialLoginHandler loginHandler,
                                              int errorCode, String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private void handleSocialHsdpFailure(SocialLoginHandler loginHandler, int errorCode,
                                         String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private interface UserFileWriteListener {
        void onFileWriteSuccess();

        void onFileWriteFailure();
    }
    /*
        private String generateRefreshSecret() {
            final int SECRET_LENGTH = 40;
            SecureRandom random = new SecureRandom();
            StringBuilder buffer = new StringBuilder();

            while (buffer.length() < SECRET_LENGTH) {
                buffer.append(Integer.toHexString(random.nextInt()));
            }
            String refreshSecret = buffer.toString().substring(0, SECRET_LENGTH);
            return refreshSecret;
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
