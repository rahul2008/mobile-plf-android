package com.philips.cl.di.dev.pa.datamodel;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils.FanSpeed;

/***
 * Air Purifier Event DTO class This contains all the Air Purifier event data
 * 
 * @author 310124914
 * 
 */
public class AirPortProperties {

	private static final String TAG = "AirPurifierEvent";

	private boolean isValid;
	private int childLock;
	private String timeStamp;

	private String fanSpeed;
	private String powerMode;

	private int preFilterStatus;
	private int multicareFilterStatus;
	private int activeFilterStatus;
	private int hepaFilterStatus;

	private String replaceFilter1;
	private String replaceFilter2;
	private String replaceFilter3;
	private String replaceFilter4;

	private int dtrs;
	private int aqiThreshold;

	private int pSensor;
	private int tFav;
	private String actualFanSpeed;
	private int aqiL;
	private int indoorAQI;

	private int outdoorAirQuality;
	private String machineMode;

	public int getPreFilterStatus() {
		return AppConstants.PRE_FILTER_MAX_VALUE - preFilterStatus;
	}

	public void setPreFilterStatus(int preFilterStatus) {
		this.preFilterStatus = preFilterStatus;
	}

	public int getMulticareFilterStatus() {
		return AppConstants.MULTI_CARE_FILTER_MAX_VALUE - multicareFilterStatus;
	}

	public void setMulticareFilterStatus(int multicareFilterStatus) {
		this.multicareFilterStatus = multicareFilterStatus;
	}

	public int getActiveFilterStatus() {
		return AppConstants.ACTIVE_CARBON_FILTER_MAX_VALUE - activeFilterStatus;
	}

	public void setActiveFilterStatus(int activeFilterStatus) {
		this.activeFilterStatus = activeFilterStatus;
	}

	public int getHepaFilterStatus() {
		return AppConstants.HEPA_FILTER_MAX_VALUE - hepaFilterStatus;
	}

	public void setHepaFilterStatus(int hepaFilterStatus) {
		this.hepaFilterStatus = hepaFilterStatus;
	}

	public int getChildLock() {
		return childLock;
	}

	public void setChildLock(int childLock) {
		this.childLock = childLock;
	}

	public int getpSensor() {
		return pSensor;
	}

	public void setpSensor(int pSensor) {
		this.pSensor = pSensor;
	}

	public int gettFav() {
		return tFav;
	}

	public void settFav(int tFav) {
		this.tFav = tFav;
	}

	public String getReplaceFilter1() {
		return replaceFilter1;
	}

	public void setReplaceFilter1(String replaceFilter1) {
		this.replaceFilter1 = replaceFilter1;
	}

	public String getReplaceFilter2() {
		return replaceFilter2;
	}

	public void setReplaceFilter2(String replaceFilter2) {
		this.replaceFilter2 = replaceFilter2;
	}

	public String getReplaceFilter3() {
		return replaceFilter3;
	}

	public void setReplaceFilter3(String replaceFilter3) {
		this.replaceFilter3 = replaceFilter3;
	}

	public String getReplaceFilter4() {
		return replaceFilter4;
	}

	public void setReplaceFilter4(String replaceFilter4) {
		this.replaceFilter4 = replaceFilter4;
	}

	public int getDtrs() {
		return dtrs;
	}

	public void setDtrs(int dtrs) {
		this.dtrs = dtrs;
	}

	public int getAqiThreshold() {
		return aqiThreshold;
	}

	public void setAqiThreshold(int aqiThreshold) {
		this.aqiThreshold = aqiThreshold;
	}

	public int getAqiL() {
		return aqiL;
	}

	public void setAqiL(int aqiL) {
		this.aqiL = aqiL;
	}

	public int getIndoorAQI() {
		return indoorAQI;
	}

	public void setIndoorAQI(int indoorAQI) {
		this.indoorAQI = indoorAQI;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public FanSpeed getFanSpeed() {
		if(fanSpeed == null || fanSpeed.isEmpty()) return null;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_SILENT)) return FanSpeed.SILENT;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_AUTO)) return FanSpeed.AUTO;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_TURBO)) return FanSpeed.TURBO;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_ONE)) return FanSpeed.ONE;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_TWO)) return FanSpeed.TWO;
		if(fanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_THREE)) return FanSpeed.THREE;
		return null;
	}

	public void setFanSpeed(String fanSpeed) {
		this.fanSpeed = fanSpeed;
	}

	public int getOutdoorAirQuality() {
		return outdoorAirQuality;
	}

	public void setOutdoorAirQuality(int outdoorAirQuality) {
		this.outdoorAirQuality = outdoorAirQuality;
	}

	public String getMachineMode() {
		return machineMode;
	}

	public void setMachineMode(String machineMode) {
		this.machineMode = machineMode;
	}

	public String getPowerMode() {
		return powerMode;
	}

	public void setPowerMode(String powerMode) {
		this.powerMode = powerMode;
	}

	public String getActualFanSpeed() {
		return actualFanSpeed;
	}

	public void setActualFanSpeed(String actualFanSpeed) {
		this.actualFanSpeed = actualFanSpeed;
	}

	/**
	 * ToString implementation
	 */
	@Override
	public String toString() {
		return TAG;
	}
}
