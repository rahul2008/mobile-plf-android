package com.philips.cl.di.dev.pa.ews;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.ALog;

public class EWSWifiManager {
	
	public static final String DEVICE_SSID = "PHILIPS Setup" ;
	
	public static boolean connectToPhilipsSetup() {
		ALog.i(ALog.EWS, "connectToPhilipsSetup");
		int networkId = getConfiguredNetworkId(DEVICE_SSID);
		if (networkId == -1) {
			configureOpenNetwork(DEVICE_SSID);
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
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		
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
		final boolean connect = wifi.connectToConfiguredNetwork(PurAirApplication.getAppContext(), wifiMan, config, false);

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
	
	private static ScanResult getScanResult(String ssid, int maxAttempts) {
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
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
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> foundNetworks = wifiMan.getScanResults();
		
		for (ScanResult foundNetwork : foundNetworks) {
			if (foundNetwork.SSID.equals(ssid)) {
				ALog.d(ALog.EWS, "found scanresult");
				return foundNetwork;
			}
		}
		ALog.d(ALog.EWS, "unable to find scanresult");
		return null;
	}

}
