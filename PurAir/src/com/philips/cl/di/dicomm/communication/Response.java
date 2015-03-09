package com.philips.cl.di.dicomm.communication;

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
			mResponseHandler.onError(mError);
		}else{
			mResponseHandler.onSuccess(mResponse);
		}
	}

}
