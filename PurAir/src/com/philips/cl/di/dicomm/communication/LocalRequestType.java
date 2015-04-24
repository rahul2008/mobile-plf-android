package com.philips.cl.di.dicomm.communication;

public enum LocalRequestType {
	
	POST("POST"),
	DELETE("DELETE"),
	PUT("PUT"),
	GET("GET");
	
	public final String mMethod;
	LocalRequestType(String method){
		mMethod = method;
	}
	
	public String getMethod(){
		return mMethod;
	}
	
}