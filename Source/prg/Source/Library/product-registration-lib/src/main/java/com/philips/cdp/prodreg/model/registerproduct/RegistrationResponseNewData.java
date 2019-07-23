package com.philips.cdp.prodreg.model.registerproduct;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegistrationResponseNewData implements Serializable {

	@SerializedName("data")
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"RegistrationResponseNewData{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}