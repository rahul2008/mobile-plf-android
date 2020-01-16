package com.philips.cdp.prxclient.datamodels.specification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureItem implements Serializable {

	@SerializedName("code")
	@Expose
	public String code;

	@SerializedName("rank")
	@Expose
	public String rank;

	@SerializedName("referenceName")
	@Expose
	public String referenceName;
}