/*
 * © Koninklijke Philips N.V., 2015, 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

public enum LanRequestType {
	
	POST("POST"),
	DELETE("DELETE"),
	PUT("PUT"),
	GET("GET");
	
	public final String mMethod;
	LanRequestType(String method){
		mMethod = method;
	}
	
	public String getMethod(){
		return mMethod;
	}
	
}
