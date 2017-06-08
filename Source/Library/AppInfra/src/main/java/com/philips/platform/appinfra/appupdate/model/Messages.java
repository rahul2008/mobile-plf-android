/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Messages {

	@SerializedName("minimumVersionMessage")
	@Expose
	private String minimumVersionMessage;
	@SerializedName("deprecatedVersionMessage")
	@Expose
	private String deprecatedVersionMessage;
	@SerializedName("currentVersionMessage")
	@Expose
	private String currentVersionMessage;

	public String getMinimumVersionMessage() {
		return minimumVersionMessage;
	}

	public void setMinimumVersionMessage(String minimumVersionMessage) {
		this.minimumVersionMessage = minimumVersionMessage;
	}

	public String getDeprecatedVersionMessage() {
		return deprecatedVersionMessage;
	}

	public void setDeprecatedVersionMessage(String deprecatedVersionMessage) {
		this.deprecatedVersionMessage = deprecatedVersionMessage;
	}

	public String getCurrentVersionMessage() {
		return currentVersionMessage;
	}

	public void setCurrentVersionMessage(String currentVersionMessage) {
		this.currentVersionMessage = currentVersionMessage;
	}
}
