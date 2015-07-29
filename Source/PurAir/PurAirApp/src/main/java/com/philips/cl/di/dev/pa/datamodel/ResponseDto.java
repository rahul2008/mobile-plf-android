package com.philips.cl.di.dev.pa.datamodel;

public class ResponseDto {

	private int mResponseCode;
	private String mResponseData;
	public ResponseDto(int responseCode, String responseData) {
		mResponseCode=responseCode;
		mResponseData=responseData;
	}
	
	public int getResponseCode(){
		return mResponseCode;
	}
	
	public String getResponseData(){
		return mResponseData;
	}
}
