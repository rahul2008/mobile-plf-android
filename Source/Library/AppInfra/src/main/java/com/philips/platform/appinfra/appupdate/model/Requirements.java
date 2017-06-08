/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
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
