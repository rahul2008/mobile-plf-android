package com.philips.cdp.prxclient.datamodels.features;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KeyBenefitAreaItem implements Serializable {

	@SerializedName("feature")
	public List<FeatureItem> feature;

	@SerializedName("keyBenefitAreaCode")
	public String keyBenefitAreaCode;

	@SerializedName("keyBenefitAreaName")
	public String keyBenefitAreaName;

	@SerializedName("keyBenefitAreaRank")
	public String keyBenefitAreaRank;
}