package com.philips.cl.di.common.ssdp.lib;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.controller.MessageController;
import com.philips.cl.di.common.ssdp.models.DeviceListModel;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.DiscoveryServiceState;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.common.ssdp.util.SSDPUtils;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("JniMissingFunction")
@Deprecated
public class SsdpService extends HandlerThread {

    private static class MessageHandler extends Handler {
        private final WeakReference<SsdpService> reference;

        MessageHandler(SsdpService service) {
            this.reference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            SsdpService service = reference.get();

            if (service != null) {
                if (msg != null) {
                    service.addDevice((DeviceModel) msg.obj);
                }
            }
        }
    }

    private static SsdpService sInstance = null;

    private MessageHandler messageHandler = new MessageHandler(sInstance);

    private MessageController mMessageController = null;

    private boolean isDiscoveryStarted;

    private DiscoveryServiceState serviceState = DiscoveryServiceState.STOPPED;

    private boolean isSocketOpen;

    private final BaseUrlParser mBaseParser;

    private DeviceListModel deviceListModel;

    // private boolean mDevicesDiscovered;

    private final Set<String> mDiscoveredDevicesSet;
    private final Map<String, Integer> mDeviceDiscoverdCounterMap;

    static {
        try {
            System.loadLibrary("ssdpJNI"); // $codepro.audit.disable relativeLibraryPath
        } catch (final UnsatisfiedLinkError e) {
            DICommLog.e(ConnectionLibContants.LOG_TAG, "failed to load libssdpJNI Interface library" + e.getMessage());
            throw e;
        }
    }

    /**
     * Method getInstance.
     *
     * @return SSDP
     */
    public static SsdpService getInstance() {
        synchronized (SsdpService.class) {
            if (sInstance == null) {
                synchronized (SsdpService.class) {
                    sInstance = new SsdpService();
                }
            }
        }

        return sInstance;
    }

    private final Set<String> lostDevices = new HashSet<String>();
    private final Runnable mDiscovery = new Runnable() {
        /** */
        private static final int DELAY_LOOP_COUNT = 1;
        /** */
        private int loopCount = 0;

        /** */


        @Override
        public void run() {
            if (++loopCount == DELAY_LOOP_COUNT) {
                DICommLog.i(ConnectionLibContants.LOG_TAG, "  :run loop: ");
                loopCount = 0;
                final Set<String> aliveDevices = deviceListModel.getAliveDevicesMap().keySet();

                for (final String usn : aliveDevices) {
                    if (mDeviceDiscoverdCounterMap.get(usn) != null) {
                        int discoveryCounter = mDeviceDiscoverdCounterMap.get(usn);
                        DICommLog.i(ConnectionLibContants.LOG_TAG, usn + ", discoveryCounter: " + discoveryCounter);
                        if (discoveryCounter > 2) {
                            DICommLog.i(ConnectionLibContants.LOG_TAG, usn + ", Device not sending notification: " + discoveryCounter);
                            mDiscoveredDevicesSet.remove(usn);
                        } else {
                            discoveryCounter++;
                            mDeviceDiscoverdCounterMap.put(usn, discoveryCounter);
                        }
                    }

                    if (!mDiscoveredDevicesSet.contains(usn)) {
                        lostDevices.add(usn);
                    }
                }

                for (final String usn : lostDevices) {
                    DICommLog.i(ConnectionLibContants.LOG_TAG, "Device Lost bye bye: " + usn);
                    mMessageController.sendInternalMessage(DiscoveryMessageID.DEVICE_LOST, deviceListModel.getDevice(usn));
                    deviceListModel.removeDevice(usn);
                    mDeviceDiscoverdCounterMap.remove(usn);
                }
                lostDevices.clear();
            }
            sendBroadcastMX5();

            if (isDiscoveryStarted && (serviceState == DiscoveryServiceState.STARTED)) {
                messageHandler.postDelayed(mDiscovery, ConnectionLibContants.SSDP_LOOP_DELAY_MS);
            }
        }
    };


    private SsdpService() {
        super(SsdpService.class.getSimpleName());
    }

    {
        mDiscoveredDevicesSet = new HashSet<String>();
        mDeviceDiscoverdCounterMap = new Hashtable<String, Integer>();
        mBaseParser = new BaseUrlParser();
        serviceState = DiscoveryServiceState.STOPPED;
        isDiscoveryStarted = false;
        deviceListModel = new DeviceListModel();
    }

