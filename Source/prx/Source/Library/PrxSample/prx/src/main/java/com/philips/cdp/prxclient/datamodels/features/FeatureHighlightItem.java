package com.philips.cdp.prxclient.datamodels.features;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureHighlightItem implements Serializable {

	@SerializedName("featureCode")
	public String featureCode;

	@SerializedName("featureHighlightRank")
	public String featureHighlightRank;

	@SerializedName("featureReferenceName")
	public String featureReferenceName;
}