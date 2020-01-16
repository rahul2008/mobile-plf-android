package com.philips.cdp.prxclient.datamodels.specification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurposeItem implements Serializable {

	@SerializedName("features")
	@Expose
	public Features features;

	@SerializedName("csItems")
	@Expose
	public CsItems csItems;

	@SerializedName("type")
	@Expose
	public String type;
}