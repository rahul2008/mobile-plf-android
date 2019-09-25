package com.ecs.demotestuapp.jsonmodel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GroupItem implements Serializable {

	@SerializedName("subgroup")
	private List<SubgroupItem> subgroup;

	@SerializedName("name")
	private String name;

	public void setSubgroup(List<SubgroupItem> subgroup){
		this.subgroup = subgroup;
	}

	public List<SubgroupItem> getSubgroup(){
		return subgroup;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}