package com.philips.cl.di.common.ssdp.lib;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.MessageID;
import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.controller.MessageController;
import com.philips.cl.di.common.ssdp.models.DeviceListModel;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.common.ssdp.models.ServiceState;
import com.philips.cl.di.common.ssdp.util.Utils;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class SsdpService extends HandlerThread {
	private static SsdpService sInstance = null;
	static {
		try {
			System.loadLibrary("ssdpJNI"); // $codepro.audit.disable relativeLibraryPath
		} catch (final UnsatisfiedLinkError e) {
			Log.e(ConnectionLibContants.LOG_TAG, "failed to load libssdpJNI Interface library" + e.getMessage());
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

	private final BaseUrlParser mBaseParser;

	private DeviceListModel mDeviceListModel;

	private boolean mDevicesDiscovered;

	private final Set<String> mDiscoveredDevicesSet;

	private final Runnable mDiscovery = new Runnable() {
		/** */
		private static final int DELAY_LOOP_COUNT = 2;
		/** */
		private int loopCount = 0;
		/** */
		private final Set<String> lostDevices = new HashSet<String>();

		@Override
		public void run() {
			if (++loopCount == DELAY_LOOP_COUNT) {
				loopCount = 0;
				final Set<String> aliveDevices = mDeviceListModel.getAliveDevicesMap().keySet();
				for (final String usn : aliveDevices) {
					if (!mDiscoveredDevicesSet.contains(usn)) {
						lostDevices.add(usn);
					}
				}
				// Log.v(LOG, "%% lostDevices" + lostDevices);
				for (final String usn : lostDevices) {
					mMessageController.sendInternalMessage(MessageID.DEVICE_LOST, new DeviceModel(usn));
					mDeviceListModel.removeDevice(usn);
					Log.d(ConnectionLibContants.LOG_TAG, "Lost device: " + usn);
				}
				lostDevices.clear();
				mDiscoveredDevicesSet.clear();
			}
			sendBroadcastMX5();

			if (mRunDiscovery && (mServiceState == ServiceState.STARTED)) {
				mMessageHandler.postDelayed(mDiscovery, ConnectionLibContants.SSDP_LOOP_DELAY_MS);
			}
		}
	};

	private MessageController mMessageController = null;

	private Handler mMessageHandler = null;

	private boolean mRunDiscovery;

	private ServiceState mServiceState = ServiceState.STOPPED;

	private boolean mSocketOpen;

	/**
	 * Constructor for SSDP.
	 * 
	 * @param name
	 *            String
	 * @param m_messageController
	 *            MessageController
	 * @param context
	 *            Context
	 */
	private SsdpService() {
		super(SsdpService.class.getSimpleName());
	}

	{
		mDiscoveredDevicesSet = new HashSet<String>();
		mBaseParser = new BaseUrlParser();
		mServiceState = ServiceState.STOPPED;
		mRunDiscovery = false;
		mDeviceListModel = new DeviceListModel();
	}

	/**
	 * Method addDevice.
	 * 
	 * @param ssdpPacket
	 *            SSDPPacket
	 */
	private void addDevice(DeviceModel device) {
		if (null != device) {
			if ((null == device.getNts()) || device.getNts().contains(ConnectionLibContants.SSDP_ALIVE)) {
				mDiscoveredDevicesSet.add(device.getUsn());
				if (!mDeviceListModel.getAliveDevicesMap().containsKey(device.getUsn())) {
					final String location = device.getLocation();
					if ((location != null) && !location.isEmpty()) {
						try {
							final String xmlDescription = Utils.getHTTP(new URL(location));
							mBaseParser.parse(xmlDescription);
							final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
							if ((deviceList != null) && (deviceList.size() > 0)) {
								device = getRootPhilipsDevice(mBaseParser.getDevicesList(), device);
								// Log.v(LOG, "[after device**]%% " + device);
								if ((null != device) && (null != mMessageController)) {
									mMessageController.sendInternalMessage(MessageID.DEVICE_DISCOVERED, device);
									mDeviceListModel.addNewDevice(device.getUsn(), device);
									mDevicesDiscovered = true;
									// Log.d(LOG, "[addDevice] mAliveDevicesMap " +
									// mDeviceListModel.getAliveDevicesMap());
								} else {
									Log.d(ConnectionLibContants.LOG_TAG, "[addDevice]%% device is not philips ");
								}
							}
						} catch (final MalformedURLException e) {
							Log.e(ConnectionLibContants.LOG_TAG,
							        "[MalformedURLException] MalformedURLException " + e.getMessage());
						}
					} else {
						Log.d(ConnectionLibContants.LOG_TAG, "ssdpPacket.LOCATION " + device.getLocation());
					}
				}
			} else if ((null != device.getNts()) && device.getNts().contains(ConnectionLibContants.SSDP_BYEBYE)) {
				mDiscoveredDevicesSet.remove(device.getUsn());
				mDeviceListModel.removeDevice(device);
				mMessageController.sendInternalMessage(MessageID.DEVICE_LOST, new DeviceModel(device.getUsn()));
			}
		}
	}

	/**
	 * Method closeSocket.
	 */
	private native void closeSocket();

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
		if (null != mDeviceListModel) {
			deviceModel = mDeviceListModel.getAliveDevices();
		}
		return deviceModel;
	}

	private DeviceModel getRootPhilipsDevice(final List<SSDPdevice> devicesList, final DeviceModel pdevice) {
		final DeviceModel device = pdevice;
		if ((null != devicesList) && (devicesList.size() > 0)) {
			final SSDPdevice ssdpDevice = devicesList.get(0);
			Log.d(ConnectionLibContants.LOG_TAG, "devicesList is " + devicesList + "ssdpDevice is " + ssdpDevice);
			// if
			// (ssdpDevice.getManufacturer().toLowerCase().contains(DeviceModel.MANUFACTURER_PHILIPS))
			// {
			// && !ssdpDevice.getModelName ().toLowerCase().contains("nmr")) {
			// TODO : Why this is required need clarifications on this
			if (null != ssdpDevice) {
				if (null != pdevice) {
					ssdpDevice.setBaseURL(pdevice.getLocation().replaceAll("[\\w\\.]*$", ""));
				}
				if (null != device) {
					device.setFromSSDP(ssdpDevice);
					Log.d(ConnectionLibContants.LOG_TAG, "device is " + device);
				}
			}
			// } else {
			// // if the manufacturer is not philips then no need to consider the device
			// device = null;
			// }
		}

		return device;
	}

	/**
	 * Method openSocket.
	 * 
	 * @return int
	 */
	private native int openSocket();

	/**
	 * Method registerCallback.
	 * 
	 * @param pcallbackHandler
	 *            Callback
	 */
	private void registerCallback(final Callback pcallbackHandler) {
		mMessageController = MessageController.getInstance();
		mMessageController.setCallback(pcallbackHandler);

		mMessageHandler = new Handler() {
			@Override
			public void handleMessage(final Message message) {
				if (null != message) {
					addDevice((DeviceModel) message.obj);
				}
			}
		};
	}

	/**
	 * Method registerHandler.
	 * 
	 * @param pHandler
	 *            Handler
	 */
	private void registerHandler(final Handler pHandler) {
		mMessageController = MessageController.getInstance();
		mMessageController.addMessageHandler(pHandler);
		mMessageHandler = new Handler() {
			@Override
			public void handleMessage(final Message message) {
				if (null != message) {
					addDevice((DeviceModel) message.obj);
				}
			}
		};
	}

	/**
	 * Method registerListener.
	 * 
	 * @return int
	 */
	private native int registerListener();

	/**
	 * Method sendBroadcastMX3.
	 * 
	 * @return int
	 */
	private native int sendBroadcastMX3();

	/**
	 * Method sendBroadcastMX5.
	 * 
	 * @return int
	 */
	private native int sendBroadcastMX5();

	/**
	 * Method ssdpCallback.
	 * 
	 * @param pNts
	 *            String
	 * @param pUsn
	 *            String
	 * @param pLocation
	 *            String
	 */
	protected void ssdpCallback(final String pNts, String pUsn, final String pLocation) {
		synchronized (SsdpService.class) {
			if ((pLocation != null) && (!pLocation.isEmpty())) {
				if ((null != pUsn) && pUsn.isEmpty()) {
					pUsn = pUsn.replace(ConnectionLibContants.SSDP_ROOT_DEVICE, "");
				}
				final URL url;
				DeviceModel deviceParam = null;
				try {
					// Added to fetch ip and port from location/url
					url = new URL(pLocation);
					final String ipAddress = (InetAddress.getByName(url.getHost()).getHostAddress());
					final int port = url.getPort();
					// End
					deviceParam = new DeviceModel(pNts, pUsn, pLocation, ipAddress, port);
				} catch (final MalformedURLException e) {
					Log.e(ConnectionLibContants.LOG_TAG, "MalformedURLException : " + e.getMessage());
				} catch (final UnknownHostException e) {
					Log.e(ConnectionLibContants.LOG_TAG, "UnknownHostException : " + e.getMessage());
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
	 * @param callback
	 *            Callback
	 */
	public void startDeviceDiscovery(final Callback callback) {
		if (null != callback) {
			registerCallback(callback);
		}
		startDiscovering();
	}

	/**
	 * Method startDeviceDiscovery.
	 * 
	 * @param pHandler
	 *            Handler
	 */
	public void startDeviceDiscovery(final Handler pHandler) {
		if (null != pHandler) {
			registerHandler(pHandler);
		}
		startDiscovering();
	}

	/**
	 * Method enableDiscovery.
	 */
	private void startDiscovering() {
		if (mServiceState != ServiceState.STARTED) {
			// throw (new IllegalStateException("SSDP not initialized"));
			// Is it better not to throw exception
			mServiceState = ServiceState.STARTED;

			if (!super.isAlive()) {
				super.start();
			}
		}
		if (!mSocketOpen && (openSocket() == 0)) {
			mSocketOpen = true;
			registerListener();
			sendBroadcastMX3();
			sendBroadcastMX5();
			startDiscovery();
			mRunDiscovery = true;
			if (mMessageHandler != null) {
				mMessageHandler.postDelayed(mDiscovery, ConnectionLibContants.SSDP_LOOP_DELAY_MS);
			}
		} else {
			// Need to discuss with Arvind regarding restarting if its already running.
		}
	}

	/**
	 * Method startDiscovery.
	 * 
	 * @return int
	 */
	private native int startDiscovery();

	/**
	 * This API is used to safely stop device discovery service which intern takes care of removing registered handlers from broadcasting list
	 */
	public void stopDeviceDiscovery() {
		if (mServiceState == ServiceState.STARTED) {
			if (mSocketOpen) {
				mSocketOpen = false;
				mRunDiscovery = false;
				closeSocket();
				stopDiscovery();
				if ((null != mMessageHandler) && (null != mDiscovery)) {
					mMessageHandler.removeCallbacks(mDiscovery);
				}
				if ((null != mMessageController) && (null != mMessageHandler)) {
					mMessageController.removeMessageHandler(mMessageHandler);
				}
				mServiceState = ServiceState.STOPPED;
			}
		}

	}

	/**
	 * Method stopDiscovery.
	 */
	private native void stopDiscovery();

}
