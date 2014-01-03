package com.philips.cl.di.dev.pa.dto;

/***
 * Air Purifier Event DTO class
 * This contains all the Air Purifier event data
 * @author 310124914
 *
 */
public class AirPurifierEventDto {
	
	private static final String TAG = "AirPurifierEvent";
	
	private boolean isValid;
	public int getFilterStatus1() {
		return filterStatus1;
	}
	public void setFilterStatus1(int filterStatus1) {
		this.filterStatus1 = filterStatus1;
	}
	public int getFilterStatus2() {
		return filterStatus2;
	}
	public void setFilterStatus2(int filterStatus2) {
		this.filterStatus2 = filterStatus2;
	}
	public int getFilterStatus3() {
		return filterStatus3;
	}
	public void setFilterStatus3(int filterStatus3) {
		this.filterStatus3 = filterStatus3;
	}
	public int getFilterStatus4() {
		return filterStatus4;
	}
	public void setFilterStatus4(int filterStatus4) {
		this.filterStatus4 = filterStatus4;
	}


	private int childLock;
	private String timeStamp;
	
	private String fanSpeed;
	private String powerMode; 
	
	
	private int filterStatus1 ;
	private int filterStatus2 ;
	private int filterStatus3 ;
	private int filterStatus4 ;
	
	private String replaceFilter1 ;
	private String replaceFilter2 ;
	private String replaceFilter3 ;
	private String replaceFilter4 ;
	
	private int dtrs ;
	private int aqiThreshold ;
	
	private int pSensor ;
	private int tFav ;
	
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


	private int aqiL ;
	
	public int getIndoorAQI() {
		return indoorAQI;
	}
	public void setIndoorAQI(int indoorAQI) {
		this.indoorAQI = indoorAQI;
	}


	private int indoorAQI ;

	private int outdoorAirQuality;
	private String machineMode;
	
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
	
	
	public String getFanSpeed() {
		return fanSpeed;
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
	
	
	/**
	 * ToString implementation
	 */
	@Override
	public String toString() {
		return TAG ;
	}
}
