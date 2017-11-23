/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.ICPDownloadListener;
import com.philips.cdp.cloudcontroller.api.listener.AppUpdateListener;
import com.philips.cdp.cloudcontroller.api.listener.DcsEventListener;
import com.philips.cdp.cloudcontroller.api.listener.DcsResponseListener;
import com.philips.cdp.cloudcontroller.api.listener.PublishEventListener;
import com.philips.cdp.cloudcontroller.api.listener.SendNotificationRegistrationIdListener;
import com.philips.cdp.cloudcontroller.api.listener.SignonListener;
import com.philips.cdp.cloudcontroller.api.pairing.DefaultPairingController;
import com.philips.cdp.cloudcontroller.api.pairing.PairingController;
import com.philips.cdp.cloudcontroller.api.util.LogConstants;
import com.philips.icpinterface.ComponentDetails;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.FileDownload;
import com.philips.icpinterface.GlobalStore;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ICPClientToAppInterface;
import com.philips.icpinterface.Provision;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.ThirdPartyNotification;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.ComponentInfo;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.IdentityInformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The default implementation of {@link CloudController}.
 *
 * @publicApi
 * @see CloudController
 */
public class DefaultCloudController implements CloudController, ICPClientToAppInterface, ICPEventListener {

    private static final String TAG = "DefaultCloudController";
    private static final String CERTIFICATE_EXTENSION = ".cer";
    private static final String DCS_RESPONSE = "RESPONSE";
    private static final String DCS_CHANGE = "CHANGE";

    private PairingController mPairingController;

    private enum AppRequestedState {
        NONE, START, STOP
    }

    private enum KeyProvision {
        NOT_PROVISIONED,
        PROVISIONING,
        PROVISIONED
    }

    private Context mContext;
    private KpsConfigurationInfo mKpsConfigurationInfo;

    private final Set<SignonListener> mSignOnListeners;
    private final Set<PublishEventListener> mPublishEventListeners;
    private final Set<DcsResponseListener> mDcsResponseListeners;

    private HashMap<String, DcsEventListener> mDcsEventListenersMap;
    private SignOn mSignOn;

    private boolean mIsSignOn;
    private SendNotificationRegistrationIdListener mNotificationListener;
    private AppUpdateListener mAppUpdateListener;

    private ICPCallbackHandler mICPCallbackHandler;
    private EventSubscription mEventSubscription;
    private DCSStartListener dcsStartListener;

    private ICPClientDCSState mDcsState = ICPClientDCSState.STOPPED;
    private AppRequestedState mAppDcsRequestState = AppRequestedState.NONE;
    private KeyProvision mKeyProvisioningState = KeyProvision.NOT_PROVISIONED;
    private SignOnState mSignOnState = SignOnState.NOT_SIGNED_ON;

    private ICPDownloadListener mDownloadDataListener;
    private StringBuilder mDownloadDataBuilder;

    private FileOutputStream mFileOutputStream = null;
    private ComponentInfo mComponentInfo;
    private Params mKpsConfiguration;
    private String mAppCppId;

    private int mFileSize = 0;
    private int mPercentage;
    private int mByteOffset = 0;

    public DefaultCloudController(Context context, KpsConfigurationInfo kpsConfigurationInfo) {
        this.mContext = context;

        mKpsConfigurationInfo = kpsConfigurationInfo;
        mKpsConfiguration = new KeyProvisioningHelper(kpsConfigurationInfo);

        mICPCallbackHandler = new ICPCallbackHandler(this);
        mPairingController = new DefaultPairingController(this);

        mSignOnListeners = new CopyOnWriteArraySet<>();
        mPublishEventListeners = new CopyOnWriteArraySet<>();
        mDcsResponseListeners = new CopyOnWriteArraySet<>();
        mDcsEventListenersMap = new HashMap<>();

        if (mSignOn == null) {
            mSignOn = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }

        mSignOn.setInterfaceAndContextObject(this, mContext);

        int commandResult = mSignOn.init();
        if (commandResult == Errors.SUCCESS) {
            startKeyProvisioning();
        } else {
            Log.e(LogConstants.CLOUD_CONTROLLER, "Init failed, command result: " + commandResult);
        }
        setLocale();
    }

