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

	@SerializedName("property")
	public List<PropertyItem> property = new ArrayList<>();

	public List<PropertyItem> getProperty() {
		return property;
	}
}