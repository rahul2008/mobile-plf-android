package com.philips.cl.di.dev.pa.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.philips.cl.di.dev.pa.PurAirApplication;

public class LocationUtils {
	public final static String CURR_LOC_PREF = "current_loc_pref";
	public final static String CURR_LOC_AREAID = "current_loc_aid";
	public final static String CURR_LOC_LAT = "current_loc_lat";
	public final static String CURR_LOC_LON = "current_loc_lon";
	public final static String OUTDOOR_LOCATION_VISITED = "outdoor_location_visited";
	public final static String CURR_LOC_ENABLEB = "current_loc_enabled";
	
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
	
	public static void saveCurrentLocationLatLon(String lat, String lon) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(CURR_LOC_LAT, lat);
		edit.putString(CURR_LOC_LON, lon);
		edit.commit();
	}
	
	public static String getCurrentLocationLat() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getString(CURR_LOC_LAT, "");
	}
	
	public static String getCurrentLocationLon() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getString(CURR_LOC_LON, "");
	}
	
	public static void saveCurrentLocationEnabled(boolean enabled) {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean(CURR_LOC_ENABLEB, enabled);
		edit.commit();
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorController$current location enabled " + enabled);
	}
	
	public static boolean isCurrentLocationEnabled() {
		SharedPreferences pref = PurAirApplication.getAppContext()
				.getSharedPreferences(CURR_LOC_PREF, Activity.MODE_PRIVATE);
		return pref.getBoolean(CURR_LOC_ENABLEB, true);
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
