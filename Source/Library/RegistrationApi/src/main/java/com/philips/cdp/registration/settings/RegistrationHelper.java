
package com.philips.cdp.registration.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import com.janrain.android.Jump;
import com.janrain.android.utils.SecureUtility;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.events.NetworkStateHelper;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.tagging.Tagging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Locale;

public class RegistrationHelper {

    private Context mContext;

    public boolean mJanrainIntialized = false;

    private static RegistrationHelper mRegistrationHelper = null;

    private EvalRegistrationSettings mEvalRegistrationSettings;

    private DevRegistrationSettings mDevRegistrationSettings;

    private ProdRegistrationSettings mProdRegistrationSettings;

    private TestingRegistrationSettings mTestingRegistrationSettings;

    private StaginglRegistrationSettings mStagingRegistrationSettings;

    private RegistrationSettings mRegistrationSettings;

    private String countryCode;

    private final int CALL_AFTER_DELAY = 500;

    private boolean isCoppaFlow = false;

    private boolean isHsdpFlow;

    private boolean isJsonRead;

    private boolean mIsInitializationInProgress;

    private JumpFlowDownloadStatusListener mJumpFlowDownloadStatusListener;

    public boolean isJumpInitializationInProgress() {
        return mIsInitializationInProgress;
    }

    public boolean isJanrainIntialized() {
        return mJanrainIntialized;
    }

    public void setJanrainIntialized(boolean janrainIntializationStatus) {
        mJanrainIntialized = janrainIntializationStatus;
    }

    private Handler mHandler;

    private RegistrationHelper() {
        mHandler = new Handler();
    }

    public static RegistrationHelper getInstance() {
        if (mRegistrationHelper == null) {
            mRegistrationHelper = new RegistrationHelper();

        }
        //  RLog.i(RLog.ACTIVITY_LIFECYCLE,"mRegistrationHelper " +mRegistrationHelper);
        return mRegistrationHelper;
    }

    public void registerJumpFlowDownloadListener(JumpFlowDownloadStatusListener pJumpFlowDownloadStatusListener) {
        this.mJumpFlowDownloadStatusListener = pJumpFlowDownloadStatusListener;
    }

    public void unregisterJumpFlowDownloadListener() {
        this.mJumpFlowDownloadStatusListener = null;
    }

