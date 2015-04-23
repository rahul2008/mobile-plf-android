package com.philips.cl.di.dicomm.communication;


public interface ResponseHandler {
	
	void onSuccess(String data);
	void onError(Error error, String errorData);
}
