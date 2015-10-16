package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.HSDPClientInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by 310190722 on 9/15/2015.
 */
public class HsdpUser {

    private Context mContext;

    private HsdpUserRecord mHsdpUserRecord;

    private final String SUCCESS_CODE = "200";

    private final int NETWORK_ERROR_CODE = 111;

    private final String HSDP_RECORD_FILE = "hsdpRecord";

    public HsdpUser(Context context) {
        this.mContext = context;
    }

    public void login(final String email, final String password, final TraditionalLoginHandler loginHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.authenticate(email, password);
                    if (dhpAuthenticationResponse == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                 UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                                userRegistrationFailureInfo.setErrorCode(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
                                userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                                loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                            }
                        });
                        return;
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        final Map<String, Object> rawResponse = dhpAuthenticationResponse.rawResponse;
                        mHsdpUserRecord = new HsdpUserRecord(mContext);
                        mHsdpUserRecord = mHsdpUserRecord.parseHsdpUserInfo(rawResponse);
                        saveToDisk();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLoginSuccess : response :" + rawResponse.toString());
                                loginHandler.onLoginSuccess();
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLoginFailure :  responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                                userRegistrationFailureInfo.setErrorCode(Integer.parseInt(dhpAuthenticationResponse.responseCode) + RegConstants.HSDP_LOWER_ERROR_BOUND);
                                userRegistrationFailureInfo.setErrorDescription(dhpAuthenticationResponse.message);
                                loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                            }
                        });
                    }
                }
            }).start();
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.NoNetworkConnection));
            loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
        }
    }

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
                        logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdsLogoutSuccess : response :" + dhpAuthenticationResponse.rawResponse.toString());
                                logoutHandler.onLogoutSuccess();
                            }
                        });

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
            logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.NoNetworkConnection));
        }
    }

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
                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.refresh(mHsdpUserRecord.getUserUUID(), mHsdpUserRecord.getAccessCredential().getRefreshToken());
                    if (dhpAuthenticationResponse == null) {
                        refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
                    }
                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        mHsdpUserRecord.getAccessCredential().setExpiresIn(dhpAuthenticationResponse.expiresIn.intValue());
                        mHsdpUserRecord.getAccessCredential().setRefreshToken(dhpAuthenticationResponse.refreshToken);
                        mHsdpUserRecord.getAccessCredential().setAccessToken(dhpAuthenticationResponse.accessToken);
                        saveToDisk();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpRefreshSuccess : response :" + dhpAuthenticationResponse.rawResponse.toString());
                                refreshHandler.onRefreshLoginSessionSuccess();
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

    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        String environment = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        HSDPClientInfo hsdpClientInfo = RegistrationConfiguration.getInstance().getHsdpConfiguration().getHSDPClientInfo(environment);
        if (null != hsdpClientInfo && null != hsdpClientInfo.getBaseUrl() && null != hsdpClientInfo.getSecretId() && null != hsdpClientInfo.getSharedId()
                && null != hsdpClientInfo.getApplicationName()) {
            dhpApiClientConfiguration = new DhpApiClientConfiguration(
                    hsdpClientInfo.getBaseUrl(),
                    hsdpClientInfo.getApplicationName(),
                    hsdpClientInfo.getSharedId(),
                    hsdpClientInfo.getSecretId());
        }
        return dhpApiClientConfiguration;
    }

    private void saveToDisk() {
        try {
            FileOutputStream fos = mContext.openFileOutput(HSDP_RECORD_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mHsdpUserRecord);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HsdpUserRecord getHsdpUserRecord() {
        try {
            FileInputStream fis = mContext.openFileInput(HSDP_RECORD_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mHsdpUserRecord = (HsdpUserRecord) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mHsdpUserRecord;
    }

    public void deleteFromDisk() {
        mContext.deleteFile(HSDP_RECORD_FILE);
        mHsdpUserRecord = null;
    }
}
