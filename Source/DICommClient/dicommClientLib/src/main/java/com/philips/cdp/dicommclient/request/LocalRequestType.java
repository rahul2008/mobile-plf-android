/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

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