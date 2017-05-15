package com.philips.platform.appinfra.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Requirements {

	@SerializedName("minimumOSVersion")
	@Expose
	private String minimumOSVersion;

	public String getMinimumOSVersion() {
		return minimumOSVersion;
	}

	public void setMinimumOSVersion(String minimumOSVersion) {
		this.minimumOSVersion = minimumOSVersion;
	}

}
