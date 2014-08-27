package com.philips.cl.di.dev.pa.ews;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.ALog;

public class EWSWifiManager {

	public static final String DEVICE_SSID = "PHILIPS Setup";

	public static boolean connectToPhilipsSetup() {
		ALog.i(ALog.EWS, "connectToPhilipsSetup");

		int networkId = getConfiguredNetworkId(DEVICE_SSID);
		if (networkId == -1) {
			configureOpenNetwork(DEVICE_SSID);
		}

		if (!connectToConfiguredNetwork(DEVICE_SSID)) {
			ALog.i(ALog.EWS, "Failed to connect to Philips setup try again");
			if (networkId == -1) {
				configureOpenNetwork(DEVICE_SSID);
			}
		}

		if (!connectToConfiguredNetwork(DEVICE_SSID)) {
			ALog.i(ALog.EWS, "Failed to connect to Philips setup");
			return false;
		}

		ALog.i(ALog.EWS, "Successfully connected to Philips setup");
		return true;
	}

	public static boolean connectToHomeNetwork(String ssid) {
		ALog.i(ALog.EWS, "connectto Home Network");
		if (ssid == null) {
			ALog.i(ALog.EWS, "Failed to connect to Home network - ssid is null");
			return false;
		}
		if (!connectToConfiguredNetwork(ssid)) {
			ALog.i(ALog.EWS, "Failed to connect to Home network");
			return false;
		}

		ALog.i(ALog.EWS, "Successfully connected to Home network");
		return true;
	}

	public static String getSsidOfConnectedNetwork() {
		ALog.i(ALog.EWS, "getSSIDofConnectedNetwork");
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo connectedWifiNetwork = wifiMan.getConnectionInfo();
		if (connectedWifiNetwork == null)
			return null;

		String currentSsid = connectedWifiNetwork.getSSID();
		if (currentSsid == null)
			return null;

		ALog.i(ALog.EWS, "Ssid of connected network: " + currentSsid);
		currentSsid = currentSsid.replace("\"", ""); // TODO replace to
														// Wifimethod
		return currentSsid;
	}

	public static boolean isOpenNetwork(String ssid) {
		ALog.i(ALog.EWS, "isOpenNetwork " + ssid);

		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> results = wifiMan.getScanResults();

		if (results == null || results.size() < 1) {
			return false;
		}

		for (ScanResult scanResult : results) {
			if (scanResult.SSID != null && scanResult.SSID.equals(ssid)) {
				if (scanResult.capabilities.contains("WPA")) {
					return false;
				} else if (scanResult.capabilities.contains("WEP")) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Connect to a configured network.
	 * 
	 * @return
	 */
	private static boolean connectToConfiguredNetwork(String ssid) {
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);

		ScanResult result = getScanResult(ssid, 6);
		if (result == null) {
			return false;
		}

		// Fix for WEP networks
		wifiMan.startScan();

		Wifi wifi = new Wifi();
		final String security = wifi.ConfigSec.getScanResultSecurity(result);
		final WifiConfiguration config = wifi.getWifiConfiguration(wifiMan,
				result, security);
		final boolean connect = wifi.connectToConfiguredNetwork(
				PurAirApplication.getAppContext(), wifiMan, config, false);

		if (!connect) {
			ALog.i(ALog.EWS, "Failed to connect to network - reconnect failed");
			return false;
		}

		ALog.i(ALog.EWS, "Successfully connected to network");
		return true;
	}

	/**
	 * @return -1 if not yet configured, else network id
	 */
	private static int getConfiguredNetworkId(String ssid) {
		WifiConfiguration config = getWifiConfiguration(ssid);
		if (config == null)
			return -1;

		return config.networkId;
	}

	private static WifiConfiguration getWifiConfiguration(String ssid) {
		ssid = ssid.replace("\"", "");

		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> configuredNetworks = wifiMan
				.getConfiguredNetworks();

		if (configuredNetworks == null || configuredNetworks.size() < 1) {
			return null;
		}

		for (WifiConfiguration config : configuredNetworks) {
			String configSsid = config.SSID.replace("\"", "");
			if (configSsid.equals(ssid)) {
				return config;
			}
		}
		return null;
	}

	private static int configureOpenNetwork(String ssid) {
		WifiConfiguration wfc = new WifiConfiguration();

		wfc.SSID = "\"".concat(ssid).concat("\"");
		wfc.status = WifiConfiguration.Status.DISABLED;
		wfc.priority = 40;

		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wfc.allowedAuthAlgorithms.clear();
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);
		int networkId = wifiMan.addNetwork(wfc);

		if (networkId != -1) {
			ALog.i(ALog.EWS, "Successfully configured open network: " + ssid);
		} else {
			ALog.i(ALog.EWS, "Failed to configure open network: " + ssid);
		}
		return networkId;
	}

	private static ScanResult getScanResult(String ssid, int maxAttempts) {
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);
		ScanResult result = null;
		int attempt = 0;

		while (result == null && attempt < maxAttempts) {
			attempt++;
			result = getScanResult(ssid);

			if (result == null) {
				wifiMan.startScan();
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// NOP
				}
			}
		}

