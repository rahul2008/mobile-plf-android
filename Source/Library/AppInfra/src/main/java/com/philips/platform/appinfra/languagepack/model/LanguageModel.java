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
	private String mRemoteVersion;

	@SerializedName("url")
	@Expose
	private String mUrl;

	public String getLocale() {
		return mLocale;
	}

	public void setLocale(String locale) {
		this.mLocale = locale;
	}

	public String getRemoteVersion() {
		return mRemoteVersion;
	}

	public void setRemoteVersion(String remoteVersion) {
		this.mRemoteVersion = remoteVersion;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

}
