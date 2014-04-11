package com.philips.cl.di.dev.pa.datamodel;

public class PurifierDetailDto {
	
	private long id;
	private long bootId;
	private String usn;
	private String cppId;
	private String deviceKey;
	private String deviceName;
	private String deviceIp;
	private boolean isPaired;
	private long lastPairedMillisec;
	
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
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
	public void setPairedStatus(int isPaired){
		if(isPaired==0){
			this.isPaired=false;
		}else{
			this.isPaired=true;
		}
	}
	public boolean getPairedStatus(){
		return isPaired;
	}
	public void setLastPaired(long lastPaired){
		this.lastPairedMillisec=lastPaired;
	}
	public long getLastpaired(){
		return lastPairedMillisec;
	}
}
