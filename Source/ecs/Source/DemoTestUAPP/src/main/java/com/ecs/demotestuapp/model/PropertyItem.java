package com.ecs.demotestuapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyItem implements Serializable {

	@SerializedName("name")
	public String name;

	@SerializedName("type")
	public String type;

	@SerializedName("apiNumber")
	public int apiNumber;

	@SerializedName("needInput")
	public boolean needInput;

	@SerializedName("fetchProductInput")
	public FetchProductInput fetchProductInput;

	@SerializedName("property")
	public List<PropertyItem> property = new ArrayList<>();

	public List<PropertyItem> getProperty() {
		return property;
	}

	private List<String> ctns;

	public List<String> getCtns() {
		return ctns;
	}

	public void setCtns(List<String> ctns) {
		this.ctns = ctns;
	}
}