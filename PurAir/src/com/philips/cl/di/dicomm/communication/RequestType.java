package com.philips.cl.di.dicomm.communication;

public enum RequestType {
	POST("POST"),
	DELETE("DELETE"),
	PUT("PUT"),
	GET("GET");
	
	public final String mMethod;
	RequestType(String method){
		mMethod = method;
	}
	
	public String getMethod(){
		return mMethod;
	}
	
}