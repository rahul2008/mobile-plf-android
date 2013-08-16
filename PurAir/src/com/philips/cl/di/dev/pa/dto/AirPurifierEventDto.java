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
	private int id;
	private String timeStamp;
	private int lightSensor;
	private double particleSensor;
	private double gasSensor;
	private int motorSpeed;
	private String powerMode; 
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getLightSensor() {
		return lightSensor;
	}
	public void setLightSensor(int lightSensor) {
		this.lightSensor = lightSensor;
	}
	public double getParticleSensor() {
		return particleSensor;
	}
	public void setParticleSensor(double particleSensor) {
		this.particleSensor = particleSensor;
	}
	public double getGasSensor() {
		return gasSensor;
	}
	public void setGasSensor(double gasSensor) {
		this.gasSensor = gasSensor;
	}
	public int getMotorSpeed() {
		return motorSpeed;
	}
	public void setMotorSpeed(int motorSpeed) {
		this.motorSpeed = motorSpeed;
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
