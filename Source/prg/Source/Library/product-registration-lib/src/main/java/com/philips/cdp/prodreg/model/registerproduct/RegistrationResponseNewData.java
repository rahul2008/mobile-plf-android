package com.philips.cdp.prodreg.model.registerproduct;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.io.Serializable;

public class RegistrationResponseNewData extends ResponseData {

	@SerializedName("data")
	private Data data;

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData(){
		return data;
	}

//	@Override
// 	public String toString(){
//		return
//			"RegistrationResponseNewData{" +
//			"data = '" + data + '\'' +
//			"}";
//		}

	public ResponseData parseJsonResponseData(JSONObject response) {

		RegistrationResponseNewData registrationResponse;
		registrationResponse = new Gson().fromJson(response.toString(), RegistrationResponseNewData.class);
		return registrationResponse;
	}

}