package com.philips.cl.di.dev.pa.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.philips.cl.di.dev.pa.PurAirApplication;

public class LocationUtils {
	public static String CURR_LOC_PREF = "current_loc_pref";
	public static String CURR_LOC_AREAID = "current_loc_aid";
	public static String EWS_STATE = "ews_state";
	public static String OUTDOOR_LOCATION_VISITED = "outdoor_location_visited";
	public static String CURR_LOC_ENABLEB = "current_loc_enabled";
	
	public static void saveCurrentLocationAreaId(String areaId) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(CURR_LOC_AREAID, areaId);
		edit.commit();
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorController$current location areaID " + areaId);
	}
	
	public static String getCurrentLocationAreaId() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getString(CURR_LOC_AREAID, "");
	}
	
	public static void saveCurrentLocationEnabled(boolean enabled) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean(CURR_LOC_ENABLEB, enabled);
		edit.commit();
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorController$current location enabled " + enabled);
	}
	
	public static boolean getCurrentLocationEnabled() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getBoolean(CURR_LOC_ENABLEB, true);
	}
	
	public static void saveFirstTimeEWSState(boolean state) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean(EWS_STATE, state);
		edit.commit();
	}
	
	public static boolean getFirstTimeEWSState() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getBoolean(EWS_STATE, false);
	}
	
	public static void saveDashboardWithoutPurifierState(boolean state) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean(OUTDOOR_LOCATION_VISITED, state);
		edit.commit();
	}
	
	public static boolean getDashboardWithoutPurifierState() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getBoolean(OUTDOOR_LOCATION_VISITED, false);
	}
}