    private void addDevice(final @NonNull DeviceModel device) {
        DICommLog.i(ConnectionLibContants.LOG_TAG, "addDevice");

        if ((device.getNts() == null) || device.getNts().contains(ConnectionLibContants.SSDP_ALIVE)) {
            mDeviceDiscoverdCounterMap.put(device.getUsn(), 0);
            mDiscoveredDevicesSet.add(device.getUsn());

            final DeviceModel discoveredDeviceModel = deviceListModel.getAliveDevicesMap().get(device.getUsn());
            String bootId = "";
            if (discoveredDeviceModel != null) {
                bootId = discoveredDeviceModel.getBootId();
            }
            DICommLog.i(ConnectionLibContants.LOG_TAG, "Old bootId: " + bootId + ", new bootId: " + device.getBootId());

            DICommLog.i(ConnectionLibContants.LOG_TAG, "Device alive: " + device.getUsn());
            final String location = device.getLocation();

            if (TextUtils.isEmpty(location)) {
                DICommLog.d(ConnectionLibContants.LOG_TAG, "ssdp empty description url for device: " + device.toString());
            } else {
                try {
                    DICommLog.i(ConnectionLibContants.LOG_TAG, "ssdp description url: " + location);
                    final String xmlDescription = SSDPUtils.getHTTP(new URL(location));
                    DICommLog.i(ConnectionLibContants.LOG_TAG, location + "  xmlDescription:  " + xmlDescription);
                    mBaseParser.parse(xmlDescription);
                    final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();

                    if ((deviceList != null) && (deviceList.size() > 0)) {
                        final DeviceModel rootPhilipsDevice = getRootPhilipsDevice(mBaseParser.getDevicesList(), device);

                        if ((rootPhilipsDevice != null) && (mMessageController != null)) {
                            mMessageController.sendInternalMessage(DiscoveryMessageID.DEVICE_DISCOVERED, rootPhilipsDevice);
                            deviceListModel.addNewDevice(rootPhilipsDevice.getUsn(), rootPhilipsDevice);
                        } else {
                            DICommLog.d(ConnectionLibContants.LOG_TAG, "[addDevice] %% device is not a Philips device");
                        }
                    }
                } catch (MalformedURLException e) {
                    DICommLog.e(ConnectionLibContants.LOG_TAG, "MalformedURLException: " + e.getMessage());
                }
            }
        } else if ((null != device.getNts()) && device.getNts().contains(ConnectionLibContants.SSDP_BYEBYE)) {
            DICommLog.i(ConnectionLibContants.LOG_TAG, "Device Lost bye bye: " + device.getUsn() + ":" + device.getNts());

            DeviceModel lostDevice = deviceListModel.getDevice(device.getUsn());
            mDeviceDiscoverdCounterMap.remove(device.getUsn());
            mDiscoveredDevicesSet.remove(device.getUsn());
            deviceListModel.removeDevice(device);

            mMessageController.sendInternalMessage(DiscoveryMessageID.DEVICE_LOST, lostDevice);
        }
    }

    /**
     * Method getAliveDeviceList.
     * This API returns HashSet of DeviceModel Object or null if service is not running
     * each Device object contains information about currently available/active devices in a network
     * which supports SSDP protocol
     *
     * @return Set<DeviceModel>
     */
    public Set<DeviceModel> getAliveDeviceList() {
        Set<DeviceModel> deviceModel = null;
        if (null != deviceListModel) {
            deviceModel = deviceListModel.getAliveDevices();
        }
        return deviceModel;
    }

    private DeviceModel getRootPhilipsDevice(
            final List<SSDPdevice> devicesList, final DeviceModel device) {

        if ((null != devicesList) && (devicesList.size() > 0)) {
            final SSDPdevice ssdpDevice = devicesList.get(0);
            if (null != ssdpDevice) {
                if (null != device) {
                    ssdpDevice.setBaseURL(device.getLocation().replaceAll("[\\w\\.]*$", ""));
                }
                if (null != device) {
                    device.setFromSSDP(ssdpDevice);
                    DICommLog.d(ConnectionLibContants.LOG_TAG, "device is " + device);
                }
            }
        }

        return device;
    }

    /**
     * Method registerCallback.
     *
     * @param callback Callback
     */
    private void registerCallback(final Callback callback) {
        mMessageController = MessageController.getInstance();
        mMessageController.setCallback(callback);
    }

    /**
     * Method registerHandler.
     *
     * @param handler Handler
     */
    private void registerHandler(final Handler handler) {
        mMessageController = MessageController.getInstance();
        mMessageController.addMessageHandler(handler);
    }

