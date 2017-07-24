package com.philips.cl.di.dev.pa.demo;

import java.net.HttpURLConnection;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;

import com.philips.cdp.dicommclient.communication.LocalStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class DemoModeBroadcastReceiver extends BroadcastReceiver implements
        Runnable {

    private DemoModeListener listener;
    private boolean isRegistered;
    private int errorCode = DemoModeConstant.DEMO_MODE_ERROR_NOT_IN_PHILIPS_SETUP;
    private boolean stop = true;
    private int totalTime = 10 * 1000;
    private AirPurifier tempDemoModePurifier;
    private int taskType = DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET;
    private IntentFilter filter = new IntentFilter();
    private DeviceDetailState deviceDetailState = DeviceDetailState.NONE;

    public DemoModeBroadcastReceiver(DemoModeListener listener) {
        this.listener = listener;
        generateTempDemoModeDevice();
    }

    @Override
    public void run() {
        int timeElapsed = 0;
        while (!stop) {
            try {
                Thread.sleep(1000);
                timeElapsed = timeElapsed + 1000;
                if (timeElapsed == totalTime) {
                    timeElapsed = 0;
                    // StartScan
                }
            } catch (InterruptedException e) {
                ALog.e(ALog.DEMO_MODE, "Runnable thread: " + "Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ALog.i(ALog.DEMO_MODE, "On Receive:" + intent.getAction());
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
                    .getSystemService(Context.WIFI_SERVICE);

            WifiInfo connectedWifiNetwork = wifiMan.getConnectionInfo();

            if (connectedWifiNetwork.getSupplicantState() == SupplicantState.COMPLETED) {

                String ssid = EWSWifiManager.getSsidOfSupplicantNetwork();
                if (ssid == null) {
                    ALog.i(ALog.DEMO_MODE,
                            "Failed to get ssid of connected network");
                    return;
                }

                if (ssid.contains(EWSWifiManager.DEVICE_SSID)) {
                    ALog.i(ALog.DEMO_MODE, "Connected to AirPurifier - Ssid= "
                            + ssid);
                    errorCode = DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED;
                    getDeviceDetails();
                    return;
                }
            }
        }
    }

    public void connectToDeviceAP() {
        ALog.i(ALog.DEMO_MODE, "connecttoDevice AP");
        deviceDetailState = DeviceDetailState.NONE;
        WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.disconnect();
        new Thread(new Runnable() {

            @Override
            public void run() {
                EWSWifiManager.connectToPhilipsSetup();
            }
        }).start();

        startScanForDeviceAp();
        deviceSSIDTimer.start();
    }

    public void registerListener() {
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (!isRegistered) {
            PurAirApplication.getAppContext().registerReceiver(this, filter);
            isRegistered = true;
        }
    }

    public void startScanForDeviceAp() {
        registerListener();
        if (stop) {
            stop = false;
            new Thread(this).start();
        }
    }

    public void unRegisterListener() {
        if (isRegistered) {
            stop = true;
            PurAirApplication.getAppContext().unregisterReceiver(this);
            isRegistered = false;
        }
    }

    private CountDownTimer deviceSSIDTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            stop = true;
            unRegisterListener();
            listener.onErrorOccur(errorCode);
        }
    };

    public void stopSSIDTimer() {
        if (deviceSSIDTimer != null) {
            deviceSSIDTimer.cancel();
        }
    }

    private void generateTempDemoModeDevice() {
        String tempEui64 = UUID.randomUUID().toString();


        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(-1);
        networkNode.setCppId(tempEui64);
        networkNode.setIpAddress(EWSConstant.PURIFIER_ADHOCIP);
        networkNode.setName(DemoModeConstant.DEMO);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        DISecurity diSecurity = new DISecurity(networkNode);
        LocalStrategy communicationStrategy = new LocalStrategy(diSecurity, networkNode);

        tempDemoModePurifier = new AirPurifier(networkNode, communicationStrategy);
    }

    private void updateTempDevice(String eui64) {
        if (tempDemoModePurifier == null)
            return;
        String encryptionKey = tempDemoModePurifier.getNetworkNode().getEncryptionKey();
        String purifierName = DemoModeConstant.DEMO;

        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(-1);
        networkNode.setCppId(eui64);
        networkNode.setIpAddress(EWSConstant.PURIFIER_ADHOCIP);
        networkNode.setName(purifierName);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        DISecurity diSecurity = new DISecurity(networkNode);
        LocalStrategy communicationStrategy = new LocalStrategy(diSecurity, networkNode);

        tempDemoModePurifier = new AirPurifier(networkNode, communicationStrategy);
        // TODO DIComm Refactor - remove this line
        tempDemoModePurifier.getNetworkNode().setEncryptionKey(encryptionKey);

        AirPurifierManager.getInstance().setCurrentAppliance(tempDemoModePurifier);
        PurAirApplication.setDemoModePurifier(eui64);
    }

    private void getDeviceDetails() {
        if (deviceDetailState == DeviceDetailState.NONE) {
            deviceDetailState = DeviceDetailState.START;
            ALog.i(ALog.DEMO_MODE, "device details");
            taskType = DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET;

            final DevicePort devicePort = tempDemoModePurifier.getDevicePort();
            devicePort.addPortListener(new DICommPortListener() {

                @Override
                public void onPortUpdate(DICommPort<?> port) {
                    receiveServerResponse(HttpURLConnection.HTTP_OK, (DevicePortProperties) port.getPortProperties(), null);
                    port.removePortListener(this);
                }

                @Override
                public void onPortError(DICommPort<?> port, Error error, String errorData) {
                    receiveServerResponse(-1, null, null);
                    port.removePortListener(this);
                }
            });

            devicePort.getPortProperties();
        }
    }

    private void getWifiDetails() {
        ALog.i(ALog.DEMO_MODE, "gettWifiDetails");
        taskType = DemoModeConstant.DEMO_MODE_TASK_WIFI_GET;

        final WifiPort wifiPort = tempDemoModePurifier.getWifiPort();
        wifiPort.addPortListener(new DICommPortListener() {

            @Override
            public void onPortUpdate(DICommPort<?> port) {
                receiveServerResponse(HttpURLConnection.HTTP_OK, null, (WifiPortProperties) port.getPortProperties());
                port.removePortListener(this);
            }

            @Override
            public void onPortError(DICommPort<?> port, Error error, String errorData) {
                receiveServerResponse(-1, null, null);
                port.removePortListener(this);
            }
        });

        wifiPort.getPortProperties();
    }

    public void receiveServerResponse(int responseCode, DevicePortProperties devicePortProperties, WifiPortProperties wifiPortProperties) {
        stop = true;
        ALog.i(ALog.DEMO_MODE, "onTaskCompleted:" + responseCode);
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                if (taskType == DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET) {
                    SessionDto.getInstance().setDeviceDto(devicePortProperties);
                    if (devicePortProperties == null)
                        return;
                    tempDemoModePurifier.getNetworkNode().setName(DemoModeConstant.DEMO);
                    getWifiDetails();
                } else if (taskType == DemoModeConstant.DEMO_MODE_TASK_WIFI_GET) {
                    SessionDto.getInstance().setDeviceWifiDto(wifiPortProperties);

                    if (wifiPortProperties != null) {
                        this.updateTempDevice(wifiPortProperties.getCppid());
                    }

                    deviceSSIDTimer.cancel();
                    listener.onHandShakeWithDevice();
                }
                break;
            default:
                stop = true;
                stopSSIDTimer();

                if (taskType == DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET
                        || taskType == DemoModeConstant.DEMO_MODE_TASK_WIFI_GET) {
                    listener.onErrorOccur(DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED);
                }
                break;
        }
    }
}
