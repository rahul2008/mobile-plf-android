package com.philips.cdp.prxclient.datamodels.specification;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

	@SerializedName("filters")
	@Expose
	public Filters filters;

	@SerializedName("csChapter")
	@Expose
	public List<CsChapterItem> csChapter;
}