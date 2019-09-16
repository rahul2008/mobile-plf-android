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

	@SerializedName("property")
	public List<PropertyItem> property;

	public List<PropertyItem> getProperty() {
		if(property == null){
			property = new ArrayList<>();
		}
		return property;
	}

}