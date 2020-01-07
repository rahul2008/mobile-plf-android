package com.philips.cdp.prxclient.datamodels.features;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

	@SerializedName("keyBenefitArea")
	public List<KeyBenefitAreaItem> keyBenefitArea;

	@SerializedName("code")
	public List<CodeItem> code;

	@SerializedName("featureHighlight")
	public List<FeatureHighlightItem> featureHighlight;
}