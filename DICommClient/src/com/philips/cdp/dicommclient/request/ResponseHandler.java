package com.philips.cdp.dicommclient.request;

public interface ResponseHandler {

	void onSuccess(String data);
	void onError(Error error, String errorData);
}
