package com.ecs.demotestuapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Config implements Serializable {

	@SerializedName("buttonConfig")
	public ButtonConfig buttonConfig;
}