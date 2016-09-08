/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cdp.dicommclient.cpp.listener.AppUpdateListener;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.cpp.listener.DcsResponseListener;
import com.philips.cdp.dicommclient.cpp.listener.PublishEventListener;
import com.philips.cdp.dicommclient.cpp.listener.SendNotificationRegistrationIdListener;
import com.philips.cdp.dicommclient.cpp.listener.SignonListener;
import com.philips.cdp.dicommclient.util.DICommLog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CppController implements ICPClientToAppInterface, ICPEventListener {

    public enum ICP_CLIENT_DCS_STATE {
        STARTED, STARTING, STOPPED, STOPPING
    }

    public enum SignonState {
        NOT_SIGON,
        SIGNING,
        SIGNED_ON
    }

    private enum APP_REQUESTED_STATE {
        NONE, START, STOP
    }

    private enum KeyProvision {
        NOT_PROVISIONED,
        PROVISIONING,
        PROVISIONED
    }

    public static final String NOTIFICATION_SERVICE_TAG = "3pns";
    public static final String NOTIFICATION_PROTOCOL = "push";
    private static final String CERTIFICATE_EXTENSION = ".cer";

    private static CppController sInstance;
    private static KpsConfigurationInfo mKpsConfigurationInfo;

    private final List<SignonListener> mSignOnListeners;
    private final List<PublishEventListener> mPublishEventListeners;
    private final List<DcsResponseListener> mDcsResponseListeners;

    private HashMap<String, DcsEventListener> mDcsEventListenersMap = new HashMap<>();

    private Context mContext;
    private static SignOn mSignon;

    private boolean mIsSignOn;
    private SendNotificationRegistrationIdListener mNotificationListener;
    private AppUpdateListener mAppUpdateListener;

    private ICPCallbackHandler mICPCallbackHandler;
    private EventSubscription mEventSubscription;
    private DcsEventListener mCppDiscoverEventListener;
    private DCSStartListener dcsStartListener;

    private ICP_CLIENT_DCS_STATE mDcsState = ICP_CLIENT_DCS_STATE.STOPPED;
    private APP_REQUESTED_STATE mAppDcsRequestState = APP_REQUESTED_STATE.NONE;
    private KeyProvision mKeyProvisioningState = KeyProvision.NOT_PROVISIONED;
    private SignonState mSignonState = SignonState.NOT_SIGON;

    private ICPDownloadListener mDownloadDataListener;
    private StringBuilder mDownloadDataBuilder;

    private FileOutputStream mFileOutputStream = null;
    private ComponentInfo mComponentInfo;
    private Params mKpsConfiguration;
    private String mAppCppId;

    private int mFileSize = 0;
    private int mPercentage;
    private int mByteOffset = 0;

    public static synchronized CppController createSharedInstance(Context context, KpsConfigurationInfo kpsConfigurationInfo) {
        if (sInstance != null) {
            throw new RuntimeException("CPPController can only be initialized once");
        }
        if (kpsConfigurationInfo == null || context == null) {
            throw new RuntimeException("CPPController cannot be initialized without context and kpsConfigurationInfo");
        }
        sInstance = new CppController(context, kpsConfigurationInfo);
        return sInstance;
    }

    public static synchronized CppController getInstance() {
        DICommLog.i(DICommLog.ICPCLIENT, "GetInstance: " + sInstance);
        // TODO:DICOMM Refactor, need generic mechanism to update this locale information whenever the language changes.
        setLocale();
        return sInstance;
    }

    private CppController(Context context, KpsConfigurationInfo kpsConfigurationInfo) {

        this.mContext = context;
        mKpsConfigurationInfo = kpsConfigurationInfo;
        mKpsConfiguration = new KeyProvisioningHelper(kpsConfigurationInfo);

        mICPCallbackHandler = new ICPCallbackHandler();
        mICPCallbackHandler.setHandler(this);

        mSignOnListeners = new ArrayList<>();
        mPublishEventListeners = new ArrayList<>();
        mDcsResponseListeners = new ArrayList<>();

        init();
    }

    // Only used for testing
    private CppController() {
        mSignon = null;
        mSignOnListeners = new ArrayList<>();
        mDcsResponseListeners = null;
        mPublishEventListeners = null;
    }

    public void signOnWithProvisioning() {
        if (getKeyProvisioningState() == KeyProvision.NOT_PROVISIONED) {
            DICommLog.i(DICommLog.ICPCLIENT, "startprovisioning on network change if not provisioned");
            startKeyProvisioning();
        } else if (getKeyProvisioningState() == KeyProvision.PROVISIONED && !isSignOn()) {
            DICommLog.i(DICommLog.ICPCLIENT, "start signon on network change if not signed on");
            signOn();
        }
    }

    private class KeyProvisioningException extends RuntimeException {
        public KeyProvisioningException(Throwable throwable) {
            super(throwable);
        }
    }

    private void startKeyProvisioning() {
        DICommLog.i(DICommLog.KPS, "Start provision");
        mKeyProvisioningState = KeyProvision.PROVISIONING;
        String appVersion = null;

        // set Peripheral Information
        Provision provision = new Provision(mICPCallbackHandler, mKpsConfiguration,
                null, mContext);

        // Set Application Info
        // TODO:DICOMM Refactor, replace appversion by getappversion API and check how to get app id and app type
        PackageManager pm = mContext.getPackageManager();
        String appId = mKpsConfigurationInfo.getAppId();

        try {
            appVersion = "" + pm.getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new KeyProvisioningException(e);
        }

        DICommLog.i(DICommLog.KPS, appId + ":" + mKpsConfigurationInfo.getAppType() + ":" + appVersion);
        provision.setApplicationInfo(createIdentityInformation(appVersion, appId));

        int commandResult = provision.executeCommand();
        if (commandResult != Errors.REQUEST_PENDING) {
            DICommLog.i(DICommLog.KPS, "PROVISION-FAILED");
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

    public boolean isSignOn() {
        if (mSignon == null) {
            mSignon = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }
        DICommLog.i(DICommLog.CPPCONTROLLER, "isSign " + mSignon.getSignOnStatus());
        return mSignon.getSignOnStatus();
    }

    private KeyProvision getKeyProvisioningState() {
        return mKeyProvisioningState;
    }

    /**
     * Method to inialize
     */
    private void init() {

        if (mSignon == null) {
            mSignon = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }

        mSignon.setInterfaceAndContextObject(this, mContext);

        int commandResult = mSignon.init();
        if (commandResult == Errors.SUCCESS) {
            startKeyProvisioning();
        } else {
            DICommLog.e(DICommLog.CPPCONTROLLER, "init failed " + commandResult);
        }
    }

    /**
     * This method will call the signon On Callback the status of the signon is
     * known.
     */
    private void signOn() {
        if (!mIsSignOn) {
            DICommLog.i(DICommLog.ICPCLIENT, "onSignOn");
            mIsSignOn = true;
            mSignonState = SignonState.SIGNING;

            if (mICPCallbackHandler == null) {
                mICPCallbackHandler = new ICPCallbackHandler();
                mICPCallbackHandler.setHandler(this);
            }

            mSignon.setIsFirstTime(true);
            DICommLog.i(DICommLog.ICPCLIENT, "Version: " + mSignon.clientVersion());
            int commandResult = mSignon.executeCommand();
            if (commandResult != Errors.REQUEST_PENDING) {
                mIsSignOn = false;
                mSignonState = SignonState.NOT_SIGON;
            }
        }
    }

    public void addSignOnListener(SignonListener signOnListener) {
        synchronized (mSignOnListeners) {
            if (!mSignOnListeners.contains(signOnListener)) {
                mSignOnListeners.add(signOnListener);
                DICommLog.v(DICommLog.CPPCONTROLLER, "Added signOn listener - " + signOnListener.hashCode());
            }
        }
    }

    public void removeSignOnListener(SignonListener signOnListener) {
        synchronized (mSignOnListeners) {
            if (mSignOnListeners.contains(signOnListener)) {
                mSignOnListeners.remove(signOnListener);
                DICommLog.v(DICommLog.CPPCONTROLLER, "Removed signOn listener - " + signOnListener.hashCode());
            }
        }
    }

    public void setNotificationListener(SendNotificationRegistrationIdListener listener) {
        mNotificationListener = listener;
    }

    public void setDownloadDataListener(ICPDownloadListener downloadDataListener) {
        DICommLog.i(DICommLog.INDOOR_RDCP, "setDownloadDataListener");
        this.mDownloadDataListener = downloadDataListener;
    }

    public void removeDownloadDataListener() {
        DICommLog.i(DICommLog.INDOOR_RDCP, "setDownloadDataListener");
        this.mDownloadDataListener = null;
    }

    public void addDCSEventListener(String cppId, DcsEventListener dcsEventListener) {
        //DI-Comm change. Checking the listener before adding it to the map
        if (mDcsEventListenersMap != null && !mDcsEventListenersMap.containsKey(cppId)) {
            mDcsEventListenersMap.put(cppId, dcsEventListener);
        }
    }

    //DI-Comm change. Added one more method to disable remote subscription
    public void removeDCSEventListener(String cppId) {
        if (mDcsEventListenersMap != null) {
            mDcsEventListenersMap.remove(cppId);
        }
    }

    private DcsEventListener getDCSEventListener(String cppId) {
        return mDcsEventListenersMap.get(cppId);
    }

    public void setDCSDiscoverEventListener(DcsEventListener mCppDiscoverEventListener) {
        this.mCppDiscoverEventListener = mCppDiscoverEventListener;
    }

    public void addDCSResponseListener(DcsResponseListener dcsResponseListener) {
        synchronized (mDcsResponseListeners) {
            if (!mDcsResponseListeners.contains(dcsResponseListener)) {
                mDcsResponseListeners.add(dcsResponseListener);
            }
        }
    }

    public void removeDCSResponseListener(DcsResponseListener dcsResponseListener) {
        synchronized (mDcsResponseListeners) {
            if (mDcsResponseListeners.contains(dcsResponseListener)) {
                mDcsResponseListeners.remove(dcsResponseListener);
            }
        }
    }

    public void addPublishEventListener(PublishEventListener publishEventListener) {
        synchronized (mPublishEventListeners) {
            if (!mPublishEventListeners.contains(publishEventListener)) {
                this.mPublishEventListeners.add(publishEventListener);
            }
        }
    }

    public void removePublishEventListener(PublishEventListener publishEventListener) {
        synchronized (mPublishEventListeners) {
            if (mPublishEventListeners.contains(publishEventListener)) {
                mPublishEventListeners.remove(publishEventListener);
            }
        }
    }

    public interface DCSStartListener {
        void onResponseReceived();
    }

    /**
     * This method will subscribe to events
     */
    public void startDCSService(DCSStartListener dcsStartListener) {
        DICommLog.d(DICommLog.CPPCONTROLLER, "Start DCS: isSIgnOn " + mIsSignOn + " DCS state: " + mDcsState);

        if (mDcsState == ICP_CLIENT_DCS_STATE.STOPPED) {
            this.dcsStartListener = dcsStartListener;
            mAppDcsRequestState = APP_REQUESTED_STATE.NONE;
            if (isSignOn()) {
                DICommLog.i(DICommLog.CPPCONTROLLER, "Starting DCS - Already Signed On");
                int numberOfEvents = 1;
                mEventSubscription = EventSubscription.getInstance(mICPCallbackHandler,
                        numberOfEvents);
                mEventSubscription.setFilter("");
                mEventSubscription.setServiceTag("");

                int commandResult = mEventSubscription.executeCommand();
                DICommLog.i(DICommLog.ICPCLIENT, "executeCommand commandResult " + commandResult);
                if (commandResult == Errors.REQUEST_PENDING) {
                    mDcsState = ICP_CLIENT_DCS_STATE.STARTING;
                } else {
                    mDcsState = ICP_CLIENT_DCS_STATE.STOPPED;
                }
            } else {
                mAppDcsRequestState = APP_REQUESTED_STATE.START;
                DICommLog.i(DICommLog.CPPCONTROLLER, "Failed to start DCS - not signed on");
                signOnWithProvisioning();
            }
        } else {
            mAppDcsRequestState = APP_REQUESTED_STATE.START;
        }
    }

    /**
     * Stop the DCS service
     */
    public void stopDCSService() {
        if (isSignOn() && mEventSubscription != null) {
            if (mDcsState == ICP_CLIENT_DCS_STATE.STARTED) {
                mDcsState = ICP_CLIENT_DCS_STATE.STOPPING;
                mAppDcsRequestState = APP_REQUESTED_STATE.NONE;
                DICommLog.i(DICommLog.SUBSCRIPTION, "Stop DCS service");
                mEventSubscription.stopCommand();
            } else {
                mAppDcsRequestState = APP_REQUESTED_STATE.STOP;
            }
        }
    }

    private void notifySignOnListeners(boolean signOnStatus) {
        synchronized (mSignOnListeners) {
            for (SignonListener listener : mSignOnListeners) {
                listener.signonStatus(signOnStatus);
            }
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

    public void notifyDCSListener(String data, String fromEui64, String action, String conversationId) {
        if (action == null) return;
        if (action.equalsIgnoreCase("RESPONSE")) {
            synchronized (mDcsResponseListeners) {
                for (DcsResponseListener listener : mDcsResponseListeners) {
                    listener.onDCSResponseReceived(data, conversationId);
                }
            }
        }
        if (data == null) return;

        if (mCppDiscoverEventListener != null) {
            mCppDiscoverEventListener.onDCSEventReceived(data, fromEui64, action);
        }

        if (getDCSEventListener(fromEui64) != null) {
            getDCSEventListener(fromEui64).onDCSEventReceived(data, fromEui64, action);
        }
    }

    /**
     * This method will be used to publish the events from App to Air Purifier
     * via CPP
     */
    public int publishEvent(String eventData, String eventType,
                            String actionName, String conversationId, int priority,
                            int ttl, String purifierEui64) {
        EventPublisher mEventPublisher = new EventPublisher(mICPCallbackHandler);
        int messageID = -1;
        DICommLog.i(DICommLog.ICPCLIENT, "publishEvent eventData " + eventData + " eventType "
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

    public boolean sendNotificationRegistrationId(String gcmRegistrationId, String provider) {
        if (!CppController.getInstance().isSignOn()) {
            DICommLog.e(DICommLog.CPPCONTROLLER, "Failed to send registration ID to CPP - not signed on");
            return false;
        }

        DICommLog.i(DICommLog.CPPCONTROLLER, "CPPController sendNotificationRegistrationId provider : " + provider
                + "------------RegId : " + gcmRegistrationId);

        ThirdPartyNotification thirdParty = new ThirdPartyNotification(
                mICPCallbackHandler, NOTIFICATION_SERVICE_TAG);
        thirdParty.setProtocolDetails(NOTIFICATION_PROTOCOL, provider, gcmRegistrationId);

        int commandResult = thirdParty.executeCommand();
        if (commandResult != Errors.REQUEST_PENDING) {
            DICommLog.e(DICommLog.CPPCONTROLLER, "Failed to send registration ID to CPP - immediate");
            return false;
        }
        return true;
    }

    /**
     * This method will download the data from the cpp given the query and the
     * buffer size callback from the download will happen in
     * onICPCallbackEventOccurred
     */
    public void downloadDataFromCPP(String query, int bufferSize) {
        DICommLog.i(DICommLog.CPPCONTROLLER, "downloadDataFromCPP query: " + query + ", isSignOn: " + mIsSignOn + ", state: " + mSignonState);
        try {
            DownloadData mDownloadData = new DownloadData(mICPCallbackHandler);
            mDownloadData.setDownloadDataDetails(query, 2048, 0, 0);
            mDownloadData.executeCommand();
        } catch (Exception | Error e) {
            notifyDownloadDataListener(Errors.GENERAL_ERROR, null);
            e.printStackTrace();
        }
    }

    public void setDefaultDcsState() {
        mDcsState = ICP_CLIENT_DCS_STATE.STOPPED;
        mAppDcsRequestState = APP_REQUESTED_STATE.NONE;
    }

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
        DICommLog.i(DICommLog.ICPCLIENT, "onICPCallbackEventOccurred eventType " + CppCommand.fromCommandCode(eventType) + " status " + CppError.fromErrorCode(status));
        switch (eventType) {

            case Commands.SIGNON:
                if (status == Errors.SUCCESS) {
                    DICommLog.i(DICommLog.ICPCLIENT, "SIGNON-SUCCESSFUL");
                    mIsSignOn = true;
                    mSignonState = SignonState.SIGNED_ON;
                    notifySignOnListeners(true);

                    if (mAppDcsRequestState == APP_REQUESTED_STATE.START) {
                        DICommLog.i(DICommLog.CPPCONTROLLER, "Starting DCS after sign on");
                        startDCSService(dcsStartListener);
                    }

                } else {
                    DICommLog.e(DICommLog.ICPCLIENT, "SIGNON-FAILED");
                    mIsSignOn = false;
                    mSignonState = SignonState.NOT_SIGON;
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
                DICommLog.i(DICommLog.ICPCLIENT, "Event Notification: " + status);
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
            DICommLog.i(DICommLog.KPS, "PROVISION-SUCCESS");
            mKeyProvisioningState = KeyProvision.PROVISIONED;
            Provision provision = (Provision) icpClient;
            DICommLog.i(DICommLog.KPS, "EUI64(APP-KEY): " + provision.getEUI64());
            mAppCppId = provision.getEUI64();
            signOn();
        } else {
            DICommLog.e(DICommLog.KPS, "PROVISION-FAILED");
            mKeyProvisioningState = KeyProvision.NOT_PROVISIONED;
        }
    }

    // TODO:DICOMM Refactor, check if this also can be moved to appupdater then onfiledownloadFailed callback can be removed
    private void componentDetailsEvent(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {
            DICommLog.i(DICommLog.CPPCONTROLLER, "ICPCallback FetchComponentDetails success");
            ComponentDetails componentDetails = (ComponentDetails) icpClient;
            int numberOfComponents = componentDetails.getNumberOfComponentReturned();
            DICommLog.i(DICommLog.ICPCLIENT, "Number of components: " + numberOfComponents);
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
            DICommLog.e(DICommLog.CPPCONTROLLER, "ICPCallback FetchComponentDetails failed: " + status);
        }
    }

    private void subscribeEvents(int status) {
        //TODO : Handle SUBSCRIBE_EVENTS_STOPPED and SUBSCRIBE_EVENTS_DISCONNECTED
        if (status == Errors.SUCCESS) {
            mDcsState = ICP_CLIENT_DCS_STATE.STARTED;
            DICommLog.i(DICommLog.ICPCLIENT, "State: " + mEventSubscription.getState());

            switch (mEventSubscription.getState()) {
                case EventSubscription.SUBSCRIBE_EVENTS_STOPPED:
                    mDcsState = ICP_CLIENT_DCS_STATE.STOPPED;
                    if (mAppDcsRequestState == APP_REQUESTED_STATE.START) {
                        startDCSService(dcsStartListener);
                    }
                    return;
                case EventSubscription.SUBSCRIBE_EVENTS_RECEIVED:
                    extractEvents(mEventSubscription, true);
                    break;
                default:
                    extractEvents(mEventSubscription, false);
            }

            if (mAppDcsRequestState == APP_REQUESTED_STATE.STOP) {
                stopDCSService();
            }
        }

        if (dcsStartListener != null) {
            dcsStartListener.onResponseReceived();
        }
    }

    private void extractEvents(EventSubscription eventSubscription, boolean notifyListeners) {
        int noOfEvents = eventSubscription.getNumberOfEventsReturned();
        for (int i = 0; i < noOfEvents; i++) {
            String dcsEvents = eventSubscription.getData(i);
            String fromEui64 = eventSubscription.getReplyTo(i);
            String action = eventSubscription.getAction(i);

            DICommLog.d(DICommLog.ICPCLIENT, "DCS event received from: " + fromEui64 + "    action: " + action);
            DICommLog.d(DICommLog.ICPCLIENT, "DCS event received: " + dcsEvents);

            if (notifyListeners) {
                notifyDCSListener(dcsEvents, fromEui64, action, mEventSubscription.getConversationId(i));
            }
        }
    }

    private void thirdPartyRegisterProtocolAddressEvent(int status, ICPClient icpClient) {
        ThirdPartyNotification tpns = (ThirdPartyNotification) icpClient;
        if (status == Errors.SUCCESS && tpns.getRegistrationStatus()) {
            DICommLog.i(DICommLog.CPPCONTROLLER, "Successfully registered with CPP");
            notifyNotificationListener(true);
        } else {
            DICommLog.i(DICommLog.CPPCONTROLLER, "Failed to send registration ID to CPP - errorCode: " + status);
            notifyNotificationListener(false);
        }
    }

    private void rdcpDownloadEvent(int status, ICPClient icpClient) {
        if (status == Errors.SUCCESS) {
            DownloadData downloadData = (DownloadData) icpClient;
            ByteBuffer byteBuffer = downloadData.getBuffer();
            byte[] bufferOriginal = new byte[byteBuffer.capacity()];
            for (int i = 0; i < byteBuffer.capacity(); i++) {
                bufferOriginal[i] = byteBuffer.get(i);
            }

            byteBuffer.get(bufferOriginal);

            byte[] buffer = bufferOriginal.clone();
            if (mDownloadDataBuilder == null) {
                mDownloadDataBuilder = new StringBuilder();
            }

            mDownloadDataBuilder.append(new String(buffer, Charset.defaultCharset()));

            if (downloadData.getIsDownloadComplete()) {
                DICommLog.d(DICommLog.CPPCONTROLLER, "Download complete");
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
            DICommLog.i(DICommLog.ICPCLIENT, "File download parameters are correct");
        }
    }

    private boolean isUpgradeAvailable(int versionAvailableInCPP) {
        DICommLog.i(DICommLog.ICPCLIENT, "Version at CPP:" + versionAvailableInCPP);
        if (getAppVersion() < versionAvailableInCPP) {
            DICommLog.i(DICommLog.ICPCLIENT, "Version:" + getAppVersion());
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
                    int read = 0;
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
            DICommLog.e(DICommLog.CPPCONTROLLER, "Error: " + e.getMessage());
        }
        return gs.getNumberOfCertificates() > 0;
    }

    /*
     * Description: Function throws exception if network not exist. If network
     * exist return from function.
     */
    @Override
    public void checkNetworkSate() throws Exception {
        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            // Network exist
            return;
        }
        throw new Exception("No Network Exist");
    }

    /*
     * Returns ICPClient version
     */
    public String getICPClientVersion() {
        if (mSignon == null) {
            mSignon = SignOn.getInstance(mICPCallbackHandler, mKpsConfiguration);
        }
        return mSignon.clientVersion();
    }

    static CppController getCppControllerForTesting() {
        return new CppController();
    }

    public void fetchICPComponents(String appComponentId) {
        ComponentInfo[] componentInfo = new ComponentInfo[1];

        componentInfo[0] = new ComponentInfo();
        componentInfo[0].id = appComponentId;
        componentInfo[0].versionNumber = getAppVersion();

        ComponentDetails componentDetails = new ComponentDetails(mICPCallbackHandler, componentInfo);

        int commandResult = componentDetails.executeCommand();
        if (commandResult == Errors.REQUEST_PENDING) {
            DICommLog.i(DICommLog.CPPCONTROLLER, "fetchICPComponentDetails success");
        } else {
            mAppUpdateListener.onAppUpdateDownloadFailed();
            DICommLog.e(DICommLog.CPPCONTROLLER, "fetchICPComponentDetails failed");
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
                    e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            DICommLog.i(DICommLog.CPPCONTROLLER, "Application version: " + packageInfo.versionName + " (" + packageInfo.versionCode + ")");
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void setLocale() {
        if (mKpsConfigurationInfo != null && mSignon != null) {
            DICommLog.i(DICommLog.CPPCONTROLLER, "setLocale is called, Country = " + mKpsConfigurationInfo.getCountryCode() + "Language = " + mKpsConfigurationInfo.getLanguageCode());
            mSignon.setNewLocale(mKpsConfigurationInfo.getCountryCode(), mKpsConfigurationInfo.getLanguageCode());
        }
    }

    public String getAppType() {
        return mKpsConfigurationInfo.getAppType();
    }

    public String getAppCppId() {
        return mAppCppId;
    }

    public SignonState getSignOnState() {
        return mSignonState;
    }

    public ICP_CLIENT_DCS_STATE getState() {
        return mDcsState;
    }
}
