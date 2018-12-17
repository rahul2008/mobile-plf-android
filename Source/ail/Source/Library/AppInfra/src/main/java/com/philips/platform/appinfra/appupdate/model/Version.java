/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Version {

	@SerializedName("minimumVersion")
	@Expose
	private String minimumVersion;
	@SerializedName("deprecatedVersion")
	@Expose
	private String deprecatedVersion;
	@SerializedName("deprecationDate")
	@Expose
	private String deprecationDate;
	@SerializedName("currentVersion")
	@Expose
	private String currentVersion;

	public String getMinimumVersion() {
		return minimumVersion;
	}

	public void setMinimumVersion(String minimumVersion) {
		this.minimumVersion = minimumVersion;
	}

	public String getDeprecatedVersion() {
		return deprecatedVersion;
	}

	public void setDeprecatedVersion(String deprecatedVersion) {
		this.deprecatedVersion = deprecatedVersion;
	}

	public String getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(String deprecatedDate) {
		this.deprecationDate = deprecatedDate;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
}