    /**
     * Method ssdpCallback.
     */
    protected void ssdpCallback(final String pNts, String pUsn, final String pLocation, final String pServer, final String bootId) {
        if (pServer == null) {
            DICommLog.i(ConnectionLibContants.LOG_TAG, "Not fetching xml - Server name is null");
            return;
        }

        synchronized (this) {
            DICommLog.i(ConnectionLibContants.LOG_TAG, "Fetching xml for device with server name: " + pServer);

            if ((pLocation != null) && (!pLocation.isEmpty())) {
                if ((null != pUsn) && pUsn.isEmpty()) {
                    pUsn = pUsn.replace(ConnectionLibContants.SSDP_ROOT_DEVICE, "");
                }
                final URL url;
                DeviceModel deviceParam = null;

                try {
                    url = new URL(pLocation);
                    DICommLog.i(ConnectionLibContants.LOG_TAG, "pNts: " + pNts);
                    final String ipAddress = (InetAddress.getByName(url.getHost()).getHostAddress());
                    final int port = url.getPort();
                    deviceParam = new DeviceModel(pNts, pUsn, pLocation, ipAddress, port, bootId);
                } catch (final MalformedURLException e) {
                    DICommLog.e(ConnectionLibContants.LOG_TAG, "MalformedURLException : " + e.getMessage());
                } catch (final UnknownHostException e) {
                    DICommLog.e(ConnectionLibContants.LOG_TAG, "UnknownHostException : " + e.getMessage());
                }

                if (deviceParam != null) {
                    addDevice(deviceParam);
                }
            }
        }
    }

    /**
     * Method startDeviceDiscovery.
     *
     * @param callback Callback
     */
    public void startDeviceDiscovery(final Callback callback) {
        if (callback != null) {
            registerCallback(callback);
        }
        startDiscovering();
    }

    /**
     * Method startDeviceDiscovery.
     *
     * @param handler Handler
     */
    public void startDeviceDiscovery(final Handler handler) {
        if (null != handler) {
            registerHandler(handler);
        }
        startDiscovering();
    }

    /**
     * Method enableDiscovery.
     */
    private void startDiscovering() {
        if (serviceState != DiscoveryServiceState.STARTED) {
            // throw (new IllegalStateException("SSDP not initialized"));
            // Is it better not to throw exception
            serviceState = DiscoveryServiceState.STARTED;

            if (!super.isAlive()) {
                super.start();
            }
        }

        if (!isSocketOpen && (openSocket() == 0)) {
            isSocketOpen = true;

            DICommLog.i(ConnectionLibContants.LOG_TAG, "register listener");
            registerListener();

            sendBroadcastMX3();
            sendBroadcastMX5();
            startDiscovery();

            isDiscoveryStarted = true;

            if (messageHandler != null) {
                messageHandler.postDelayed(mDiscovery, ConnectionLibContants.SSDP_LOOP_DELAY_MS);
            }
        }
    }

    /**
     * This API is used to safely stop device discovery service which
     * intern takes care of removing registered handlers from broadcasting list
     */
    public void stopDeviceDiscovery() {
        if (serviceState == DiscoveryServiceState.STARTED) {
            if (messageHandler != null && mDiscovery != null) {
                messageHandler.removeCallbacks(mDiscovery);
            }
            if (mMessageController != null && messageHandler != null) {
                mMessageController.removeMessageHandler(messageHandler);
            }

            if (isSocketOpen) {
                serviceState = DiscoveryServiceState.STOPPED;

                closeSocket();
                isSocketOpen = false;

                stopDiscovery();
                isDiscoveryStarted = false;
            }

            if (mDiscoveredDevicesSet != null) {
                mDiscoveredDevicesSet.clear();
            }

            if (mDeviceDiscoverdCounterMap != null) {
                mDeviceDiscoverdCounterMap.clear();
            }

            if (lostDevices != null) {
                lostDevices.clear();
            }

            if (deviceListModel != null && deviceListModel.getAliveDevicesMap() != null) {
                deviceListModel.getAliveDevicesMap().clear();
            }

            if (deviceListModel != null && deviceListModel.getAliveDevices() != null) {
                deviceListModel.getAliveDevices().clear();
            }
        }
    }

    private native int openSocket();

    private native void closeSocket();

    private native int registerListener();

    private native int sendBroadcastMX3();

    private native int sendBroadcastMX5();

    private native int startDiscovery();

    private native void stopDiscovery();

}
