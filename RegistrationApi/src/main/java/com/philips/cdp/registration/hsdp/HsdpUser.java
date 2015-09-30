package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.HSDPClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.hsdp.handler.LoginHandler;
import com.philips.cdp.registration.hsdp.handler.LogoutHandler;
import com.philips.cdp.registration.hsdp.handler.RefreshHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
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

    public void hsdpLogin(final String email, final String password, final LoginHandler loginHandler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DhpAuthenticationManagementClient authenticationManagementClient = new DhpAuthenticationManagementClient(getDhpApiClientConfiguration());
                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.authenticate(email, password);
                    if(dhpAuthenticationResponse == null){
                        loginHandler.onHsdpLoginFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.JanRain_Server_Connection_Failed));
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
                                loginHandler.onHsdpLoginSuccess();
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLoginFailure :  responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                loginHandler.onHsdpLoginFailure(Integer.parseInt(dhpAuthenticationResponse.responseCode), dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            loginHandler.onHsdpLoginFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.NoNetworkConnection));
        }
    }

    public void hsdpLogOut(final LogoutHandler logoutHandler) {
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
                    if(dhpAuthenticationResponse==null){
                        logoutHandler.onHsdpLogoutFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    }

                    if (dhpAuthenticationResponse.responseCode.equals(SUCCESS_CODE)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLogoutSuccess : response :" + dhpAuthenticationResponse.rawResponse.toString());
                                logoutHandler.onHsdpLogoutSuccess();
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpLogoutFailure : responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                logoutHandler.onHsdpLogoutFailure(Integer.parseInt(dhpAuthenticationResponse.responseCode), dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            logoutHandler.onHsdpLogoutFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.NoNetworkConnection));
        }
    }

    public void hsdpRefreshToken(final RefreshHandler refreshHandler) {
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
                    if(dhpAuthenticationResponse == null){
                        refreshHandler.onHsdpRefreshFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.JanRain_Server_Connection_Failed));
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
                                refreshHandler.onHsdpRefreshSuccess();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                RLog.i(RLog.HSDP, "onHsdpRefreshFailure : responseCode : " + dhpAuthenticationResponse.responseCode +
                                        " message : " + dhpAuthenticationResponse.message);
                                refreshHandler.onHsdpRefreshFailure(Integer.parseInt(dhpAuthenticationResponse.responseCode), dhpAuthenticationResponse.message);
                            }
                        });
                    }
                }
            }).start();
        } else {
            refreshHandler.onHsdpRefreshFailure(NETWORK_ERROR_CODE, mContext.getString(R.string.NoNetworkConnection));
        }
    }

    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        HSDPClientId hsdpClientId = RegistrationConfiguration.getInstance().getHsdpConfiguration().getHSDPClientId(RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        if (null != hsdpClientId && null != hsdpClientId.getBaseUrl() && null != hsdpClientId.getSecretId() && null != hsdpClientId.getSharedId()
                && null != RegistrationConfiguration.getInstance().getHsdpConfiguration().getApplicationName()) {
           dhpApiClientConfiguration = new DhpApiClientConfiguration(
                    hsdpClientId.getBaseUrl(),
                    RegistrationConfiguration.getInstance().getHsdpConfiguration().getApplicationName(),
                   hsdpClientId.getSharedId(),
                   hsdpClientId.getSecretId());
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
