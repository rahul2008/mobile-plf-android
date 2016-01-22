package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.HSDPClientInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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

    private static byte[] secretKey;
    static{
        generateSecretKey();
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
                                handleServerConnectionFailed(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.JanRain_Server_Connection_Failed));
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
                                        RLog.i(RLog.HSDP, "onHsdpLoginSuccess : response :" + rawResponse.toString());
                                        loginHandler.onLoginSuccess();
                                    }
                                });
                            }

                            @Override
                            public void onFileWriteFailure() {
                                handleHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.NoNetworkConnection));
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
            handleHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.NoNetworkConnection));
        }
    }

    private void handleServerConnectionFailed(TraditionalLoginHandler loginHandler, int errorCode, String message) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(message);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private void handleHsdpFailure(TraditionalLoginHandler loginHandler, int errorCode, String message) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(message);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
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
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                logoutHandler.onLogoutFailure(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.JanRain_Server_Connection_Failed));
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

                    if (mHsdpUserRecord == null || mHsdpUserRecord.getUserUUID() == null || mHsdpUserRecord.getAccessCredential().getRefreshToken() == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshHandler.onRefreshLoginSessionFailedWithError(NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND);
                            }
                        });
                        return;
                    }

                    final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.refresh(mHsdpUserRecord.getUserUUID(), mHsdpUserRecord.getAccessCredential().getRefreshToken());
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

    private DhpApiClientConfiguration getDhpApiClientConfiguration() {
        DhpApiClientConfiguration dhpApiClientConfiguration = null;
        String environment = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
       // HSDPClientInfo hsdpClientInfo = RegistrationConfiguration.getInstance().getHsdpConfiguration().getHSDPClientInfo(environment);
        HSDPClientInfo hsdpClientInfo = RegistrationConfiguration.getInstance().getCurrentHSDPConfiguration().getHSDPClientInfo(environment);
        if (null != hsdpClientInfo && null != hsdpClientInfo.getBaseURL() && null != hsdpClientInfo.getSecret() && null != hsdpClientInfo.getShared()
                && null != hsdpClientInfo.getHSDPApplicationName()) {
           /* dhpApiClientConfiguration = new DhpApiClientConfiguration(
                    hsdpClientInfo.getBaseUrl(),
                    hsdpClientInfo.getApplicationName(),
                    hsdpClientInfo.getSharedId(),
                    hsdpClientInfo.getSecretId());*/
            RLog.i(RLog.HSDP, "Social base URL " + hsdpClientInfo.getBaseURL());
            dhpApiClientConfiguration = new DhpApiClientConfiguration(hsdpClientInfo.getBaseURL(),hsdpClientInfo.getHSDPApplicationName(),hsdpClientInfo.getShared(),hsdpClientInfo.getSecret());
        }
        return dhpApiClientConfiguration;
    }

    private void saveToDisk(UserFileWriteListener userFileWriteListener) {
        try {
            FileOutputStream fos = mContext.openFileOutput(HSDP_RECORD_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String objectPlainString = objectToString(mHsdpUserRecord);
            byte[] ectext = encrypt(objectPlainString);
            oos.writeObject(ectext);
            oos.close();
            fos.close();
            userFileWriteListener.onFileWriteSuccess();
        } catch (Exception e) {
            userFileWriteListener.onFileWriteFailure();
        }
    }

    public HsdpUserRecord getHsdpUserRecord() {
        try {
            FileInputStream fis = mContext.openFileInput(HSDP_RECORD_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] enctText = (byte[]) ois.readObject();
            byte[] decrtext = decrypt(enctText);
            mHsdpUserRecord = (HsdpUserRecord) stringToObject(new String(decrtext));
        } catch (Exception e) {
        }
        return mHsdpUserRecord;
    }

    public void deleteFromDisk() {
        mContext.deleteFile(HSDP_RECORD_FILE);
        mHsdpUserRecord = null;
    }

    private String objectToString(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
        }
        return null;
    }

    private Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] encrypt(String text) {
        try {
            Key key = (Key) new SecretKeySpec(secretKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encText = cipher.doFinal(text.getBytes());
            return encText;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private byte[] decrypt(byte[] encByte) {
        try {
            Key key = (Key) new SecretKeySpec(secretKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decText = cipher.doFinal(encByte);
            return decText;

        } catch (Exception ex) {
        }
        return null;
    }

    //private final String secretKey = "ASecureSecretKey";


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
                                handleSocialConnectionFailed(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.JanRain_Server_Connection_Failed));
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
                                handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.NoNetworkConnection));
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
            handleSocialHsdpFailure(loginHandler, NETWORK_ERROR_CODE + RegConstants.HSDP_LOWER_ERROR_BOUND, mContext.getString(R.string.NoNetworkConnection));
        }
    }

    private void handleSocialConnectionFailed(SocialLoginHandler loginHandler, int errorCode, String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private void handleSocialHsdpFailure(SocialLoginHandler loginHandler, int errorCode, String string) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(errorCode);
        userRegistrationFailureInfo.setErrorDescription(string);
        loginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private interface UserFileWriteListener {
        void onFileWriteSuccess();

        void onFileWriteFailure();
    }

    private static void generateSecretKey(){
        final byte[] salt = Settings.Secure.ANDROID_ID.getBytes();
        final char[] key = "jlapp7jokj4ngiafcrbna8nutu".toCharArray();
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(key,salt,1024,128);
            SecretKey s = f.generateSecret(ks);
            secretKey = s.getEncoded();
        }catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public boolean isHsdpUserSignedIn(){

        if(getHsdpUserRecord() != null && getHsdpUserRecord().getAccessCredential().getRefreshToken()!=null && getHsdpUserRecord().getUserUUID()!=null
                && getHsdpUserRecord().getAccessCredential().getAccessToken()!=null){
            return true;
        }
        return false;
    }
}
