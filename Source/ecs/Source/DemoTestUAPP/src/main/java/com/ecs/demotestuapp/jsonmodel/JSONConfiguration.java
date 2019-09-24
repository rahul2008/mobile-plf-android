package com.ecs.demotestuapp.jsonmodel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class JSONConfiguration implements Serializable {

	@SerializedName("group")
	private List<GroupItem> group;

	public void setGroup(List<GroupItem> group){
		this.group = group;
	}

	public List<GroupItem> getGroup(){
		return group;
	}
}