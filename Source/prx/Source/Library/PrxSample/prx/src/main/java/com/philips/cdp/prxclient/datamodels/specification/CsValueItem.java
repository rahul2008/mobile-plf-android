package com.philips.cdp.prxclient.datamodels.specification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CsValueItem implements Serializable {

	@SerializedName("csValueRank")
	@Expose
	public String csValueRank;

	@SerializedName("csValueName")
	@Expose
	public String csValueName;

	@SerializedName("csValueCode")
	@Expose
	public String csValueCode;
}