    @VisibleForTesting
    DefaultCloudController() {
        mSignOn = null;
        mSignOnListeners = new CopyOnWriteArraySet<>();
        mDcsResponseListeners = new CopyOnWriteArraySet<>();
        mDcsEventListenersMap = new HashMap<>();
        mPublishEventListeners = null;
    }

    @Override
    public void signOnWithProvisioning() {
        if (getKeyProvisioningState() == KeyProvision.NOT_PROVISIONED) {
            Log.i(LogConstants.ICPCLIENT, "Startprovisioning on network change if not provisioned.");
            startKeyProvisioning();
        } else if (getKeyProvisioningState() == KeyProvision.PROVISIONED && !isSignOn()) {
            Log.i(LogConstants.ICPCLIENT, "Start signon on network change if not signed on.");
            signOn();
        }
    }

    private class KeyProvisioningException extends RuntimeException {
        KeyProvisioningException(Throwable throwable) {
            super(throwable);
        }
    }

    private void startKeyProvisioning() {
        Log.i(LogConstants.KPS, "Start provision");
        mKeyProvisioningState = KeyProvision.PROVISIONING;
        String appVersion;

        // set Peripheral Information
        Provision provision = new Provision(mICPCallbackHandler, mKpsConfiguration, null, mContext);

        // Set Application Info
        PackageManager pm = mContext.getPackageManager();
        String appId = mKpsConfigurationInfo.getAppId();

        try {
            appVersion = "" + pm.getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new KeyProvisioningException(e);
        }

        Log.i(LogConstants.KPS, appId + ":" + mKpsConfigurationInfo.getAppType() + ":" + appVersion);
        provision.setApplicationInfo(createIdentityInformation(appVersion, appId));

        int commandResult = provision.executeCommand();
        if (commandResult != Errors.REQUEST_PENDING) {
            Log.i(LogConstants.KPS, "PROVISION-FAILED");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new KeyProvisioningException(e);
            }
            commandResult = provision.executeCommand();
            if (commandResult != Errors.REQUEST_PENDING) {
                mKeyProvisioningState = KeyProvision.NOT_PROVISIONED;
            }
        }
    }

    private IdentityInformation createIdentityInformation(String relationshipId, String appVersion) {
        IdentityInformation identityInformation = new IdentityInformation();
        identityInformation.idInfo = relationshipId;
        identityInformation.typeInfo = mKpsConfigurationInfo.getAppType();
        identityInformation.versionInfo = appVersion;

        return identityInformation;
    }

    @Override
    public boolean isSignOn() {
        if (mSignOn == null) {
            mSignOn = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }
        Log.i(LogConstants.CLOUD_CONTROLLER, "isSign: " + mSignOn.getSignOnStatus());
        return mSignOn.getSignOnStatus();
    }

    private KeyProvision getKeyProvisioningState() {
        return mKeyProvisioningState;
    }

    /**
     * This method will call the signon On Callback the status of the signon is
     * known.
     */
    private void signOn() {
        if (!mIsSignOn) {
            Log.i(LogConstants.ICPCLIENT, "onSignOn");
            mIsSignOn = true;
            mSignOnState = SignOnState.SIGNING;

            if (mICPCallbackHandler == null) {
                mICPCallbackHandler = new ICPCallbackHandler(this);
            }

            mSignOn.setIsFirstTime(true);
            Log.i(LogConstants.ICPCLIENT, "Version: " + mSignOn.clientVersion());
            int commandResult = mSignOn.executeCommand();
            if (commandResult != Errors.REQUEST_PENDING) {
                mIsSignOn = false;
                mSignOnState = SignOnState.NOT_SIGNED_ON;
            }
        }
    }

    @Override
    public void addSignOnListener(@NonNull SignonListener signOnListener) {
        if (mSignOnListeners.add(signOnListener)) {
            Log.v(LogConstants.CLOUD_CONTROLLER, "Added signOn listener - " + signOnListener.hashCode());
        }
    }

    @Override
    public void removeSignOnListener(@NonNull SignonListener signOnListener) {
        if (mSignOnListeners.remove(signOnListener)) {
            Log.v(LogConstants.CLOUD_CONTROLLER, "Removed signOn listener - " + signOnListener.hashCode());
        }
    }

    @Override
    public void setNotificationListener(SendNotificationRegistrationIdListener listener) {
        mNotificationListener = listener;
    }

    @Override
    public void setDownloadDataListener(ICPDownloadListener downloadDataListener) {
        Log.i(LogConstants.INDOOR_RDCP, "setDownloadDataListener");
        this.mDownloadDataListener = downloadDataListener;
    }

    @Override
    public void removeDownloadDataListener() {
        Log.i(LogConstants.INDOOR_RDCP, "setDownloadDataListener");
        this.mDownloadDataListener = null;
    }

    @Override
    public void addDCSEventListener(@NonNull String cppId, @NonNull DcsEventListener dcsEventListener) {
        //DI-Comm change. Checking the listener before adding it to the map
        if (mDcsEventListenersMap != null && !mDcsEventListenersMap.containsKey(cppId)) {
            mDcsEventListenersMap.put(cppId, dcsEventListener);
        }
    }

    //DI-Comm change. Added one more method to disable remote subscription
    @Override
    public void removeDCSEventListener(@NonNull String cppId) {
        if (mDcsEventListenersMap != null) {
            mDcsEventListenersMap.remove(cppId);
        }
    }

    private DcsEventListener getDCSEventListener(String cppId) {
        return mDcsEventListenersMap.get(cppId);
    }

    @Override
    public void setDCSDiscoverEventListener(DcsEventListener mCppDiscoverEventListener) {
    }

    @Override
    public void addDCSResponseListener(@NonNull DcsResponseListener dcsResponseListener) {
        mDcsResponseListeners.add(dcsResponseListener);
    }

    @Override
    public void removeDCSResponseListener(@NonNull DcsResponseListener dcsResponseListener) {
        mDcsResponseListeners.remove(dcsResponseListener);
    }

    @Override
    public void addPublishEventListener(@NonNull PublishEventListener publishEventListener) {
        this.mPublishEventListeners.add(publishEventListener);
    }

    @Override
    public void removePublishEventListener(@NonNull PublishEventListener publishEventListener) {
        mPublishEventListeners.remove(publishEventListener);
    }

    /**
     * This method will subscribe to events
     */
    @Override
    public void startDCSService(DCSStartListener dcsStartListener) {
        Log.d(LogConstants.CLOUD_CONTROLLER, "Start DCS: isSIgnOn " + mIsSignOn + " DCS state: " + mDcsState);

        if (mDcsState == ICPClientDCSState.STOPPED) {
            this.dcsStartListener = dcsStartListener;
            mAppDcsRequestState = AppRequestedState.NONE;
            if (isSignOn()) {
                Log.i(LogConstants.CLOUD_CONTROLLER, "Starting DCS - Already Signed On");
                int numberOfEvents = 1;
                mEventSubscription = EventSubscription.getInstance(mICPCallbackHandler,
                        numberOfEvents);
                mEventSubscription.setFilter("");
                mEventSubscription.setServiceTag("");

                int commandResult = mEventSubscription.executeCommand();
                Log.i(LogConstants.ICPCLIENT, "executeCommand commandResult " + commandResult);
                if (commandResult == Errors.REQUEST_PENDING) {
                    mDcsState = ICPClientDCSState.STARTING;
                } else {
                    mDcsState = ICPClientDCSState.STOPPED;
                }
            } else {
                mAppDcsRequestState = AppRequestedState.START;
                Log.i(LogConstants.CLOUD_CONTROLLER, "Failed to start DCS - not signed on");
                signOnWithProvisioning();
            }
        } else {
            mAppDcsRequestState = AppRequestedState.START;
        }
    }

    /**
     * Stop the DCS service
     */
    @Override
    public void stopDCSService() {
        if (isSignOn() && mEventSubscription != null) {
            if (mDcsState == ICPClientDCSState.STARTED) {
                mDcsState = ICPClientDCSState.STOPPING;
                mAppDcsRequestState = AppRequestedState.NONE;
                Log.i(LogConstants.SUBSCRIPTION, "Stop DCS service");
                mEventSubscription.stopCommand();
            } else {
                mAppDcsRequestState = AppRequestedState.STOP;
            }
        }
    }

    private void notifySignOnListeners(boolean signOnStatus) {
        for (SignonListener listener : mSignOnListeners) {
            listener.signonStatus(signOnStatus);
        }
    }

    private void notifyNotificationListener(boolean success) {
        if (mNotificationListener == null) return;
        if (success) {
            mNotificationListener.onRegistrationIdSentSuccess();
        } else {
            mNotificationListener.onRegistrationIdSentFailed();
        }
    }

    @Override
    public void notifyDCSListener(String data, String fromEui64, String action, String conversationId) {
        if (action == null || data == null) return;

        switch(action) {
            case DCS_RESPONSE:
                // DICOMM-RESPONSE
                for (DcsResponseListener listener : mDcsResponseListeners) {
                    listener.onDCSResponseReceived(data, conversationId);
                }
                break;
            case DCS_CHANGE:
                // DICOMM-CHANGE
                DcsEventListener eventListener = getDCSEventListener(fromEui64);
                if (eventListener != null) {
                    eventListener.onDCSEventReceived(data, fromEui64, action);
                }
                break;
            default:
                // Unsupported action
                String logData = "Action: " + action + ", data: " + data;
                Log.e(TAG, "Received a DCS message but action was not supported. " + logData);
                break;
        }
    }

    /**
     * This method will be used to publish the events from App to Air Purifier
     * via CPP
     */
    @Override
    public int publishEvent(String eventData, String eventType,
                            String actionName, String conversationId, int priority,
                            int ttl, String purifierEui64) {
        EventPublisher mEventPublisher = new EventPublisher(mICPCallbackHandler);
        int messageID = -1;
        Log.i(LogConstants.ICPCLIENT, "publishEvent eventData " + eventData + " eventType "
                + eventType + " Action Name: " + actionName +
                " replyTo: " + mAppCppId + " + isSignOn " + mIsSignOn);
        if (mIsSignOn) {
            mEventPublisher.setEventInformation(eventType, actionName,
                    mAppCppId, conversationId, priority, ttl);
            mEventPublisher.setEventData(eventData);
            if (purifierEui64 != null) {
                mEventPublisher.setTargets(new String[]{purifierEui64});
            } else {
                mEventPublisher.setTargets(new String[0]);
            }
            mEventPublisher.setEventCommand(Commands.PUBLISH_EVENT);
            mEventPublisher.executeCommand();
            messageID = mEventPublisher.getMessageId();
        }
        return messageID;
    }

    @Override
    public boolean sendNotificationRegistrationId(String gcmRegistrationId, String provider) {
        if (!isSignOn()) {
            Log.e(LogConstants.CLOUD_CONTROLLER, "Failed to send registration ID to CPP - not signed on");
            return false;
        }

        Log.i(LogConstants.CLOUD_CONTROLLER, "CPPController sendNotificationRegistrationId provider : " + provider
                + "------------RegId : " + gcmRegistrationId);

        ThirdPartyNotification thirdParty = new ThirdPartyNotification(
                mICPCallbackHandler, NOTIFICATION_SERVICE_TAG);
        thirdParty.setProtocolDetails(NOTIFICATION_PROTOCOL, provider, gcmRegistrationId);

        int commandResult = thirdParty.executeCommand();
        if (commandResult != Errors.REQUEST_PENDING) {
            Log.e(LogConstants.CLOUD_CONTROLLER, "Failed to send registration ID to CPP - immediate");
            return false;
        }
        return true;
    }

    /**
     * This method will download the data from the cpp given the query and the
     * buffer size callback from the download will happen in
     * onICPCallbackEventOccurred
     */
    @Override
    public void downloadDataFromCPP(String query, int bufferSize) {
        Log.i(LogConstants.CLOUD_CONTROLLER, "downloadDataFromCPP query: " + query + ", isSignOn: " + mIsSignOn + ", state: " + mSignOnState);
        try {
            DownloadData mDownloadData = new DownloadData(mICPCallbackHandler);
            mDownloadData.setDownloadDataDetails(query, 2048, 0, 0);
            mDownloadData.executeCommand();
        } catch (Exception | Error e) {
            notifyDownloadDataListener(Errors.GENERAL_ERROR, null);
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void setAppUpdateNotificationListener(AppUpdateListener listener) {
        this.mAppUpdateListener = listener;
    }

    /***
     * This is the callback method for all the CPP related events. (Signon,
     * Publish Events, Subscription, etc..)
     */
    @Override
    public void onICPCallbackEventOccurred(int eventType, int status,
                                           ICPClient icpClient) {
        Log.i(LogConstants.ICPCLIENT, "onICPCallbackEventOccurred eventType " + CloudCommand.fromCommandCode(eventType) + " status " + CloudError.fromErrorCode(status));
        switch (eventType) {

            case Commands.SIGNON:
                if (status == Errors.SUCCESS) {
                    Log.i(LogConstants.ICPCLIENT, "SIGNON-SUCCESSFUL");
                    mIsSignOn = true;
                    mSignOnState = SignOnState.SIGNED_ON;
                    notifySignOnListeners(true);

                    if (mAppDcsRequestState == AppRequestedState.START) {
                        Log.i(LogConstants.CLOUD_CONTROLLER, "Starting DCS after sign on");
                        startDCSService(dcsStartListener);
                    }
                } else {
                    Log.e(LogConstants.ICPCLIENT, "SIGNON-FAILED");
                    mIsSignOn = false;
                    mSignOnState = SignOnState.NOT_SIGNED_ON;
                    notifySignOnListeners(false);
                }
                break;
            case Commands.PUBLISH_EVENT:
                EventPublisher eventPublisher = (EventPublisher) icpClient;
                for (PublishEventListener listener : mPublishEventListeners) {
                    listener.onPublishEventReceived(status, eventPublisher.getMessageId(), eventPublisher.getEventId());
                }
                break;
            case Commands.KEY_PROVISION:
                keyProvisionEvent(status, icpClient);
                break;
            case Commands.EVENT_NOTIFICATION:
                Log.i(LogConstants.ICPCLIENT, "Event Notification: " + status);
                if (status == Errors.SUCCESS) {
                    if (mAppUpdateListener != null) {
                        mAppUpdateListener.onAppUpdateAvailable();
                    }
                }
                break;
            case Commands.GET_COMPONENT_DETAILS:
                componentDetailsEvent(status, icpClient);
                break;
            case Commands.SUBSCRIBE_EVENTS:
                subscribeEvents(status);
                break;
            case Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS:
                thirdPartyRegisterProtocolAddressEvent(status, icpClient);
                break;
            case Commands.DOWNLOAD_DATA:
                rdcpDownloadEvent(status, icpClient);
                break;
            case Commands.DOWNLOAD_FILE:
                startFileDownload(status, icpClient);
                break;
            default:
                break;
        }
    }

    private void keyProvisionEvent(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {
            Log.i(LogConstants.KPS, "PROVISION-SUCCESS");
            mKeyProvisioningState = KeyProvision.PROVISIONED;
            Provision provision = (Provision) icpClient;
            Log.i(LogConstants.KPS, "EUI64(APP-KEY): " + provision.getEUI64());
            mAppCppId = provision.getEUI64();
            signOn();
        } else {
            Log.e(LogConstants.KPS, "PROVISION-FAILED");
            mKeyProvisioningState = KeyProvision.NOT_PROVISIONED;
        }
    }

    private void componentDetailsEvent(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {
            Log.i(LogConstants.CLOUD_CONTROLLER, "ICPCallback FetchComponentDetails success");
            ComponentDetails componentDetails = (ComponentDetails) icpClient;
            int numberOfComponents = componentDetails.getNumberOfComponentReturned();
            Log.i(LogConstants.ICPCLIENT, "Number of components: " + numberOfComponents);
            for (int index = 0; index < numberOfComponents; index++) {
                if (componentDetails.getComponentInfo(index).id.equals(mKpsConfigurationInfo.getComponentId())) {
                    // Start software download
                    if (isUpgradeAvailable(componentDetails.getComponentInfo(index).versionNumber)) {
                        //downloadNewApplication(componentDetails.getComponentInfo(index));
                        mComponentInfo = componentDetails.getComponentInfo(index);
                        mAppUpdateListener.onAppUpdateInfoDownloaded();
                        break;
                    }
                }
            }
        } else {
            //downloadFailed() ;
            mAppUpdateListener.onAppUpdateDownloadFailed();
            Log.e(LogConstants.CLOUD_CONTROLLER, "ICPCallback FetchComponentDetails failed: " + status);
        }
    }

    private void subscribeEvents(int status) {
        if (status == Errors.SUCCESS) {
            mDcsState = ICPClientDCSState.STARTED;
            Log.i(LogConstants.ICPCLIENT, "State: " + mEventSubscription.getState());

            switch (mEventSubscription.getState()) {
                case EventSubscription.SUBSCRIBE_EVENTS_STOPPED:
                    mDcsState = ICPClientDCSState.STOPPED;
                    if (mAppDcsRequestState == AppRequestedState.START) {
                        startDCSService(dcsStartListener);
                    }
                    return;
                case EventSubscription.SUBSCRIBE_EVENTS_RECEIVED:
                    extractEvents(true);
                    break;
                default:
                    extractEvents(false);
            }

            if (mAppDcsRequestState == AppRequestedState.STOP) {
                stopDCSService();
            }
        }

        if (dcsStartListener != null) {
            dcsStartListener.onResponseReceived();
        }
    }

    private void extractEvents(boolean notifyListeners) {
        int noOfEvents = mEventSubscription.getNumberOfEventsReturned();
        for (int i = 0; i < noOfEvents; i++) {
            String dcsEvents = mEventSubscription.getData(i);
            String fromEui64 = mEventSubscription.getReplyTo(i);
            String action = mEventSubscription.getAction(i);
            String conversationId = mEventSubscription.getConversationId(i);

            Log.d(LogConstants.ICPCLIENT, "DCS event received from: " + fromEui64 + "    action: " + action);
            Log.d(LogConstants.ICPCLIENT, "DCS event received: " + dcsEvents);

            if (notifyListeners) {
                notifyDCSListener(dcsEvents, fromEui64, action, conversationId);
            }
        }
    }

    private void thirdPartyRegisterProtocolAddressEvent(int status, ICPClient icpClient) {
        ThirdPartyNotification tpns = (ThirdPartyNotification) icpClient;
        if (status == Errors.SUCCESS && tpns.getRegistrationStatus()) {
            Log.i(LogConstants.CLOUD_CONTROLLER, "Successfully registered with CPP");
            notifyNotificationListener(true);
        } else {
            Log.i(LogConstants.CLOUD_CONTROLLER, "Failed to send registration ID to CPP - errorCode: " + status);
            notifyNotificationListener(false);
        }
    }

    private void rdcpDownloadEvent(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {
            DownloadData downloadData = (DownloadData) icpClient;
            ByteBuffer byteBuffer = downloadData.getBuffer();
            byte[] bufferOriginal = new byte[byteBuffer.capacity()];

            byteBuffer.rewind();
            byteBuffer.get(bufferOriginal);

            byte[] buffer = bufferOriginal.clone();
            if (mDownloadDataBuilder == null) {
                mDownloadDataBuilder = new StringBuilder();
            }

            mDownloadDataBuilder.append(new String(buffer, Charset.defaultCharset()));

            if (downloadData.getIsDownloadComplete()) {
                Log.d(LogConstants.CLOUD_CONTROLLER, "Download complete");
                if (mDownloadDataListener != null) {
                    String downloadedData = mDownloadDataBuilder.toString();
                    mDownloadDataBuilder.setLength(0);
                    notifyDownloadDataListener(status, downloadedData);
                }
            }
        } else {
            notifyDownloadDataListener(status, null);
        }
    }

    private void notifyDownloadDataListener(int status, String downloadedData) {
        if (mDownloadDataListener != null) {
            mDownloadDataListener.onDataDownload(status, downloadedData);
        }
    }

    @Override
    public void startNewAppUpdateDownload() {
        FileDownload fileDownload = new FileDownload(mICPCallbackHandler);

        fileDownload.setURL(mComponentInfo.url);
        fileDownload.setSecurityKey(mComponentInfo.secuirtyKey);
        fileDownload.setCRC(mComponentInfo.crc);
        fileDownload.setChunkSize(10240);
        fileDownload.setSize(mComponentInfo.size);
        int mCntOffset = 0;
        fileDownload.setOffset(mCntOffset);

        int commandResult = fileDownload.executeCommand();
        if (commandResult == Errors.REQUEST_PENDING) {
            Log.i(LogConstants.ICPCLIENT, "File download parameters are correct");
        }
    }

    private boolean isUpgradeAvailable(int versionAvailableInCPP) {
        Log.i(LogConstants.ICPCLIENT, "Version at CPP:" + versionAvailableInCPP);
        if (getAppVersion() < versionAvailableInCPP) {
            Log.i(LogConstants.ICPCLIENT, "Version:" + getAppVersion());
            return true;
        }
        return false;
    }

    @Override
    public boolean loadCertificates() {
        GlobalStore gs = GlobalStore.getInstance();

        // Read certificates
        byte[] buffer = new byte[1024];
        try {
            String assetFiles[] = mContext.getAssets().list("");
            InputStream in;

            for (String asset : assetFiles) {
                if (asset.contains(CERTIFICATE_EXTENSION)) {
                    in = mContext.getAssets().open(asset);
                    int read;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((read = in.read(buffer, 0, buffer.length)) != -1) {
                        baos.write(buffer, 0, read);
                    }
                    baos.flush();
                    in.close();
                    gs.setCertificateByteArray(baos.toByteArray());
                    baos.close();
                }
            }
        } catch (IOException e) {
            Log.e(LogConstants.CLOUD_CONTROLLER, "Error reading certificate file: " + e.getMessage());
        }
        return gs.getNumberOfCertificates() > 0;
    }

    /*
     * Description: Function throws exception if network is not available. If network
     * is available return from function.
     */
    @Override
    public void checkNetworkSate() throws IllegalStateException {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return;
        }
        throw new IllegalStateException("Network unavailable.");
    }

    /*
     * Returns ICPClient version
     */
    @Override
    public String getICPClientVersion() {
        if (mSignOn == null) {
            mSignOn = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }
        return mSignOn.clientVersion();
    }

    @Override
    public void fetchICPComponents(String appComponentId) {
        ComponentInfo[] componentInfo = new ComponentInfo[1];

        componentInfo[0] = new ComponentInfo();
        componentInfo[0].id = appComponentId;
        componentInfo[0].versionNumber = getAppVersion();

        ComponentDetails componentDetails = new ComponentDetails(mICPCallbackHandler, componentInfo);

        int commandResult = componentDetails.executeCommand();
        if (commandResult == Errors.REQUEST_PENDING) {
            Log.i(LogConstants.CLOUD_CONTROLLER, "fetchICPComponentDetails success");
        } else {
            mAppUpdateListener.onAppUpdateDownloadFailed();
            Log.e(LogConstants.CLOUD_CONTROLLER, "fetchICPComponentDetails failed");
        }
    }

    private void startFileDownload(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {

            FileDownload fileDownload = (FileDownload) icpClient;
            if (mFileOutputStream == null) {
                mAppUpdateListener.onAppUpdateDownloadStart(mPercentage);
                mFileSize = fileDownload.getFileSize();
                createFileOutputStream();
            }
            if (mFileOutputStream != null) {
                try {
                    ByteBuffer byteBuffer = fileDownload.getBuffer();
                    byte[] bufferOriginal = new byte[byteBuffer.capacity()];
                    for (int i = 0; i < byteBuffer.capacity(); i++) {
                        bufferOriginal[i] = byteBuffer.get(i);
                    }

                    byte[] buffer = bufferOriginal.clone();

                    mFileOutputStream.write(buffer);
                    mByteOffset += buffer.length;
                    float currentPercentage = (mByteOffset / (float) mFileSize) * 100;

                    if (mPercentage != (int) currentPercentage) {
                        mPercentage = (int) currentPercentage;
                        mAppUpdateListener.onAppUpdateDownloadProgress(mPercentage);
                    }

                    if (fileDownload.getDownloadStatus()) {
                        //In downloading time, if Internet disconnect, then we are getting getDownloadStatus() true.
                        // So we added double check as file size.
                        if (fileDownload.getDownloadProgress() == mFileSize) {
                            // Reset the offset
                            mPercentage = 0;
                            mByteOffset = 0;
                            closeFileOutputStream();
                            mAppUpdateListener.onAppUpdateDownloadComplete();
                        } else {
                            mAppUpdateListener.onAppUpdateDownloadFailed();
                        }
                    }
                } catch (IOException e) {
                    mAppUpdateListener.onAppUpdateDownloadFailed();
                    Log.e(TAG, "Error while downloading file: " + e.getMessage());
                }
            }
        } else {
            mAppUpdateListener.onAppUpdateDownloadFailed();
        }
    }

    private void createFileOutputStream() {
        try {
            File outFile = mAppUpdateListener.createFileForAppUpdateDownload();
            if (outFile == null) {
                mFileOutputStream = null;
                return;
            }
            mFileOutputStream = new FileOutputStream(outFile);
            mByteOffset = 0;
        } catch (FileNotFoundException e) {
            mAppUpdateListener.onAppUpdateDownloadFailed();
            Log.e(TAG, e.getMessage());
            mFileOutputStream = null;
        }
    }

    private void closeFileOutputStream() {
        try {
            if (mFileOutputStream != null) {
                mFileOutputStream.close();
                mFileOutputStream = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            Log.i(LogConstants.CLOUD_CONTROLLER, "Application version: " + packageInfo.versionName + " (" + packageInfo.versionCode + ")");
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new IllegalStateException("Could not get package name: " + e);
        }
    }

    private void setLocale() {
        if (mKpsConfigurationInfo != null && mSignOn != null) {
            Log.i(LogConstants.CLOUD_CONTROLLER, "setLocale is called, Country = " + mKpsConfigurationInfo.getCountryCode() + "Language = " + mKpsConfigurationInfo.getLanguageCode());
            mSignOn.setNewLocale(mKpsConfigurationInfo.getCountryCode(), mKpsConfigurationInfo.getLanguageCode());
        }
    }

    @Override
    public String getAppType() {
        return mKpsConfigurationInfo.getAppType();
    }

    @Override
    public String getAppCppId() {
        return mAppCppId;
    }

    @Override
    public SignOnState getSignOnState() {
        return mSignOnState;
    }

    @Override
    public ICPClientDCSState getState() {
        return mDcsState;
    }

    @Override
    public PairingController getPairingController() {
        return mPairingController;
    }
}
