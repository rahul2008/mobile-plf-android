package com.ecs.demotestuapp.jsonmodel;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SubgroupItem implements Serializable {

	@SerializedName("button")
	private int button;

	@SerializedName("name")
	private String name;

	@SerializedName("editText")
	private int editText;

	@SerializedName("apiNumber")
	private int apiNumber;

	@SerializedName("spinner")
	private int spinner;

	public void setButton(int button){
		this.button = button;
	}

	public int getButton(){
		return button;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEditText(int editText){
		this.editText = editText;
	}

	public int getEditText(){
		return editText;
	}

	public void setApiNumber(int apiNumber){
		this.apiNumber = apiNumber;
	}

	public int getApiNumber(){
		return apiNumber;
	}

	public void setSpinner(int spinner){
		this.spinner = spinner;
	}

	public int getSpinner(){
		return spinner;
	}
}