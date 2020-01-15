package com.philips.cdp.prxclient.datamodels.specification;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Features implements Serializable {

	@SerializedName("feature")
	@Expose
	public List<FeatureItem> feature;
}