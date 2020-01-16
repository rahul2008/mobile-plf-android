package com.philips.cdp.prxclient.datamodels.specification;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CsItems implements Serializable {

	@SerializedName("csItem")
	@Expose
	public List<CsItemItem> csItem;
}