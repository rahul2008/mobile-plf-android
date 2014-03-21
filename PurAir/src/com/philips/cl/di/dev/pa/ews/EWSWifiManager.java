package com.philips.cl.di.dev.pa.ews;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.philips.cl.di.dev.pa.pureairui.PurAirApplication;
import com.philips.cl.di.dev.pa.utils.ALog;

public class EWSWifiManager {
	
	public static final String DEVICE_SSID = "PHILIPS Setup" ;
	
	public static boolean connectToPhilipsSetup() {
		ALog.i(ALog.EWS, "connectToPhilipsSetup");
		int networkId = getConfiguredNetworkId(DEVICE_SSID);
		if (networkId == -1) {
			networkId = configureOpenNetwork(DEVICE_SSID);
		}
		
		if (!connectToConfiguredNetwork(DEVICE_SSID)) {
			ALog.i(ALog.EWS,"Failed to connect to Philips setup");
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
		
		ALog.i(ALog.EWS,"Successfully connected to Home network");
		return true;
	}
	
	public static String getSsidOfConnectedNetwork() {
		ALog.i(ALog.EWS, "getSSIDofConnectedNetwork");
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);

		WifiInfo connectedWifiNetwork = wifiMan.getConnectionInfo();
		if (connectedWifiNetwork == null ) return null;
		
		String currentSsid = connectedWifiNetwork.getSSID();
		if (currentSsid == null) return null;
		
		ALog.i(ALog.EWS, "Ssid of connected network: " + currentSsid);
		currentSsid = currentSsid.replace("\"", ""); // TODO replace to Wifimethod
		return currentSsid;
	}
	
	public static boolean isOpenNetwork(String ssid) {
		ALog.i(ALog.EWS, "isOpenNetwork " + ssid);
		
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> results = wifiMan.getScanResults();
		
		for (ScanResult scanResult : results) {
			if (scanResult.SSID != null && scanResult.SSID.equals(ssid)) {
				if (scanResult.capabilities.contains("WPA")) {
					return false;
				} else if(scanResult.capabilities.contains("WEP")) {
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
	 * @return
	 */
	private static boolean connectToConfiguredNetwork(String ssid) {
		ALog.i(ALog.EWS, "connecttoConfiguredNetwork: "+ssid);
		WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration config = getWifiConfiguration(ssid);
		if (config == null) {
			ALog.i(ALog.EWS, "Failed to connect to network - configuration null");
			return false;
		}
		
		int oldPri = config.priority;
	
		// Make it the highest priority.
		int newPri = getMaxPriority(wifiManager) + 1;
		if (newPri > 99999) {
			newPri = shiftPriorityAndSave(wifiManager);
			config = getWifiConfiguration(ssid);
			if (config == null) {
				return false;
			}
		}
	
		// Set highest priority to this configured network
		config.priority = newPri;
		int networkId = wifiManager.updateNetwork(config);
		if (networkId == -1) {
			return false;
		}
	
		wifiManager.disconnect();
		
		// Do not disable others
		if (!wifiManager.enableNetwork(networkId, false)) {
			config.priority = oldPri;
			return false;
		}
	
		if (!wifiManager.saveConfiguration()) {
			config.priority = oldPri;
			return false;
		}
	
		// We have to retrieve the WifiConfiguration after save.
		config = getWifiConfiguration(ssid);
		if (config == null) {
			return false;
		}
	
		// Disable others, but do not save.
		// Just to force the WifiManager to connect to it.
		if (!wifiManager.enableNetwork(config.networkId, true)) {
			return false;
		}
	
		final boolean connect = wifiManager.reconnect();
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
		if (config == null) return -1; 
		
		return config.networkId;
	}

	private static WifiConfiguration getWifiConfiguration(String ssid) {
		ssid = ssid.replace("\"", "");
		
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> configuredNetworks = wifiMan.getConfiguredNetworks();
		
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
		
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		int networkId = wifiMan.addNetwork(wfc);
		
		if (networkId != -1) {
			ALog.i(ALog.EWS, "Successfully configured open network: " + ssid);
		} else {
			ALog.i(ALog.EWS, "Failed to configure open network: " + ssid);
		}
		return networkId;
	}

	private static void sortByPriority(final List<WifiConfiguration> configurations) {
		java.util.Collections.sort(configurations,
				new Comparator<WifiConfiguration>() {
					@Override
					public int compare(WifiConfiguration object1,
							WifiConfiguration object2) {
						return object1.priority - object2.priority;
					}
				});
	}

	private static int shiftPriorityAndSave(final WifiManager wifiMgr) {
	    final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
	    sortByPriority(configurations);
	    final int size = configurations.size();
	    for(int i = 0; i < size; i++) {
	            final WifiConfiguration config = configurations.get(i);
	            config.priority = i;
	            wifiMgr.updateNetwork(config);
	    }
	    wifiMgr.saveConfiguration();
	    return size;
	}

	private static int getMaxPriority(final WifiManager wifiManager) {
	    final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
	    int pri = 0;
	    for(final WifiConfiguration config : configurations) {
	            if(config.priority > pri) {
	                    pri = config.priority;
	            }
	    }
	    return pri;
	}

}
