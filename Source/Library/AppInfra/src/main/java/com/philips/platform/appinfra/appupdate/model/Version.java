package com.philips.platform.appinfra.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Version {

	@SerializedName("minimumVersion")
	@Expose
	private String minimumVersion;
	@SerializedName("sunsetVersion")
	@Expose
	private String sunsetVersion;
	@SerializedName("sunsetDate")
	@Expose
	private String sunsetDate;
	@SerializedName("currentVersion")
	@Expose
	private String currentVersion;

	public String getMinimumVersion() {
		return minimumVersion;
	}

	public void setMinimumVersion(String minimumVersion) {
		this.minimumVersion = minimumVersion;
	}

	public String getSunsetVersion() {
		return sunsetVersion;
	}

	public void setSunsetVersion(String sunsetVersion) {
		this.sunsetVersion = sunsetVersion;
	}

	public String getSunsetDate() {
		return sunsetDate;
	}

	public void setSunsetDate(String sunsetDate) {
		this.sunsetDate = sunsetDate;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

}
