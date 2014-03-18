package com.philips.cl.di.dev.pa.dto;

public class DeviceInfoDto {
	
	private long id;
	private long bootId;
	private String usn;
	private String cppId;
	private String deviceKey;
	private String deviceName;
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsn() {
		return usn;
	}
	public void setUsn(String usn) {
		this.usn = usn;
	}
	public String getCppId() {
		return cppId;
	}
	public void setCppId(String cppId) {
		this.cppId = cppId;
	}
	public long getBootId() {
		return bootId;
	}
	public void setBootId(long bootId) {
		this.bootId = bootId;
	}
	public String getDeviceKey() {
		return deviceKey;
	}
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}
	
}
