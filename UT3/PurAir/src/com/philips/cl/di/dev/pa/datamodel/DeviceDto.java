package com.philips.cl.di.dev.pa.datamodel;

public class DeviceDto {
	private String name ;
	private String type ;
	private String modelid ;
	private String swversion ;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModelid() {
		return modelid;
	}
	public void setModelid(String modelid) {
		this.modelid = modelid;
	}
	public String getSwversion() {
		return swversion;
	}
	public void setSwversion(String swversion) {
		this.swversion = swversion;
	}
}
