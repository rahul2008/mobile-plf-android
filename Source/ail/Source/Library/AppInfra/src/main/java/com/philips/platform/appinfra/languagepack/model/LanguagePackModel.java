/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Language Pack Model class
 */

public class LanguagePackModel {

	@SerializedName("locale")
	@Expose
	private String mLocale;

	@SerializedName("remoteVersion")
	@Expose
	private String version;

	@SerializedName("url")
	@Expose
	private String mUrl;

	public String getLocale() {
		return mLocale;
	}

	public void setLocale(String locale) {
		this.mLocale = locale;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String remoteVersion) {
		this.version = remoteVersion;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof LanguagePackModel) {
			final LanguagePackModel languagePackModel = (LanguagePackModel) obj;
			return languagePackModel.getLocale().equals(this.getLocale());
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