		return result;
	}

	private static ScanResult getScanResult(String ssid) {
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> foundNetworks = wifiMan.getScanResults();

		if (foundNetworks == null || foundNetworks.size() < 1) {
			return null;
		}

		for (ScanResult foundNetwork : foundNetworks) {
			if (foundNetwork.SSID.equals(ssid)) {
				ALog.d(ALog.EWS, "found scanresult");
				return foundNetwork;
			}
		}
		ALog.d(ALog.EWS, "unable to find scanresult");
		return null;
	}

	/**
	 * Taken from:
	 * http://stackoverflow.com/questions/20551658/disabling-samsung-
	 * auto-network-switch-for-wifi-connection Checks whether the
	 * "Avoid poor networks" setting (named "Auto network switch" on some
	 * Samsung devices) is enabled, which blocks connection with the Soft-access
	 * point during speaker setup.
	 * 
	 * @return true if the "Avoid poor networks" or "Auto network switch"
	 *         setting is enabled
	 */
	@SuppressLint("NewApi")
	public static boolean isPoorNetworkAvoidanceEnabled(Context ctx) {
		final int SETTING_UNKNOWN = -1;
		final int SETTING_ENABLED = 1;
		final String AVOID_POOR = "wifi_watchdog_poor_network_test_enabled";
		final String WATCHDOG_CLASS = "android.net.wifi.WifiWatchdogStateMachine";
		final String DEFAULT_ENABLED = "DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED";
		final ContentResolver cr = ctx.getContentResolver();

		int result;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			// Setting was moved from Secure to Global as of JELLY_BEAN_MR1
			result = Settings.Global.getInt(cr, AVOID_POOR, SETTING_UNKNOWN);
		}
		// else if (Build.VERSION.SDK_INT >=
		// Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
		// result = Settings.Secure.getInt(cr, AVOID_POOR, SETTING_UNKNOWN);
		// }
		else {
			// Poor network avoidance not introduced until
			// ICE_CREAM_SANDWICH_MR1
			// See android.provider.Settings.java
			return false;
		}

		// Exit here if the setting value is known
		if (result != SETTING_UNKNOWN) {
			return (result == SETTING_ENABLED);
		}

		// Setting does not exist in database, so it has never been changed.
		// It will be initialized to the default value.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			// As of JELLY_BEAN_MR1, a constant was added to
			// WifiWatchdogStateMachine to determine
			// the default behavior of the Avoid Poor Networks setting.
			try {
				// In the case of any failures here, take the safe route and
				// assume the
				// setting is disabled to avoid disrupting the user with false
				// information
				Class<?> wifiWatchdog = Class.forName(WATCHDOG_CLASS);
				Field defValue = wifiWatchdog.getField(DEFAULT_ENABLED);
				if (!defValue.isAccessible())
					defValue.setAccessible(true);
				return defValue.getBoolean(null);
			} catch (IllegalAccessException ex) {
				return false;
			} catch (NoSuchFieldException ex) {
				return false;
			} catch (ClassNotFoundException ex) {
				return false;
			} catch (IllegalArgumentException ex) {
				return false;
			}
		} else {
			// Prior to JELLY_BEAN_MR1, the default for the Avoid Poor Networks
			// setting was
			// to enable it unless explicitly disabled
			return true;
		}
	}
}