    private int mJumpDownloadFlow;
    private boolean mReceivedDownloadFlowSuccess;
    private boolean mReceivedProviderFlowSuccess;
    private final BroadcastReceiver janrainStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras.getString("message").equalsIgnoreCase("Download flow Success!!")) {
                    // mJumpDownloadFlow++;
                    mReceivedDownloadFlowSuccess = true;
                    RLog.i(RLog.ACTIVITY_LIFECYCLE, "janrainStatusReceiver, intent = mJumpDownloadFlow" + mJumpDownloadFlow);
                } else if (extras.getString("message").equalsIgnoreCase("Provider flow Success!!")) {
                    mReceivedProviderFlowSuccess = true;
                }
                RLog.i(RLog.ACTIVITY_LIFECYCLE, "janrainStatusReceiver, intent = " + intent.toString());
                if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent.getAction()) || Jump.JR_PROVIDER_FLOW_SUCCESS.equalsIgnoreCase(intent.getAction()))
                        && (null != extras)) {

                    if (mReceivedDownloadFlowSuccess && mReceivedProviderFlowSuccess) {
                        mJanrainIntialized = true;
                        mIsInitializationInProgress = false;
                        mReceivedDownloadFlowSuccess = false;
                        mReceivedProviderFlowSuccess = false;
                        if (mJumpFlowDownloadStatusListener != null) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mJumpFlowDownloadStatusListener.onFlowDownloadSuccess();
                                }
                            }, CALL_AFTER_DELAY);


                        }
                        EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_SUCCESS);

                    }

                } else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW.equalsIgnoreCase(intent.getAction())
                        && (extras != null)) {
                    mIsInitializationInProgress = false;
                    mJanrainIntialized = false;
                    mReceivedDownloadFlowSuccess = false;
                    mReceivedProviderFlowSuccess = false;
                    if (mJumpFlowDownloadStatusListener != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mJumpFlowDownloadStatusListener.onFlowDownloadFailure();
                            }
                        }, CALL_AFTER_DELAY);

                    }
                    EventHelper.getInstance()
                            .notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);


                }
            }
        }
    };

    public void resetInitializationState() {
        mIsInitializationInProgress = false;
        mReceivedDownloadFlowSuccess = false;
        mReceivedProviderFlowSuccess = false;
    }

    private Locale mLocale;

    private Locale mUILocale;

    public void setUiLocale(String languageCode, String countryCode) {
        if (languageCode == null || languageCode.length() != 2) {
            throw new RuntimeException("Please set language code correctly . Please pass valid locale");
        }

        if (languageCode == null || languageCode.length() != 2) {
            throw new RuntimeException("Please set country code correctly .  Please pass valid locale");
        }

        mUILocale = new Locale(languageCode, countryCode);
    }

    public Locale getUiLocale() {
        return mUILocale;
    }

    public  boolean isFileEncryptionDone(final String pFileName ){
        FileInputStream fis = null;
        boolean isEncryptionDone = true;
        try {
            fis =mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object plainTextString =  ois.readObject();
            if("jr_capture_signed_in_user".equals(pFileName)){
                byte[] sss = (byte[])plainTextString;
                String s = new String(sss);
               if(s.contains("access")){
                   isEncryptionDone = false;
               }

            }else{
                if(plainTextString instanceof String){
                    isEncryptionDone = false;
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (JSONException e) {
            isEncryptionDone = false;
            e.printStackTrace();
        }*/

        return isEncryptionDone;
    }

    private void checkFileEncryptionStatus(){
        if(!isFileEncryptionDone("jr_capture_signed_in_user")){
            System.out.println("***** no encrypted jr_capture_signed_in_user");
            SecureUtility.migrateUserData("jr_capture_signed_in_user");
        }

        if(!isFileEncryptionDone("hsdpRecord")){
            System.out.println("***** no encrypted hsdpRecord");
            SecureUtility.migrateUserData("hsdpRecord");

        }

        if(!isFileEncryptionDone("diProfile")){
            System.out.println("***** no encrypted hsdpRecord");
            SecureUtility.migrateUserData("diProfile");
        }
    }

    /*
     * Initialize Janrain
     * @param isInitialized true for initialize and false for reinitialize
     * Janrain
     */
    public void intializeRegistrationSettings(final Context context,
                                              Locale locale) {

        if (Tagging.isTagginEnabled() && null == Tagging.getTrackingIdentifer()) {
            throw new RuntimeException("Please set appid for tagging before you invoke registration");
        }


        Locale mlocale = locale;
        if (isCoppaFlow()) {
            mlocale = new Locale("en", "US");
        }
        setLocale(mlocale);
        mIsInitializationInProgress = false;
        mJanrainIntialized = false;
        mReceivedProviderFlowSuccess = false;
        mReceivedDownloadFlowSuccess = false;
        mContext = context.getApplicationContext();
        ServerTime.init(mContext);
        SecureUtility.init(mContext);
        SecureUtility.generateSecretKey();


        NetworkUtility.isNetworkAvailable(mContext);
        setCountryCode(mlocale.getCountry());
        final String initLocale = mlocale.toString();
        RLog.i("LOCALE", "App JAnrain Init locale :" + initLocale);


        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!isJsonRead) {
                    isJsonRead = RegistrationStaticConfiguration.getInstance().parseConfigurationJson(mContext, RegConstants.CONFIGURATION_JSON_PATH);
                }

                ServerTime.getInstance().refreshOffset();

                if (isHsdpAvailable()) {
                    isHsdpFlow = true;
                }

                checkFileEncryptionStatus();


                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.PARSING_COMPLETED);

                    IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);

                    flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
                    flowFilter.addAction("com.janrain.android.Jump.PROVIDER_FLOW_SUCCESS");
                    if (janrainStatusReceiver != null) {
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(janrainStatusReceiver);
                    }
                    LocalBroadcastManager.getInstance(context).registerReceiver(janrainStatusReceiver,
                            flowFilter);

                    String mMicrositeId = RegistrationConfiguration.getInstance().getPilConfiguration()
                            .getMicrositeId();

                    RLog.i(RLog.JANRAIN_INITIALIZE, "Mixrosite ID : " + mMicrositeId);

                    String mRegistrationType = RegistrationConfiguration.getInstance()
                            .getPilConfiguration().getRegistrationEnvironment();
                    RLog.i(RLog.JANRAIN_INITIALIZE, "Registration Environment : " + mRegistrationType);

                    boolean mIsInitialize = false;
                    mJanrainIntialized = false;
                    mIsInitializationInProgress = true;

                    if (RegistrationEnvironmentConstants.EVAL.equalsIgnoreCase(mRegistrationType)) {
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
                                + RegistrationConfiguration.getInstance().getJanRainConfiguration()
                                .getClientIds().getEvaluationId());
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Campaign ID : "
                                + RegistrationConfiguration.getInstance().getPilConfiguration()
                                .getCampaignID());
                        initEvalSettings(mContext, RegistrationConfiguration.getInstance()
                                        .getJanRainConfiguration().getClientIds().getEvaluationId(),
                                mMicrositeId, mRegistrationType, mIsInitialize, initLocale);
                        return;
                    }
                    if (RegistrationEnvironmentConstants.PROD.equalsIgnoreCase(mRegistrationType)) {
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
                                + RegistrationConfiguration.getInstance().getJanRainConfiguration()
                                .getClientIds().getProductionId());
                        initProdSettings(mContext, RegistrationConfiguration.getInstance()
                                        .getJanRainConfiguration().getClientIds().getProductionId(),
                                mMicrositeId, mRegistrationType, mIsInitialize, initLocale);
                        return;

                    }
                    if (RegistrationEnvironmentConstants.DEV.equalsIgnoreCase(mRegistrationType)) {
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
                                + RegistrationConfiguration.getInstance().getJanRainConfiguration()
                                .getClientIds().getDevelopmentId());
                        initDevSettings(mContext, RegistrationConfiguration.getInstance()
                                        .getJanRainConfiguration().getClientIds().getDevelopmentId(),
                                mMicrositeId, mRegistrationType, mIsInitialize, initLocale);
                        return;
                    }

                    if (RegistrationEnvironmentConstants.TESTING.equalsIgnoreCase(mRegistrationType)) {
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
                                + RegistrationConfiguration.getInstance().getJanRainConfiguration()
                                .getClientIds().getTestingId());
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Campaign ID : "
                                + RegistrationConfiguration.getInstance().getPilConfiguration()
                                .getCampaignID());
                        initTesting(mContext, RegistrationConfiguration.getInstance()
                                        .getJanRainConfiguration().getClientIds().getTestingId(),
                                mMicrositeId, mRegistrationType, mIsInitialize, initLocale);
                        return;
                    }


                    if (RegistrationEnvironmentConstants.STAGING.equalsIgnoreCase(mRegistrationType)) {
                        RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
                                + RegistrationConfiguration.getInstance().getJanRainConfiguration()
                                .getClientIds().getStagingId());
                        initStaging(mContext, RegistrationConfiguration.getInstance()
                                        .getJanRainConfiguration().getClientIds().getStagingId(),
                                mMicrositeId, mRegistrationType, mIsInitialize, initLocale);
                        return;
                    }
                }
            }
        }).start();
    }

    private boolean isHsdpAvailable() {
        HSDPConfiguration hsdpConfiguration = RegistrationConfiguration.getInstance().getHsdpConfiguration();
        if (hsdpConfiguration == null  ) {
            return false;
        }

        if (hsdpConfiguration.getHsdpInfos().size()==0) {
            return false;
        }
        String environment = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        if (environment == null) {
            return false;
        }


        Configuration configuration =RegUtility.getConfiguration(environment);



        HSDPInfo hsdpInfo = hsdpConfiguration.getHSDPInfo(RegUtility.getConfiguration(environment));
        if (hsdpInfo == null) {
            throw new RuntimeException("HSDP configuration is not configured for " + environment + " environment ");
        }
        if (null != hsdpConfiguration && null != hsdpInfo) {

            String exception = null;

            if (hsdpInfo.getApplicationName() == null) {
                exception += "Application Name";
            }

            if (hsdpInfo.getSharedId() == null) {
                if (null != exception) {
                    exception += ",shared key ";
                } else {
                    exception += "shared key ";
                }
            }
            if (hsdpInfo.getSecretId() == null) {
                if (null != exception) {
                    exception += ",Secret key ";
                } else {
                    exception += "Secret key ";
                }
            }

            if (hsdpInfo.getBaseURL() == null) {
                if (null != exception) {
                    exception += ",Base Url ";
                } else {
                    exception += "Base Url ";
                }
            }

            if (null != exception) {
                throw new RuntimeException("HSDP configuration is not configured for " + RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment() + " environment for " + exception.toString().substring(4));
            }
        }


        return (null != hsdpInfo.getApplicationName() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getSecretId()
                && null != hsdpInfo.getBaseURL());
    }


    private void initEvalSettings(Context context, String captureClientId, String microSiteId,
                                  String registrationType, boolean isintialize, String locale) {

        mEvalRegistrationSettings = new EvalRegistrationSettings();
        mRegistrationSettings = mEvalRegistrationSettings;
        mEvalRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
                microSiteId, registrationType, isintialize, locale);

    }

    private void initDevSettings(Context context, String captureClientId, String microSiteId,
                                 String registrationType, boolean isintialize, String locale) {

        mDevRegistrationSettings = new DevRegistrationSettings();
        mRegistrationSettings = mDevRegistrationSettings;
        mDevRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
                microSiteId, registrationType, isintialize, locale);
    }

    private void initProdSettings(Context context, String captureClientId, String microSiteId,
                                  String registrationType, boolean isintialize, String locale) {

        mProdRegistrationSettings = new ProdRegistrationSettings();
        mRegistrationSettings = mProdRegistrationSettings;
        mProdRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
                microSiteId, registrationType, isintialize, locale);
    }

    private void initTesting(Context context, String captureClientId, String microSiteId,
                             String registrationType, boolean isintialize, String locale) {

        mTestingRegistrationSettings = new TestingRegistrationSettings();
        mRegistrationSettings = mTestingRegistrationSettings;
        mTestingRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
                microSiteId, registrationType, isintialize, locale);
    }


    private void initStaging(Context context, String captureClientId, String microSiteId,
                             String registrationType, boolean isintialize, String locale) {

        mStagingRegistrationSettings = new StaginglRegistrationSettings();
        mRegistrationSettings = mStagingRegistrationSettings;
        mStagingRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
                microSiteId, registrationType, isintialize, locale);
    }

    public RegistrationSettings getRegistrationSettings() {
        return mRegistrationSettings;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void registerUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().registerEventNotification(userRegistrationListener);
    }

    public void unRegisterUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().unregisterEventNotification(userRegistrationListener);
    }

    public UserRegistrationHelper getUserRegistrationListener() {
        return UserRegistrationHelper.getInstance();
    }


    public void registerNetworkStateListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().registerEventNotification(networStateListener);
    }

    public void unRegisterNetworkListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().unregisterEventNotification(networStateListener);
    }

    public NetworkStateHelper getNetworkStateListener() {
        return NetworkStateHelper.getInstance();
    }

    public String getAppVersion() {
        String appVersion = null;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);

            appVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {

        }
        return appVersion;
    }

    public boolean isCoppaFlow() {
        return isCoppaFlow;
    }

    public void setCoppaFlow(boolean isCoppaFlow) {
        this.isCoppaFlow = isCoppaFlow;
    }

    public Locale getLocale() {
        return mLocale;
    }

    public void setLocale(Locale mLocale) {
        this.mLocale = mLocale;
    }

    public static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isHsdpFlow() {
        return isHsdpFlow;
    }


    private RegistrationFunction prioritisedFunction = RegistrationFunction.Registration;

    public void setPrioritisedFunction(RegistrationFunction prioritisedFunction) {
        this.prioritisedFunction = prioritisedFunction;
    }

    public RegistrationFunction getPrioritisedFunction() {
        return prioritisedFunction;
    }

}
