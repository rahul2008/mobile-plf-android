package com.philips.cdp.dicommclient.cpp;

import com.philips.cdp.dicommclient.cpp.listener.AppUpdateListener;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.cpp.listener.DcsResponseListener;
import com.philips.cdp.dicommclient.cpp.listener.PublishEventListener;
import com.philips.cdp.dicommclient.cpp.listener.SendNotificationRegistrationIdListener;
import com.philips.cdp.dicommclient.cpp.listener.SignonListener;
import com.philips.cdp.dicommclient.cpp.pairing.PairingController;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ICPClientToAppInterface;

public interface CppController extends ICPClientToAppInterface, ICPEventListener {

    String NOTIFICATION_SERVICE_TAG = "3pns";
    String NOTIFICATION_PROTOCOL = "push";

    void signOnWithProvisioning();

    boolean isSignOn();

    void addSignOnListener(SignonListener signOnListener);

    void removeSignOnListener(SignonListener signOnListener);

    void setNotificationListener(SendNotificationRegistrationIdListener listener);

    void setDownloadDataListener(ICPDownloadListener downloadDataListener);

    void removeDownloadDataListener();

    void addDCSEventListener(String cppId, DcsEventListener dcsEventListener);

    //DI-Comm change. Added one more method to disable remote subscription
    void removeDCSEventListener(String cppId);

    void setDCSDiscoverEventListener(DcsEventListener mCppDiscoverEventListener);

    void addDCSResponseListener(DcsResponseListener dcsResponseListener);

    void removeDCSResponseListener(DcsResponseListener dcsResponseListener);

    void addPublishEventListener(PublishEventListener publishEventListener);

    void removePublishEventListener(PublishEventListener publishEventListener);

    void startDCSService(DCSStartListener dcsStartListener);

    void stopDCSService();

    void notifyDCSListener(String data, String fromEui64, String action, String conversationId);

    int publishEvent(String eventData, String eventType,
                     String actionName, String conversationId, int priority,
                     int ttl, String purifierEui64);

    boolean sendNotificationRegistrationId(String gcmRegistrationId, String provider);

    void downloadDataFromCPP(String query, int bufferSize);

    void setAppUpdateNotificationListener(AppUpdateListener listener);

    @Override
    void onICPCallbackEventOccurred(int eventType, int status, ICPClient icpClient);

    void startNewAppUpdateDownload();

    @Override
    boolean loadCertificates();

    /*
         * Description: Function throws exception if network not exist. If network
         * exist return from function.
         */
    @Override
    void checkNetworkSate() throws Exception;

    /*
         * Returns ICPClient version
         */
    String getICPClientVersion();

    void fetchICPComponents(String appComponentId);

    String getAppType();

    String getAppCppId();

    SignonState getSignOnState();

    ICP_CLIENT_DCS_STATE getState();

    enum ICP_CLIENT_DCS_STATE {
        STARTED, STARTING, STOPPED, STOPPING
    }

    enum SignonState {
        NOT_SIGON,
        SIGNING,
        SIGNED_ON
    }

    interface DCSStartListener {
        void onResponseReceived();
    }

    PairingController getPairingController();
}
