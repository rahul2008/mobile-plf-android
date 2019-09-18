package com.ecs.demotestuapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ButtonConfig implements Serializable {

	@SerializedName("property")
	public List<PropertyItem> property;

	@SerializedName("id")
	public int id;
}