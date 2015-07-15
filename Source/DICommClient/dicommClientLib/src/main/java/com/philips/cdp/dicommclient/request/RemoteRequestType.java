/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

public enum RemoteRequestType {
	
	PUT_PROPS("PUTPROPS"),
	GET_PROPS("GETPROPS"),
	ADD_PROPS("ADDPROPS"),
	DEL_PROPS("DELPROPS"),
	SUBSCRIBE("SUBSCRIBE"),
	UNSUBSCRIBE("UNSUBSCRIBE");
	
	public final String mMethod;
	RemoteRequestType(String method){
		mMethod = method;
	}
	
	public String getMethod(){
		return mMethod;
	}
	
}