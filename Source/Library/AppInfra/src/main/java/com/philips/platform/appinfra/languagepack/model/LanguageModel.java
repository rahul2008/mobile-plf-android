package com.philips.platform.appinfra.languagepack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkavya on 3/21/17.
 */

public class LanguageModel {

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
		LanguageModel languageModel = (LanguageModel) obj;
		return languageModel.getLocale().equals(this.getLocale());
	}
}
