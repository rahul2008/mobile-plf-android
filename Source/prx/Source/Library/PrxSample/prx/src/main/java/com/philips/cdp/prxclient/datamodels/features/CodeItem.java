package com.philips.cdp.prxclient.datamodels.features;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CodeItem implements Serializable {

	@SerializedName("extent")
	public String extent;

	@SerializedName("number")
	public String number;

	@SerializedName("extension")
	public String extension;

	@SerializedName("code")
	public String code;

	@SerializedName("description")
	public String description;

	@SerializedName("lastModified")
	public String lastModified;

	@SerializedName("locale")
	public String locale;

	@SerializedName("type")
	public String type;

	@SerializedName("asset")
	public String asset;
}