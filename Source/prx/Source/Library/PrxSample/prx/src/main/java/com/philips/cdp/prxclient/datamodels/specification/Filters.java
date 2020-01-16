package com.philips.cdp.prxclient.datamodels.specification;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filters implements Serializable {

	@SerializedName("purpose")
	@Expose
	public List<PurposeItem> purpose;
}