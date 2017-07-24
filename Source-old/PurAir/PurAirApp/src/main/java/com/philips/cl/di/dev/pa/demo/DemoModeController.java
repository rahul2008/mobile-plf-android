package com.philips.cl.di.dev.pa.demo;

import android.os.Build;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;

public class DemoModeController {
	
	/**
	 * if isCheked is true enable shop demo mode
	 * else disable shop demo mode and start normal mode
	 * @param isChecked boolean
	 * @param activity MainActivity context
	 */
	@SuppressWarnings("deprecation")
	public void toggleShopDemoMode(boolean isChecked, MainActivity activity) {
		PurAirApplication.setDemoModeEnable(isChecked);
		if (!isChecked) {
			if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
				android.provider.Settings.System.putString(activity.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "0");  
			}
			AirPurifier purAirDevice = AirPurifierManager.getInstance().getCurrentPurifier();
			if (purAirDevice != null && purAirDevice.isDemoPurifier()) {
				purAirDevice.getWifiUIPort().disableDemoMode();
			}
			AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
			activity.startNormalMode();
		} else {
			AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(1);
			activity.startDemoMode();
		}
		AirPurifierManager.getInstance().removeCurrentAppliance();
		activity.onAirPurifierChanged();
	}

}
