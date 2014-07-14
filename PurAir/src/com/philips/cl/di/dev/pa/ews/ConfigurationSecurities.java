package com.philips.cl.di.dev.pa.ews;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.util.Log;

/**
 * Code taken from android-wifi-connector library (MIT License)
 * https://code.google.com/p/android-wifi-connecter/
 * 
 * ----------
 * Wifi Connecter
 * 
 * Copyright (c) 20101 Kevin Yuan (farproc@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Kevin Yuan
 */
public class ConfigurationSecurities {
	
	static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;
    
    enum PskType {
        UNKNOWN,
        WPA,
        WPA2,
        WPA_WPA2
    }
    
    private static final String TAG = "ConfigurationSecuritiesV14";
    
    private static int getSecurity(WifiConfiguration config) {
    	if (config == null)  return SECURITY_NONE;
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    private static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }

	public String getWifiConfigurationSecurity(WifiConfiguration wifiConfig) {
		return String.valueOf(getSecurity(wifiConfig));
	}

	public String getScanResultSecurity(ScanResult scanResult) {
		return String.valueOf(getSecurity(scanResult));
	}

	public void setupSecurity(WifiConfiguration config, String security, String password) {
		config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        
        final int sec = security == null ? SECURITY_NONE : Integer.parseInt(security);
        final int passwordLen = password == null ? 0 : password.length();
        switch (sec) {
        case SECURITY_NONE:
            config.allowedKeyManagement.set(KeyMgmt.NONE);
            break;

        case SECURITY_WEP:
            config.allowedKeyManagement.set(KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
            if (passwordLen != 0) {
                // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                if ((passwordLen == 10 || passwordLen == 26 || passwordLen == 58) &&
                        password.matches("[0-9A-Fa-f]*")) {
                    config.wepKeys[0] = password;
                } else {
                    config.wepKeys[0] = '"' + password + '"';
                }
            }
            break;

        case SECURITY_PSK:
            config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
            if (passwordLen != 0) {
                if (password.matches("[0-9A-Fa-f]{64}")) {
                    config.preSharedKey = password;
                } else {
                    config.preSharedKey = '"' + password + '"';
                }
            }
            break;

        case SECURITY_EAP:
            config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
            config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
            break;

        default:
                Log.e(TAG, "Invalid security type: " + sec);
    }
		
	}
	
	private static PskType getPskType(ScanResult result) {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) {
            return PskType.WPA_WPA2;
        } else if (wpa2) {
            return PskType.WPA2;
        } else if (wpa) {
            return PskType.WPA;
        } else {
            Log.w(TAG, "Received abnormal flag string: " + result.capabilities);
            return PskType.UNKNOWN;
        }
    }

	public String getDisplaySecirityString(final ScanResult scanResult) {
		final int security = getSecurity(scanResult);
		if(security == SECURITY_PSK) {
			switch(getPskType(scanResult)) {
			case WPA:
				return "WPA";
			case WPA_WPA2:
			case WPA2:
				return "WPA2";
			default:
				return "?";
			}
		} else {
			switch(security) {
			case SECURITY_NONE:
				return "OPEN";
			case SECURITY_WEP:
				return "WEP";
			case SECURITY_EAP:
				return "EAP";
			}
		}
		
		return "?";
	}

	public boolean isOpenNetwork(String security) {
		return String.valueOf(SECURITY_NONE).equals(security);
	}

}
