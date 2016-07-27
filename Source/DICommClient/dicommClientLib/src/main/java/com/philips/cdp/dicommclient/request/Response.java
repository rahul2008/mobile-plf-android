/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

public class Response {

	private final String mResponse;
	private final Error mError;
	private final ResponseHandler mResponseHandler;

	public Response(String response, Error error, ResponseHandler responseHandler){
		mResponse = response;
		mError = error;
		mResponseHandler = responseHandler;
	}

	public void notifyResponseHandler(){
		if(mError!=null){
			mResponseHandler.onError(mError, mResponse);
		}else{
			mResponseHandler.onSuccess(mResponse);
		}
	}
	
	public String getResponseMessage(){
		return mResponse;
	}
}
