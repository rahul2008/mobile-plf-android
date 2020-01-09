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

	public String getSingleAssetImageFromFeatureCode(String featureCode){

		for (CodeItem codeItem:code) {
			if(isImage(codeItem.extension)){
              return codeItem.asset;
			}
		}
		return null;
	}

	private boolean isImage(String extension) {
		return true;
	}
}