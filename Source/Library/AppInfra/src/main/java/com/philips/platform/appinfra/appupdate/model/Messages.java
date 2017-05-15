package com.philips.platform.appinfra.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Messages {

	@SerializedName("minimumVersionMessage")
	@Expose
	private String minimumVersionMessage;
	@SerializedName("sunsetVersionMessage")
	@Expose
	private String sunsetVersionMessage;
	@SerializedName("currentVersionMessage")
	@Expose
	private String currentVersionMessage;

	public String getMinimumVersionMessage() {
		return minimumVersionMessage;
	}

	public void setMinimumVersionMessage(String minimumVersionMessage) {
		this.minimumVersionMessage = minimumVersionMessage;
	}

	public String getSunsetVersionMessage() {
		return sunsetVersionMessage;
	}

	public void setSunsetVersionMessage(String sunsetVersionMessage) {
		this.sunsetVersionMessage = sunsetVersionMessage;
	}

	public String getCurrentVersionMessage() {
		return currentVersionMessage;
	}

	public void setCurrentVersionMessage(String currentVersionMessage) {
		this.currentVersionMessage = currentVersionMessage;
	}

}
